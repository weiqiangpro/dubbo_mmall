package com.wq.mmall.service;


import com.wq.mmall.dao.ProductDao;
import com.wq.mmall.entity.Category;
import com.wq.mmall.entity.Product;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.utils.Const;
import com.wq.mmall.vo.ProductDetailResponse;
import com.wq.mmall.vo.ProductListResponse;
import com.wq.mmall.vo.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by weiqiang
 */
@Service
@Transactional(rollbackFor = {RpcException.class,MmallException.class})
public class ProductServiceImpl implements IProductService {
    @Reference(loadbalance = "random", timeout = 1000) //dubbo直连
    private ICategoryService categoryService;

    @Autowired
    private  ProductDao productDao;

    private ProductDetailResponse assembleProductDetailVo(Product product) throws MmallException {
        ProductDetailResponse productDetailVo = new ProductDetailResponse().setId(product.getId())
                .setSubtitle(product.getSubtitle())
                .setPrice(product.getPrice())
                .setMainImage(product.getMainImage())
                .setSubImages(product.getSubImages())
                .setCategoryId(product.getCategoryId())
                .setDetail(product.getDetail())
                .setName(product.getName())
                .setStatus(product.getStatus())
                .setStock(product.getStock());
        productDetailVo.setCreateTime(product.getCreateTime());
        productDetailVo.setUpdateTime(product.getUpdateTime());
        return productDetailVo;
    }


    private ProductListResponse assembleProductListVo(Product product) {
        return new ProductListResponse()
                .setId(product.getId())
                .setName(product.getName())
                .setCategoryId(product.getCategoryId())
                .setMainImage(product.getMainImage())
                .setPrice(product.getPrice())
                .setSubtitle(product.getSubtitle())
                .setStatus(product.getStatus());
    }


    public ProductDetailResponse getProductDetail(Integer productId) throws MmallException {
        if (productId == null)
            throw new MmallException(ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        Optional<Product> product = productDao.findById(productId);

        if (!product.isPresent() || product.get().getStatus() != Const.ProductStatusEnum.ON_SALE.getCode())
            throw new MmallException("产品已下架或者删除");
        return assembleProductDetailVo(product.get());
    }


    public Product getProductById(Integer productId) throws MmallException {
        if (productId == null)
            throw new MmallException(ResponseCode.ILLEGAL_ARGUMENT.getDesc());

        Optional<Product> product = productDao.findById(productId);

        if (!product.isPresent() || product.get().getStatus() != Const.ProductStatusEnum.ON_SALE.getCode())
            throw new MmallException("产品已下架或者删除");
        return product.get();
    }

//    @Override
//    public ServerResponse getProductByTime(Date d1, Date d2) {
//        List<Product> products = productMapper.selectByTime(d1, d2);
//        List<ProductListVo> productListVoList = new ArrayList<>();
//        if (products == null)
//            return ServerResponse.createByError();
//        for (Product product : products) {
//            ProductListVo productListVo = assembleProductListVo(product);
//            productListVoList.add(productListVo);
//        }
//        return ServerResponse.createBySuccess(productListVoList);
//    }

    @SuppressWarnings("all")
    public Page getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) throws MmallException {
        List<Integer> categoryIdList = new ArrayList<>();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        if (categoryId != null) {
            Category category = categoryService.selectById(categoryId);
            if (category == null && StringUtils.isBlank(keyword))
                return Page.empty();
            else
                categoryIdList = categoryService.selectCategoryAndChildrenById(category.getId());
        }
        if (StringUtils.isNotBlank(keyword))
            keyword = "%" + keyword + "%";
        Page<Product> productList = null;
        if (StringUtils.isEmpty(keyword) && categoryIdList.isEmpty())
            productList = productDao.findAll(pageable);
        else if (StringUtils.isNotBlank(keyword))
            productList = productDao.findByNameLike(keyword, pageable);
        else if (!categoryIdList.isEmpty())
            productList = productDao.findByCategoryIdIn(categoryIdList, pageable);
        else
            throw new MmallException("未找到产品");

        List<ProductListResponse> productListVoList = new ArrayList<>();
        for (Product product : productList)
            productListVoList.add(assembleProductListVo(product));

        return new PageImpl(productListVoList, pageable, productList.getTotalElements());
    }

    @Override
    public void update(Product product) {
       productDao.saveAndFlush(product);
    }
}