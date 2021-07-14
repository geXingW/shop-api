package com.gexingw.shop.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AuthInfo {
    private Long id;

    private Long memberLevelId;

    private String username;

    private String nickname;

    private String status;

}
