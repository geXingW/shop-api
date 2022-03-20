package com.gexingw.shop.modules.oms.dto;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;

@Data
public class OmsCartSearchParam extends BaseSearchParam {
    private Boolean checked;

    public Integer getChecked() {
        if (checked == null) {
            return null;
        }

        return checked ? 1 : 0;
    }
}
