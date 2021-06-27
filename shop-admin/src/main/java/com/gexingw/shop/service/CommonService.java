package com.gexingw.shop.service;

import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    String upload(MultipartFile file);

    boolean detachOldFile(Long uploadId, String uploadType);
}
