package com.gexingw.shop.modules.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.mapper.sys.SysUploadMapper;
import com.gexingw.shop.modules.sys.service.CommonService;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.StringUtil;
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
    SysUploadMapper sysUploadMapper;

    @Override
    public File upload(MultipartFile file, String uploadType, String uploadModule) {
        String separator = File.separator;

        // 上传配置目录
        String uploadPath = StringUtil.trim(fileConfig.getLocation(), separator) + separator;

        // 拼接上传路径
        StringBuilder uploadPathBuilder = new StringBuilder(uploadPath);

        // 拼接文件类型，例如：image
        uploadPathBuilder.append(StringUtil.trim(uploadType, separator));

        // 拼接分割符
        uploadPathBuilder.append(separator);

        // 拼接文件模块
        uploadPathBuilder.append(StringUtil.trim(uploadModule, separator));

        return FileUtil.upload(file, uploadPathBuilder.toString());
    }

    @Override
    public boolean detachOldFile(Long uploadId, String uploadModule) {
        // 删除数据库记录
        QueryWrapper<SysUpload> queryWrapper = new QueryWrapper<SysUpload>().eq("upload_id", uploadId).eq("upload_module", uploadModule);

        // 已绑定文件记录
        List<SysUpload> uploads = sysUploadMapper.selectList(queryWrapper);
        if (uploads.size() <= 0) { // 找不老的上传文件保存记录，直接返回
            return true;
        }

        // 删除数据库记录
        if (sysUploadMapper.deleteBatchIds(uploads.stream().map(SysUpload::getId).collect(Collectors.toList())) <= 0) {
            return true;
        }

        // 检查文件是否存在
        String filePath;
        for (SysUpload upload : uploads) {
            filePath = upload.getFullPath(); // 文件全路径
            if (FileUtil.exist(filePath)) {
                FileUtil.del(filePath);     // 删除文件
            }
        }

        return true;
    }

    @Override
    public SysUpload attachUploadFile(Long uploadId, String uploadModule, File uploadedFile) {
        String disk = fileConfig.getActiveDisk();
        String path = StrUtil.removePrefix(uploadedFile.getPath(), fileConfig.getLocation(disk));

        SysUpload upload = new SysUpload();
        upload.setName(uploadedFile.getName());
        upload.setDisk(disk);
        upload.setPath(StrUtil.removePrefix(path, File.separator));
        upload.setSize(uploadedFile.length());
        upload.setUploadId(uploadId);
        upload.setUploadModule(uploadModule);

        if (sysUploadMapper.insert(upload) <= 0) {
            return null;
        }

        return upload;
    }

    public boolean detachAdminAvatarFile(Long uploadId, String uploadType) {
        QueryWrapper<SysUpload> queryWrapper = new QueryWrapper<SysUpload>().eq("upload_id", uploadId).eq("upload_module", uploadType);
        SysUpload upload = sysUploadMapper.selectOne(queryWrapper);
        if (upload != null || upload.getPath() == null) {
            return true;
        }

        // 删除相关文件 TODO
        return FileUtil.del(upload.getPath());
    }

}
