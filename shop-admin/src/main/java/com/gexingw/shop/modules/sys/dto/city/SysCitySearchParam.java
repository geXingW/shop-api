package com.gexingw.shop.modules.sys.dto.city;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SysCitySearchParam extends BaseSearchParam {
    private Long id;

    private String blurry;

    private Integer code;

    private Integer parentCode;
}
