package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.sys.SysUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CommonService {
    File upload(MultipartFile file, String uploadType);

    boolean detachOldFile(Long uploadId, String uploadType);
    SysUpload attachUploadFile(Long uploadId, String uploadType, File uploadedFile);
}
