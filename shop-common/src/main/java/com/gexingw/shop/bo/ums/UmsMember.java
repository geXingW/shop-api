package com.gexingw.shop.bo.ums;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class UmsMember {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long memberLevelId;

    private String username;

    private String password;

    private String nickname;

    private String status;

}
