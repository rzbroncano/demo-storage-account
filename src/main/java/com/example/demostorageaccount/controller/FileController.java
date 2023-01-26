package com.example.demostorageaccount.controller;

import com.example.demostorageaccount.controller.request.FileRequest;
import com.example.demostorageaccount.controller.request.FileRequestMultiple;
import com.example.demostorageaccount.service.FileDynamicConnectionService;
import com.example.demostorageaccount.service.FileUniqueConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileUniqueConnectionService fileService;

    private final FileDynamicConnectionService fileDynamicConnectionService;

    @Autowired
    public FileController(FileUniqueConnectionService fileService, FileDynamicConnectionService fileMultipleConnectionService) {
        this.fileService = fileService;
        this.fileDynamicConnectionService = fileMultipleConnectionService;
    }

    @PostMapping(value = "/", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<?> uploadAndDownload(@RequestParam("file") MultipartFile file) {
        try {
            if (fileService.uploadAndDownloadFile(file, "files")) {
                final ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(Paths.get(fileService
                        .getFileStorageLocation() + "/" + file.getOriginalFilename())));
                return ResponseEntity.status(HttpStatus.OK).contentLength(resource.contentLength()).body(resource);
            }
            return ResponseEntity.ok("Error while processing file");
        } catch (Exception e) {
            return ResponseEntity.ok("Error while processing file");
        }
    }

    @GetMapping(value = "/dynamic")
    public ResponseEntity<?> downloadDynamic(@RequestParam("pathAttachment") String pathAttachment, @RequestParam("containerName") String containerName, @RequestParam("blobName") String blobName) {
        // Create a BlobServiceClient object which will be used to create a container client

        try {
            String result = fileDynamicConnectionService.getAttachmentEncoded(pathAttachment, containerName, blobName);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            return ResponseEntity.ok(e);
        }
    }

    @PostMapping(value = "/dynamicBatch")
    public ResponseEntity<?> downloadDynamicBatch(@RequestBody FileRequest fileRequest) {
        List<String> result = fileDynamicConnectionService.getAttachmentsUsingBlobServiceClient(fileRequest.getConnectionString(), fileRequest.getContainerName(), fileRequest.getBlobNames());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PostMapping(value = "/dynamicBatchV2")
    public ResponseEntity<?> downloadDynamicBatchV2(@RequestBody FileRequest fileRequest) {
        List<String> result = fileDynamicConnectionService.getAttachmentsUsingBlobClientBuilder(fileRequest.getConnectionString(), fileRequest.getContainerName(), fileRequest.getBlobNames());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PostMapping(value = "/dynamicBatchV3")
    public ResponseEntity<?> downloadDynamicBatchV3(@RequestBody FileRequest fileRequest) {
        List<String> result = fileDynamicConnectionService.getAttachmentsUsingBlobClientBuilderWithParallelStream(fileRequest.getConnectionString(), fileRequest.getContainerName(), fileRequest.getBlobNames());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @PostMapping(value = "/dynamicBatchV4")
    public ResponseEntity<?> downloadDynamicBatchV4(@RequestBody FileRequestMultiple fileRequestv2) {
        List<String> result = fileDynamicConnectionService.getAttachmentUsingServicePrincipalAndBlobServiceClientBuilder(fileRequestv2);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PostMapping(value = "/dynamicBatchV5")
    public ResponseEntity<?> downloadDynamicBatchV5(@RequestBody FileRequestMultiple fileRequestv2) {
        List<String> result = fileDynamicConnectionService.getAttachmentUsingServicePrincipalAndBlobClientBuilder(fileRequestv2);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PostMapping(value = "/dynamicBatchV6")
    public ResponseEntity<?> downloadDynamicBatchV6(@RequestBody FileRequestMultiple fileRequestv2) {
        List<String> result = fileDynamicConnectionService.attachmentEncodedSimpleWithClientCertificateCredentialBlobClientBuilder(fileRequestv2);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}