package com.gexingw.shop.service.impl;

import cn.hutool.core.util.StrUtil;
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
    public File upload(MultipartFile file, String uploadType) {

        // 拼接上传路径
        String uploadPath = StrUtil.removeSuffix(fileConfig.getLocation(), File.separator) + File.separator;
        uploadPath += StrUtil.removePrefix(uploadType, File.separator);

        return FileUtil.upload(file, uploadPath);
    }

    @Override
    public boolean detachOldFile(Long uploadId, String uploadType) {
        // 用户头像上传
        if (UploadConstant.UPLOAD_TYPE_ADMIN_AVATAR.equals(uploadType)) {
            detachAdminAvatarFile(uploadId, uploadType);
        }

        // 删除数据库记录
        QueryWrapper<Upload> queryWrapper = new QueryWrapper<Upload>().eq("upload_id", uploadId).eq("upload_type", uploadType);
        return uploadMapper.delete(queryWrapper) > 0;
    }

    @Override
    public String attachNewFile(Long uploadId, String uploadType, File uploadedFile) {

        Upload upload = new Upload();

        upload.setName(uploadedFile.getName());
        upload.setDisk(fileConfig.getActiveDisk());
        upload.setPath(uploadedFile.getPath());
        upload.setSize(uploadedFile.length());
        upload.setUploadId(uploadId);
        upload.setUploadType(uploadType);

        if (uploadMapper.insert(upload) <= 0) {
            return null;
        }

        return upload.getPath();
    }

    public boolean detachAdminAvatarFile(Long uploadId, String uploadType) {
        QueryWrapper<Upload> queryWrapper = new QueryWrapper<Upload>().eq("upload_id", uploadId).eq("upload_type", uploadType);
        Upload upload = uploadMapper.selectOne(queryWrapper);
        if (upload != null || upload.getPath() == null) {
            return true;
        }

        // 删除相关文件 TODO
        return FileUtil.del(upload.getPath());
    }
}
