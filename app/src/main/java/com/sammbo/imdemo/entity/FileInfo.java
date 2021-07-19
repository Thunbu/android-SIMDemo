package com.sammbo.imdemo.entity;

import java.io.File;

import lombok.Builder;
import lombok.Data;

/**
 * time   : 2021/01/27
 * desc   :
 * version:
 */
@Data
@Builder
public class FileInfo {
    private String filename;
    private String downUrl;
    private long size;
}
