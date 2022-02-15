package com.gexingw.shop.modules.pms.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.bo.pms.PmsProductSku;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.enums.PmsProductOnSaleEnum;
import com.gexingw.shop.enums.RespCode;
import com.gexingw.shop.modules.pms.dto.product.PmsProductPriceRequestParam;
import com.gexingw.shop.modules.pms.dto.product.PmsProductSearchRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.modules.pms.vo.PmsProductSkuVO;
import com.gexingw.shop.modules.pms.vo.PmsProductVO;
import com.gexingw.shop.utils.PageUtil;
import com.gexingw.shop.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product")
public class PmsProductController {

    @Autowired
    PmsProductService pmsProductService;

    @Autowired
    PmsProductSkuService pmsProductSkuService;

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
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/{id}")
    public R info(@PathVariable String id) {
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

    @PostMapping("/{id}/price")
    public R price(@PathVariable String id, @RequestBody PmsProductPriceRequestParam requestParam) {
        // 检查商品信息是否存在
        if (pmsProductService.getById(id) == null) {
            return R.ok(RespCode.RESOURCE_NOT_EXIST.getCode(), "商品不存在！");
        }

        // 获取商品价格和库存
        List<PmsProductPriceRequestParam.Option> spData = requestParam.getOptions().stream().sorted(Comparator.comparing(PmsProductPriceRequestParam.Option::getId))
                .collect(Collectors.toList());
        PmsProductSku productSku = pmsProductSkuService.getByIdAndSkuData(id, spData);

        // 查询不到该Sku信息，直接返回默认值
        if (productSku == null) {
            return R.ok(new PmsProductSkuVO());
        }

        return R.ok(new PmsProductSkuVO(productSku));
    }

}
