package com.gexingw.shop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.Upload;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.mapper.UploadMapper;
import com.gexingw.shop.service.CommonService;
import com.gexingw.shop.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

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
        // 删除数据库记录
        QueryWrapper<Upload> queryWrapper = new QueryWrapper<Upload>().eq("upload_id", uploadId).eq("upload_type", uploadType);

        // 已绑定文件记录
        List<Upload> uploads = uploadMapper.selectList(queryWrapper);
        if (uploads.size() <= 0) { // 找不老的上传文件保存记录，直接返回
            return true;
        }

        // 删除数据库记录
        if (uploadMapper.deleteBatchIds(uploads.stream().map(Upload::getId).collect(Collectors.toList())) <= 0) {
            return true;
        }

        // 检查文件是否存在
        String filePath;
        for (Upload upload : uploads) {
            filePath = upload.getFullPath(); // 文件全路径
            if (FileUtil.exist(filePath)) {
                FileUtil.del(filePath);     // 删除文件
            }
        }

        return true;
    }

    @Override
    public Upload attachUploadFile(Long uploadId, String uploadType, File uploadedFile) {
        String disk = fileConfig.getActiveDisk();
        String path = StrUtil.removePrefix(uploadedFile.getPath(), fileConfig.getLocation(disk));

        Upload upload = new Upload();
        upload.setName(uploadedFile.getName());
        upload.setDisk(disk);
        upload.setPath(StrUtil.removePrefix(path, File.separator));
        upload.setSize(uploadedFile.length());
        upload.setUploadId(uploadId);
        upload.setUploadType(uploadType);

        if (uploadMapper.insert(upload) <= 0) {
            return null;
        }

        return upload;
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
