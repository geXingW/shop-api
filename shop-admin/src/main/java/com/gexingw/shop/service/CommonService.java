package com.gexingw.shop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CommonService {
    File upload(MultipartFile file, String uploadType);

    boolean detachOldFile(Long uploadId, String uploadType);

    String attachNewFile(Long uploadId, String uploadType, File uploadedFile);

}
