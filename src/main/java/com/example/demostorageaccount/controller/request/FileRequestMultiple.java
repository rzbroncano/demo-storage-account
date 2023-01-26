package com.example.demostorageaccount.controller.request;

import lombok.Data;

import java.util.List;

@Data
public class FileRequestMultiple {

    private String clientId;
    private  String clientSecret;
    private  String tenantId;
    private  String storageId;
    private  String storageContainer;
    private  List<String> blobNames;
}
