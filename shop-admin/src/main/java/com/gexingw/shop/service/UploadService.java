package com.gexingw.shop.service;

import com.gexingw.shop.bo.sys.SysUpload;

public interface UploadService {
    SysUpload attachPicToSource(Long uploadId, String uploadType, String uploadPath);

    boolean detachSourcePic(Long uploadId, String uploadType);
}
