package com.wq.mmall.service;

import com.wq.mmall.entity.Product;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.vo.ProductDetailResponse;
import org.springframework.data.domain.Page;
/**
 * Created by weiqiang
 */
public interface IProductService {

    ProductDetailResponse getProductDetail(Integer productId) throws MmallException;
     Product getProductById(Integer productId) throws MmallException;
    Page getProductByKeywordCategory(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) throws MmallException;

    void update(Product product);

//    ServerResponse<Page> getProductByKeywordCategoryByStock(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy);
//    ServerResponse saveProductDetail(Product product, MultipartFile main_image, List<MultipartFile> sub_images, List<MultipartFile> details);
//    ServerResponse deteleProduct(int productId);
//    ServerResponse<ProductDetailResponse> updateProductDetail(Product product);
//    ServerResponse<ProductDetailResponse> getProductByTime(Date d1, Date d2);

}
