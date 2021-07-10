package com.gexingw.shop.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("oauth_account")
public class OAuthAccount implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String clientId;

    private String username;

    private String password;

    private String mobile;

    private String email;

    private Integer enabled;

    private Integer accountNonExpired;

    private Integer credentialsNonExpired;

    private Integer accountNonLocked;

    private Integer accountNonDeleted;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedTime;

}
