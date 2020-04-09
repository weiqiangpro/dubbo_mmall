package com.wq.mmall.service;

import com.wq.mmall.entity.Shipping;
import com.wq.mmall.exception.MmallException;
import org.springframework.data.domain.Page;

/**
 * Created by weiqiang
 */

public interface IShippingService {
    String add(Shipping shipping);

    String del(Integer userId, Integer shippingId) throws MmallException;

    String update(Shipping shipping) throws MmallException;

    Shipping select(Integer userId, Integer shippingId) throws MmallException;

    Page list(Integer userId, int pageNum, int pageSize);
}
