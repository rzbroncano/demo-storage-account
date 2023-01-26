package com.example.demostorageaccount.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

public class AzureBlobStorageConfig {

    private final static String ENDPOINT_URL = "https://%s.blob.core.windows.net";

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Bean
    public BlobServiceClient getBlobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(String.format(Locale.ROOT, ENDPOINT_URL, accountName))
                .credential(new StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }
}
