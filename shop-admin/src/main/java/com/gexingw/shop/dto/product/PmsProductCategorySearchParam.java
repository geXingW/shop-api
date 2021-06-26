package com.gexingw.shop.dto.product;

import com.gexingw.shop.dto.BaseSearchParam;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
public class PmsProductCategorySearchParam extends BaseSearchParam {
    private Long pid;

}
