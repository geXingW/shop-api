package com.gexingw.shop.service;

import com.gexingw.shop.bean.Upload;

public interface UploadService {
    Upload attachPicToSource(Long uploadId, String uploadType, String uploadPath);

    boolean detachSourcePic(Long uploadId, String uploadType);
}
