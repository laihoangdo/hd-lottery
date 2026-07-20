package com.hdplatform.modules.media.application.port;

import com.hdplatform.modules.media.domain.aggregate.MediaAssetId;
import com.hdplatform.modules.tenant.domain.aggregate.TenantId;

import java.io.IOException;
import java.io.InputStream;

public interface ObjectStorage {
    StoredObject store(StorageUpload upload) throws IOException;
    StoredContent load(String objectKey) throws IOException;
    void delete(String objectKey) throws IOException;

    record StorageUpload(TenantId tenantId, MediaAssetId assetId, String contentType,
                         long declaredSize, InputStream content) {
    }

    record StoredObject(String objectKey, long sizeBytes, String checksumSha256) {
    }

    record StoredContent(InputStream content, long sizeBytes) {
    }
}
