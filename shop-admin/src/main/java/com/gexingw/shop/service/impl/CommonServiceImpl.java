package com.gexingw.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.mapper.UploadMapper;
import com.gexingw.shop.service.CommonService;
import com.gexingw.shop.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    FileConfig fileConfig;

    @Autowired
    UploadMapper uploadMapper;

    @Override
    public String upload(MultipartFile file) {
        String uploadPath = fileConfig.getLocation();
        File uploadResult = FileUtil.upload(file, uploadPath);

        return uploadResult.getPath();
    }

    @Override
    public boolean detachOldFile(Long uploadId, String uploadType) {
        // 用户头像上传
        if (UploadConstant.UPLOAD_TYPE_ADMIN_AVATAR.equals(uploadType)) {
            detachAdminAvatarFile(uploadId, uploadType);
        }

        return true;
    }

    public boolean detachAdminAvatarFile(Long uploadId, String uploadType) {
        QueryWrapper<Upload> queryWrapper = new QueryWrapper<Upload>().eq("upload_id", uploadId).eq("upload_type", uploadType);
        Upload upload = uploadMapper.selectOne(queryWrapper);
        if (upload != null) {
            return true;
        }

        // 删除相关文件 TODO

        return true;
    }
}
