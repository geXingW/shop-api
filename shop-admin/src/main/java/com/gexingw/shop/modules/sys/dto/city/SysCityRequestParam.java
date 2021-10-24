package com.gexingw.shop.modules.sys.dto.city;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SysCityRequestParam {
    private Integer id;

    private String name;

    private Integer code;

    private Integer parentCode;
}
