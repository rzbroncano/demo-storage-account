package com.example.demostorageaccount.service;

import com.azure.core.util.BinaryData;
import com.azure.identity.ClientCertificateCredential;
import com.azure.identity.ClientCertificateCredentialBuilder;
import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.azure.storage.blob.*;
import com.example.demostorageaccount.controller.request.FileRequestMultiple;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class FileDynamicConnectionService {

    public String attachmentEncodedSimple(String connectionString, String containerName, String blobName) {

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        BlobClient blobClient = containerClient.getBlobClient(blobName);

        BinaryData result = blobClient.downloadContent();
        String imageEncoded = Base64.getEncoder().encodeToString(result.toBytes());

        return imageEncoded;
    }


    public List<String> attachmentEncodedMultiple(String connectionString, String containerName, List<String> blobNames) {


        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

        return blobNames.stream()
                .map(blobName -> containerClient.getBlobClient(blobName).downloadContent())
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }

    public List<String> attachmentEncodedMultiplev2(String connectionString, String containerName, List<String> blobNames) {

        return blobNames.stream()
                .map(blobName -> new BlobClientBuilder()
                        .connectionString(connectionString)
                        .containerName(containerName)
                        .blobName(blobName).buildClient().downloadContent())
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }

    public List<String> attachmentEncodedMultiplev3(String connectionString, String containerName, List<String> blobNames) {

        return blobNames.parallelStream()
                .map(blobName -> new BlobClientBuilder()
                        .connectionString(connectionString)
                        .containerName(containerName)
                        .blobName(blobName).buildClient().downloadContent())
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }

    public List<String> attachmentEncodedSimpleWithClientCredentialsBlobServiceClient(FileRequestMultiple fileRequestv2) {
        log.info("attachmentEncodedSimpleWithClientCredentialsBlobServiceClient");
        return fileRequestv2.getBlobNames().parallelStream()
                .map(blobName -> new BlobServiceClientBuilder()
                        .credential(getAzureClientCredentials(fileRequestv2.getClientId(), fileRequestv2.getClientSecret(), fileRequestv2.getTenantId()))
                        .endpoint("https://" + fileRequestv2.getStorageId() + ".blob.core.windows.net")
                        .buildClient().getBlobContainerClient(fileRequestv2.getStorageContainer())
                        .getBlobClient(blobName).downloadContent()
                )
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }

    public List<String> attachmentEncodedSimpleWithClientCredentialsBlobClientBuilder(FileRequestMultiple fileRequestv2) {
        log.info("attachmentEncodedSimpleWithClientCredentialsBlobClientBuilder");
        return fileRequestv2.getBlobNames().parallelStream()
                .map(blobName -> new BlobClientBuilder()
                        .credential(getAzureClientCredentials(fileRequestv2.getClientId(), fileRequestv2.getClientSecret(), fileRequestv2.getTenantId()))
                        .endpoint("https://" + fileRequestv2.getStorageId() + ".blob.core.windows.net/" + fileRequestv2.getStorageContainer() + "/" + blobName)
                        .buildClient().downloadContent()
                )
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }


    private ClientSecretCredential getAzureClientCredentials(String clientId, String clientSecret, String tenantId) {
        return new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenantId)
                .build();
    }

    public List<String> attachmentEncodedSimpleWithClientCertificateCredentialBlobClientBuilder(FileRequestMultiple fileRequestv2) {
        log.info("attachmentEncodedSimpleWithClientCredentialsBlobClientBuilder");
        return fileRequestv2.getBlobNames().parallelStream()
                .map(blobName -> new BlobClientBuilder()
                        .credential(getAzureCertificateCredential(fileRequestv2.getClientId(), fileRequestv2.getTenantId()))
                        .endpoint("https://" + fileRequestv2.getStorageId() + ".blob.core.windows.net/" + fileRequestv2.getStorageContainer() + "/" + blobName)
                        .buildClient().downloadContent()
                )
                .map(content -> Base64.getEncoder().encodeToString(content.toBytes()))
                .collect(Collectors.toList());
    }

    private ClientCertificateCredential getAzureCertificateCredential(String clientId,String tenantId) {
        return new ClientCertificateCredentialBuilder()
                .pemCertificate("C:\\Users\\rzbro\\Desktop\\private_key_cert.pem")
                .clientId(clientId)
                .tenantId(tenantId)
                .build();
    }

    public String getStorageEndpoint(String storageEndpoint, String storageId) {
        return storageEndpoint.replace("{STORAGE-ID}", storageId);
    }

}
