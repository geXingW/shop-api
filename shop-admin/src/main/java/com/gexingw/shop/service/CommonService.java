package com.gexingw.shop.service;

import com.gexingw.shop.bo.Upload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CommonService {
    File upload(MultipartFile file, String uploadType);

    boolean detachOldFile(Long uploadId, String uploadType);
    Upload attachUploadFile(Long uploadId, String uploadType, File uploadedFile);
}
