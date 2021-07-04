package com.gexingw.shop.bean.ums;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gexingw.shop.bean.Upload;
import com.gexingw.shop.constant.UploadConstant;
import com.gexingw.shop.mapper.UploadMapper;
import com.gexingw.shop.utils.SpringContextUtil;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@TableName("ums_admins")
public class UmsAdmin {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long deptId;

    private String password;

    private String username;

    private String nickName;

    private String gender;

    private String phone;

    private String email;


    private String avatarName;

    private String avatarPath;

    private boolean isAdmin;

    @Getter(value = AccessLevel.NONE)
    private int enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private Collection<Object> authorities;

    @TableField(exist = false)
    private UmsDept dept;

    @TableField(exist = false)
    private List<UmsJob> jobs;

    @TableField(exist = false)
    private List<UmsRole> roles;

    public boolean isEnabled() {
        return this.enabled == 1;
    }

    public String getAvatar() {
        QueryWrapper<Upload> queryWrapper = new QueryWrapper<Upload>().eq("upload_id", id)
                .eq("upload_type", UploadConstant.UPLOAD_TYPE_ADMIN_AVATAR);
        UploadMapper mapper = SpringContextUtil.getBean(UploadMapper.class);
        Upload upload = mapper.selectOne(queryWrapper);
        if(upload == null){
           return null;
        }

        return upload.getFullUrl();
    }

    @Override
    public String toString() {
        return "UmsAdmin{" +
                "id=" + id +
                ", deptId=" + deptId +
                ", username='" + username + '\'' +
                ", nickName='" + nickName + '\'' +
                ", gender=" + gender +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", avatarName='" + avatarName + '\'' +
                ", avatarPath='" + avatarPath + '\'' +
                ", isAdmin=" + isAdmin +
                ", enabled=" + enabled +
                ", createTime=" + createTime +
                ", jobs=" + jobs +
                ", roles=" + roles +
                '}';
    }
}
