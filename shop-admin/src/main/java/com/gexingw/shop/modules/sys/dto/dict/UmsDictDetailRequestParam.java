package com.gexingw.shop.modules.sys.dto.dict;

import com.gexingw.shop.bo.ums.UmsDict;
import lombok.Data;

@Data
public class UmsDictDetailRequestParam {
    private String label;

    private String value;

    private Long id;

    private int dictSort;

    private Long dictId;

    private UmsDict dict;
}
