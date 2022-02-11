package com.gexingw.shop.modules.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.mapper.sys.SysUploadMapper;
import com.gexingw.shop.modules.sys.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    SysUploadMapper sysUploadMapper;

    @Override
    public SysUpload attachPicToSource(Long uploadId, String uploadModule, String uploadPath) {
        // 根据path + uploadType，获取唯一的Upload记录
        uploadPath = uploadPath.substring(uploadPath.indexOf(uploadModule));

        QueryWrapper<SysUpload> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upload_module", uploadModule).eq("path", uploadPath);

        SysUpload upload = sysUploadMapper.selectOne(queryWrapper);
        if (upload == null) {
            return null;
        }

        upload.setUploadId(uploadId);   // 更新upload记录的upload_id
        if (sysUploadMapper.updateById(upload) <= 0) {
            return null;
        }

        return upload;
    }

    @Override
    public boolean detachSourcePic(Long uploadId, String uploadModule) {
        QueryWrapper<SysUpload> queryWrapper = new QueryWrapper<SysUpload>().eq("upload_id", uploadId).eq("upload_module", uploadModule);
        return sysUploadMapper.delete(queryWrapper) >= 0;
    }
}
