package org.codewithanish.service;

import org.codewithanish.util.ConfigProperties;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3Service implements AutoCloseable{

    private final S3Client s3Client;
    private final String bucket;
    private final String keyPrefix;

    public S3Service()
    {
         s3Client = S3Client.builder()
                .region(Region.AP_SOUTH_1)
                .build();
        bucket = ConfigProperties.getInstance().getProperty("s3.bucket.name");
        keyPrefix = ConfigProperties.getInstance().getProperty("s3.key.prefix");
    }

    public String uploadContent(String fileName, byte[] content) {
            String key = keyPrefix + fileName;
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
            return getUrl(fileName);
    }

    public String getUrl(String fileName) {
        String key = keyPrefix + fileName;
        GetUrlRequest getUrlRequest = GetUrlRequest.builder().bucket(bucket).key(key).build();
        return s3Client.utilities().getUrl(getUrlRequest).toString();
    }

    @Override
    public void close() throws Exception {
        s3Client.close();
    }
}
