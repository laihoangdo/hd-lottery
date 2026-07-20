package com.hdplatform.modules.media.adapter.out.storage;

import com.hdplatform.modules.media.application.port.ObjectStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.DigestInputStream;
import java.util.HexFormat;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class LocalObjectStorage implements ObjectStorage {
    private static final Map<String, String> EXTENSIONS = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif",
            "application/pdf", ".pdf");

    private final MediaStorageProperties properties;

    @Override
    public StoredObject store(StorageUpload upload) throws IOException {
        String extension = EXTENSIONS.get(upload.contentType());
        if (extension == null) throw new IOException("Unsupported storage content type");
        String objectKey = upload.tenantId().getValue() + "/"
                + upload.assetId().getValue() + extension;
        Path root = root();
        Path target = safeResolve(root, objectKey);
        Files.createDirectories(target.getParent());
        Path temporary = Files.createTempFile(target.getParent(), ".upload-", ".tmp");
        MessageDigest digest = sha256();
        long copied;
        try (InputStream input = new DigestInputStream(upload.content(), digest)) {
            copied = copyWithLimit(input, temporary, upload.declaredSize());
            if (copied != upload.declaredSize()) {
                throw new IOException("Uploaded size does not match declared size");
            }
            moveAtomically(temporary, target);
        } finally {
            Files.deleteIfExists(temporary);
        }
        return new StoredObject(objectKey, copied, HexFormat.of().formatHex(digest.digest()));
    }

    private long copyWithLimit(InputStream input, Path target, long limit) throws IOException {
        long total = 0;
        byte[] buffer = new byte[8192];
        try (OutputStream output = Files.newOutputStream(
                target, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE)) {
            int read;
            while ((read = input.read(buffer)) != -1) {
                total += read;
                if (total > limit) {
                    throw new IOException("Uploaded size exceeds declared size");
                }
                output.write(buffer, 0, read);
            }
        }
        return total;
    }

    @Override
    public StoredContent load(String objectKey) throws IOException {
        Path path = safeResolve(root(), objectKey);
        if (!Files.isRegularFile(path)) throw new IOException("Stored object not found");
        return new StoredContent(Files.newInputStream(path, StandardOpenOption.READ), Files.size(path));
    }

    @Override
    public void delete(String objectKey) throws IOException {
        Files.deleteIfExists(safeResolve(root(), objectKey));
    }

    private Path root() {
        return Path.of(properties.root()).toAbsolutePath().normalize();
    }

    private Path safeResolve(Path root, String objectKey) throws IOException {
        Path resolved = root.resolve(objectKey).normalize();
        if (!resolved.startsWith(root)) throw new IOException("Invalid object key");
        return resolved;
    }

    private MessageDigest sha256() {
        try { return MessageDigest.getInstance("SHA-256"); }
        catch (NoSuchAlgorithmException exception) { throw new IllegalStateException(exception); }
    }

    private void moveAtomically(Path source, Path target) throws IOException {
        try {
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException exception) {
            Files.move(source, target);
        }
    }
}
