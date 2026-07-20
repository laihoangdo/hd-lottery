package com.hdplatform.modules.media.adapter.out.storage;

import com.hdplatform.modules.media.application.port.ObjectStorage;
import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocalObjectStorageTest {

    @TempDir Path tempDirectory;

    @Test
    void stores_content_in_tenant_and_asset_namespace_with_sha256() throws Exception {
        byte[] content = "safe image bytes".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        TenantId tenantId = TenantId.newId();
        MediaAssetId assetId = MediaAssetId.newId();
        LocalObjectStorage storage = storage();

        ObjectStorage.StoredObject stored = storage.store(new ObjectStorage.StorageUpload(
                tenantId, assetId, "image/png", content.length,
                new ByteArrayInputStream(content)));

        assertThat(stored.objectKey()).isEqualTo(
                tenantId.getValue() + "/" + assetId.getValue() + ".png");
        assertThat(Files.readAllBytes(tempDirectory.resolve(stored.objectKey())))
                .isEqualTo(content);
        assertThat(stored.checksumSha256()).isEqualTo(HexFormat.of().formatHex(
                MessageDigest.getInstance("SHA-256").digest(content)));
    }

    @Test
    void rejects_declared_size_mismatch_and_leaves_no_final_object() {
        byte[] content = "content".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        TenantId tenantId = TenantId.newId();
        MediaAssetId assetId = MediaAssetId.newId();

        assertThatThrownBy(() -> storage().store(new ObjectStorage.StorageUpload(
                tenantId, assetId, "image/jpeg", content.length + 1,
                new ByteArrayInputStream(content))))
                .isInstanceOf(IOException.class)
                .hasMessageContaining("declared size");

        assertThat(tempDirectory.resolve(tenantId.getValue().toString())
                .resolve(assetId.getValue() + ".jpg")).doesNotExist();
    }

    @Test
    void stops_stream_as_soon_as_actual_content_exceeds_declared_size() {
        byte[] content = new byte[32_000];

        assertThatThrownBy(() -> storage().store(new ObjectStorage.StorageUpload(
                TenantId.newId(), MediaAssetId.newId(), "image/png", 10,
                new ByteArrayInputStream(content))))
                .isInstanceOf(IOException.class)
                .hasMessage("Uploaded size exceeds declared size");
    }

    @Test
    void delete_rejects_path_traversal_object_key() {
        assertThatThrownBy(() -> storage().delete("../../outside.txt"))
                .isInstanceOf(IOException.class)
                .hasMessage("Invalid object key");
    }

    @Test
    void loads_only_object_keys_inside_configured_root() throws Exception {
        byte[] content = "content".getBytes(java.nio.charset.StandardCharsets.UTF_8);
        TenantId tenantId = TenantId.newId();
        MediaAssetId assetId = MediaAssetId.newId();
        LocalObjectStorage storage = storage();
        ObjectStorage.StoredObject stored = storage.store(new ObjectStorage.StorageUpload(
                tenantId, assetId, "image/png", content.length,
                new ByteArrayInputStream(content)));

        try (var loaded = storage.load(stored.objectKey()).content()) {
            assertThat(loaded.readAllBytes()).isEqualTo(content);
        }
        assertThatThrownBy(() -> storage.load("../secret.txt"))
                .isInstanceOf(IOException.class)
                .hasMessage("Invalid object key");
    }

    private LocalObjectStorage storage() {
        return new LocalObjectStorage(new MediaStorageProperties(
                tempDirectory.toString(), 10_485_760,
                Set.of("image/jpeg", "image/png")));
    }
}
