package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.sys.SysUpload;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface CommonService {
    File upload(MultipartFile file, String uploadType, String uploadModule);

    boolean detachOldFile(Long uploadId, String uploadModule);
    SysUpload attachUploadFile(Long uploadId, String uploadModule, File uploadedFile);
}
