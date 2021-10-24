package com.gexingw.shop.modules.sys.service;

import com.gexingw.shop.bo.sys.SysUpload;

public interface UploadService {
    SysUpload attachPicToSource(Long uploadId, String uploadType, String uploadPath);

    boolean detachSourcePic(Long uploadId, String uploadType);
}
