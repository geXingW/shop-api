package com.gexingw.shop.modules.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.PmsProductOnSaleEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.vo.PmsProductVO;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product")
public class PmsProductController {

    @Autowired
    PmsProductService pmsProductService;

    @Autowired
    FileConfig fileConfig;

    @GetMapping("/search")
    public R search(PmsProductSearchRequestParam requestParam) {
        IPage<PmsProduct> searchResultPage = pmsProductService.search(requestParam);

        List<PmsProductVO> VORecords = searchResultPage.getRecords().stream().map(
                product -> new PmsProductVO(product).setProductPics(product, fileConfig)
        ).collect(Collectors.toList());

        Map<String, Object> result = PageUtil.format(searchResultPage);
        result.put("records", VORecords);

        return R.ok(result);
    }

    /**
     * 商品详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    R info(@PathVariable String id) {
        PmsProduct pmsProduct = pmsProductService.getById(id);
        if (pmsProduct == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品不存在！");
        }

        // 检查商品是否上架
        Integer isOnSale = Integer.valueOf(pmsProduct.getOnSale());
        if (!PmsProductOnSaleEnum.ON_SALE.getCode().equals(isOnSale)) {
            return R.ok(RespCode.RESOURCE_UNAVAILABLE.getCode(), "商品暂未上架！");
        }

        return R.ok(new PmsProductVO(pmsProduct).setProductPics(pmsProduct, fileConfig));
    }


}
