package com.example.demostorageaccount.controller.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Data
public class FileRequest {

    private String connectionString;
    private String containerName;
    private List<String> blobNames;
}
