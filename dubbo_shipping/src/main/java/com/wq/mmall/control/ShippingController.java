package com.wq.mmall.control;

import com.wq.mmall.annotation.ResponseAdvice;
import com.wq.mmall.entity.Shipping;
import com.wq.mmall.exception.MmallException;
import com.wq.mmall.service.IShippingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhaomanzhou
 */

@RestController
@RequestMapping("/shipping/")
@Api(tags = "收货地址api")
@ResponseAdvice
public class ShippingController {

    @Autowired
    private IShippingService iShippingService;


    @ApiOperation(value = "添加收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "收货人姓名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "收货人固定电话", required = true, dataType = "String"),
            @ApiImplicitParam(name = "province", value = "收货人省份", required = true, dataType = "String"),
            @ApiImplicitParam(name = "city", value = "收货人城市", required = true, dataType = "String"),
            @ApiImplicitParam(name = "district", value = "区/县", required = true, dataType = "String"),
            @ApiImplicitParam(name = "address", value = "详细地址", required = true, dataType = "String"),
            @ApiImplicitParam(name = "zip", value = "邮编", required = true, dataType = "String"),
    })
    @PostMapping("add.do")
    public String add(@RequestParam("userId") int userId,
                      @RequestParam("name") String name,
                      @RequestParam("phone") String phone,
                      @RequestParam("province") String province,
                      @RequestParam("city") String city,
                      @RequestParam("district") String district,
                      @RequestParam("address") String address,
                      @RequestParam("zip") String zip,
                      HttpServletRequest request) {
        Shipping shipping = new Shipping(userId, name, phone, address, province, city, district, zip);
        return iShippingService.add(shipping);
    }

    @ApiOperation(value = "删除收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "收货人姓名", required = true, dataType = "int")
    })
    @DeleteMapping("del.do")
    public String del(@RequestParam("shippingId") int shippingId,
                      @RequestParam("userId") int userId,
                      HttpServletRequest request) throws MmallException {
        return iShippingService.del(userId, shippingId);
    }

    @ApiOperation(value = "更新")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "收货地址id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "name", value = "收货人姓名", dataType = "String"),
            @ApiImplicitParam(name = "phone", value = "收货人固定电话", dataType = "String"),
            @ApiImplicitParam(name = "province", value = "收货人省份", dataType = "String"),
            @ApiImplicitParam(name = "city", value = "收货人城市", dataType = "String"),
            @ApiImplicitParam(name = "district", value = "区/县", dataType = "String"),
            @ApiImplicitParam(name = "address", value = "详细地址", dataType = "String"),
            @ApiImplicitParam(name = "zip", value = "邮编", dataType = "String"),
    })
    @PutMapping("update.do")
    public String update(@RequestParam("id") int id,
                         @RequestParam(value = "userId") int userId,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "phone", required = false) String phone,
                         @RequestParam(value = "province", required = false) String province,
                         @RequestParam(value = "city", required = false) String city,
                         @RequestParam(value = "district", required = false) String district,
                         @RequestParam(value = "address", required = false) String address,
                         @RequestParam(value = "zip", required = false) String zip,
                         HttpServletRequest request) throws MmallException {
        Shipping shipping = new Shipping(userId, name, phone, address, province, city, district, zip).setId(id);
        return iShippingService.update(shipping);
    }


    @ApiOperation(value = "收货地址详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shippingId", value = "收货地址id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "userId", value = "收货人姓名", required = true, dataType = "int")
    })
    @GetMapping("select.do")
    public Shipping select(@RequestParam("shippingId") int shippingId,
                           @RequestParam("userId") int userId,
                           HttpServletRequest request) throws MmallException {
        return iShippingService.select(userId, shippingId);
    }

    @ApiOperation(value = "所有收货地址")
    @ApiImplicitParams({
    @ApiImplicitParam(name = "userId", value = "收货人id", required = true, dataType = "int"),
            @ApiImplicitParam(name = "pageNum", value = "分类的id，默认为0", dataType = "int"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据的数量", dataType = "int"),
    })
    @GetMapping("list.do")

    public Page list(@RequestParam("userId") int userId,
                     @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                     @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                     HttpServletRequest request) {
        return iShippingService.list(userId, pageNum, pageSize);
    }
}
