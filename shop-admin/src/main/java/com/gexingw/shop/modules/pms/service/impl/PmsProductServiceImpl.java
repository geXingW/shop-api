package com.gexingw.shop.modules.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gexingw.shop.bo.pms.PmsProduct;
import com.gexingw.shop.config.FileConfig;
import com.gexingw.shop.constant.ProductConstant;
import com.gexingw.shop.es.ESProduct;
import com.gexingw.shop.exception.DBOperationException;
import com.gexingw.shop.mapper.pms.PmsProductMapper;
import com.gexingw.shop.modules.pms.dto.product.PmsProductRequestParam;
import com.gexingw.shop.modules.pms.service.PmsProductAttributeValueService;
import com.gexingw.shop.modules.pms.service.PmsProductService;
import com.gexingw.shop.modules.pms.service.PmsProductSkuService;
import com.gexingw.shop.utils.FileUtil;
import com.gexingw.shop.utils.RedisUtil;
import com.gexingw.shop.utils.StringUtil;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

@Service
public class PmsProductServiceImpl implements PmsProductService {
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    PmsProductMapper productMapper;

    @Autowired
    PmsProductAttributeValueService attributeValueService;

    @Autowired
    PmsProductSkuService productSkuService;

    @Autowired
    FileConfig fileConfig;

    @Autowired
    RestHighLevelClient client;

    @Override
    public IPage<PmsProduct> search(QueryWrapper<PmsProduct> queryWrapper, IPage<PmsProduct> page) {
        return productMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(PmsProductRequestParam requestParam) {
        PmsProduct product = new PmsProduct();

        BeanUtils.copyProperties(requestParam, product);

        // ???????????????????????????
        this.removeProductImageDomain(product, requestParam);

        // ??????SKU??????
        product.setSkuOptions(JSON.toJSONString(requestParam.getSkuOptions()));

        if (productMapper.insert(product) <= 0) {
            return null;
        }

        String productId = product.getId();   // ????????????ID
        if (!attributeValueService.save(productId, requestParam.getAttributeList())) {
            throw new DBOperationException("??????????????????????????????!");
        }

        // ????????????Sku??????
        if (!productSkuService.save(productId, requestParam.getSkuList())) {
            throw new DBOperationException("??????Sku?????????????????????");
        }

        return productId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(String productId, PmsProductRequestParam requestParam) {
        PmsProduct product = productMapper.selectById(productId);
        if (product == null) {
            return false;
        }

        BeanUtils.copyProperties(requestParam, product);

        // ???????????????????????????
        this.removeProductImageDomain(product, requestParam);

        // ??????SKU??????
        product.setSkuOptions(JSON.toJSONString(requestParam.getSkuOptions()));

        if (productMapper.updateById(product) <= 0) {
            throw new DBOperationException("???????????????????????????");
        }

        // ??????????????????????????????
        if (!attributeValueService.delProductAttributesByPid(productId)) {
            throw new DBOperationException("????????????????????????????????????");
        }

        // ??????????????????????????????
        if (!attributeValueService.save(productId, requestParam.getAttributeList())) {
            throw new DBOperationException("?????????????????????????????????");
        }

        // ????????????Sku??????
        if (!productSkuService.delProductAttributesByPid(productId)) {
            throw new DBOperationException("?????????Sku?????????????????????");
        }

        // ????????????Sku??????
        if (!productSkuService.save(productId, requestParam.getSkuList())) {
            throw new DBOperationException("??????Sku?????????????????????");
        }

        return true;
    }

    @Override
    public boolean update(PmsProduct product) {
        return productMapper.updateById(product) >= 0;
    }

    @Override
    public PmsProduct getById(String id) {
        // ?????????Redis??????
        PmsProduct product = this.getRedisProductByProductId(id);
        if (product != null) {
            return product;
        }

        // ???DB??????
        product = productMapper.selectById(id);

        // ????????????redis
        if (product != null) {
            this.setRedisProductByProductId(id, product);
        }

        return product;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(Set<String> ids) {
        // ?????????????????????????????????
        if (!attributeValueService.batchDelProductAttributesByProductIds(ids)) {
            throw new DBOperationException("???????????????????????????");
        }

        // ????????????Sku??????
        if (!productSkuService.batchDelProductSkuByProductIds(ids)) {
            throw new DBOperationException("??????sku?????????????????????");
        }

        if (productMapper.deleteBatchIds(ids) <= 0) {
            throw new DBOperationException("???????????????????????????");
        }

        return true;
    }

    @Override
    public PmsProduct getRedisProductByProductId(String productId) {
        Object redisObj = redisUtil.get(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
        if (redisObj == null) {
            return null;
        }

        return JSON.parseObject(redisObj.toString(), PmsProduct.class);
    }

    @Override
    public boolean setRedisProductByProductId(String productId, PmsProduct product) {
        return redisUtil.set(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId), product);
    }

    @Override
    public void delRedisProductByProductId(String productId) {
        redisUtil.del(String.format(ProductConstant.REDIS_PRODUCT_FORMAT, productId));
    }

    @Override
    public boolean addProductToES(PmsProduct product) throws IOException {
        String[] albums = product.getAlbumPics().split(",");
        ArrayList<String> albumsWithFullUrl = new ArrayList<>(albums.length);
        for (String album : albums) {
            // ???????????????????????????url
            albumsWithFullUrl.add(FileUtil.buildFileFullUrl(fileConfig, album));
        }

        // ??????ES Product??????
        ESProduct esProduct = new ESProduct();
        esProduct.setId(product.getId());
        esProduct.setTitle(product.getTitle());
        esProduct.setSalePrice(product.getSalePrice());
        esProduct.setPic(FileUtil.buildFileFullUrl(fileConfig, product.getPic()));
        esProduct.setAlbums(albumsWithFullUrl);

        // ??????Sku?????????
        if (!"".equals(product.getSkuOptions())) {
            esProduct.setSkuOptions(JSON.parseObject(product.getSkuOptions(), new TypeReference<List<ESProduct.ESProductSku>>() {
            }));
        }

        // ??????ES??????
        IndexRequest indexRequest = new IndexRequest("product").id(product.getId())
                .source(JSON.toJSONString(esProduct), XContentType.JSON);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getResult() == DocWriteResponse.Result.CREATED;
    }

    @Override
    public boolean delProductFromESById(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("product").id(id);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return deleteResponse.getResult() == DocWriteResponse.Result.DELETED;
    }

    @Override
    public List<PmsProduct> getProductsByIds(Set<String> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }

        return productMapper.selectList(new QueryWrapper<PmsProduct>().in("id", ids));
    }

    private void removeProductImageDomain(PmsProduct product, PmsProductRequestParam requestParam) {
        // ????????????
        List<String> picsWithoutDomain = requestParam.getPicsWithoutDomain(fileConfig);
        product.setAlbumPics(String.join(",", picsWithoutDomain));   // ??????
        product.setPic(picsWithoutDomain.get(0)); //??????

        // ????????????
        String separator = Matcher.quoteReplacement(File.separator);
        String fileDomain = StringUtil.trim(fileConfig.getDiskHost(), separator) + separator;
        product.setDetailPCHtml(requestParam.getDetailPCHtml().replaceAll("src=\"" + fileDomain, "src=\""));
        product.setDetailMobileHtml(requestParam.getDetailMobileHtml().replaceAll("src=\"" + fileDomain, "src=\""));
    }
}
