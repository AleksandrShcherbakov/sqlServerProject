package com.work.sqlServerProject.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by a.shcherbakov on 04.07.2019.
 */
public class PathScanFile {
    private String url;
    private String toBts;
    private MultipartFile[] scanFile;
    private MultipartFile btsFile;

    public MultipartFile[] getScanFile() {
        return scanFile;
    }

    public void setScanFile(MultipartFile[] scanFile) {
        this.scanFile = scanFile;
    }

    public MultipartFile getBtsFile() {
        return btsFile;
    }

    public void setBtsFile(MultipartFile btsFile) {
        this.btsFile = btsFile;
    }

    public String getToBts() {
        return toBts;
    }

    public void setToBts(String toBts) {
        this.toBts = toBts;
    }

    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }
}
