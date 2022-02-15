package com.gexingw.shop.modules.sys.controller;

import com.gexingw.shop.bo.sys.SysUpload;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.sys.service.CommonService;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;

@RestController
@RequestMapping("common")
public class CommonController {

    @Autowired
    CommonService commonService;

    /**
     * 上传接口
     *
     */
    @PostMapping("upload")
    public R upload(@RequestParam MultipartFile file,
                    @RequestParam String uploadType,
                    @RequestParam Long uploadId,
                    @RequestParam String uploadModule) {
        // 上传文件获取服务器文件路径
        File uploadedFile = commonService.upload(file, uploadType, uploadModule);

        // 资源与新图片绑定
        SysUpload upload = commonService.attachUploadFile(uploadId, uploadModule, uploadedFile);
        if (upload == null) {
            return R.ok(RespCode.SAVE_FAILURE.getCode(), "新图片绑定失败！");
        }

        HashMap<String, Object> result = new HashMap<>();
        result.put("url", upload.getFullUrl());
        result.put("path", upload.getPath());

        return R.ok(result, "上传成功！");
    }

}
