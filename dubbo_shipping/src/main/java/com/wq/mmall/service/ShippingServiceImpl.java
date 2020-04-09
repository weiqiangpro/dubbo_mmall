package com.wq.mmall.service;


import com.wq.mmall.dao.ShippingDao;
import com.wq.mmall.entity.Shipping;
import com.wq.mmall.exception.MmallException;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by weiqiang
 */

@Service
@Transactional(rollbackFor = {RpcException.class,MmallException.class})
public class ShippingServiceImpl implements IShippingService {

    private final ShippingDao shippingDao;

    @Autowired
    public ShippingServiceImpl(ShippingDao shippingDao) {

        this.shippingDao = shippingDao;
    }

    @Override
    public String add(Shipping shipping) {
        shippingDao.save(shipping);
        return "添加地址成功";
    }

    @Override
    public String del(Integer userId, Integer shippingId) throws MmallException {
        if (shippingDao.deleteByIdAndUserId(shippingId, userId) <= 0)
            throw new MmallException("删除地址失败");
        return "删除地址成功";
    }

    @Override
    public String update(Shipping shipping) throws MmallException {
        Optional<Shipping> res = shippingDao.findByIdAndUserId(shipping.getId(), shipping.getUserId());
        if (!res.isPresent())
            throw new MmallException("该地址不存在");
        shippingDao.saveAndFlush(shipping);
        return "更新地址成功";
    }

    @Override
    public Shipping select(Integer userId, Integer shippingId) throws MmallException {
        Optional<Shipping> shipping = shippingDao.findByIdAndUserId(shippingId, userId);
        if (!shipping.isPresent()) {
            throw new MmallException("未找到该地址");
        }
        return shipping.get();
    }

    @Override
    public Page list(Integer userId, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return shippingDao.findByUserId(userId, pageable);
    }
}

