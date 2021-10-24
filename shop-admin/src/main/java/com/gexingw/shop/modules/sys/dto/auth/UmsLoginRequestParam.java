package com.gexingw.shop.modules.sys.dto.auth;

import lombok.Data;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class UmsLoginRequestParam {
    @NotEmpty
    @Size(min = 5, max = 64)
    private String username;

    @NotEmpty
    @Size(min = 5)
    private String password;

    @NotEmpty
    private String uuid;

    @NotEmpty
    private String code;
}
