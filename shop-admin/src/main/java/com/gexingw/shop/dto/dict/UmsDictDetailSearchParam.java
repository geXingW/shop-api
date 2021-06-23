package com.gexingw.shop.dto.dict;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class UmsDictDetailSearchParam extends BaseSearchParam {
    private String dictName;
}