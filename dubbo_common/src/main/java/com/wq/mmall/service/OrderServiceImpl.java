//package com.wq.mmall.service;
//
//
//import com.google.common.collect.Lists;
//import org.apache.commons.lang.time.DateUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.util.*;
//
///**
// * Created by weiqiang
// */
//
//@Service("iOrderService")
//public class OrderServiceImpl implements IOrderService {
//
////    @Autowired
////    private OrderMapper orderMapper;
////    @Autowired
////    private OrderItemMapper orderItemMapper;
////    @Autowired
////    private PayInfoMapper payInfoMapper;
////    @Autowired
////    private CartMapper cartMapper;
////    @Autowired
////    private ProductMapper productMapper;
////    @Autowired
////    private ShippingMapper shippingMapper;
//
//
//    @Override
//    public ServerResponse createOrder(Order order) {
//        Product product = productMapper.selectByPrimaryKey(order.getId());
//        if (product == null)
//            return ServerResponse.createByErrorMessage("提交订单失败");
//        order.setPostage(0).setProductImage(product.getMainImage()).setProductName(product.getName()).setTotalPrice(product.getPrice());
//        int ok = orderMapper.insertSelective(order);
//        if (ok > 0)
//            return ServerResponse.createBySuccessMessage("提交订单成功");
//        return ServerResponse.createByErrorMessage("提交订单失败");
//    }
//
//    @Override
//    public ServerResponse<PageInfo> getOrderList(Integer userId, int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<Order> orderList = orderMapper.selectByUserId(userId);
//        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
//        PageInfo pageResult = new PageInfo(orderList);
//        pageResult.setList(orderVoList);
//        return ServerResponse.createBySuccess(pageResult);
//    }
//
//    @Override
//    public ServerResponse getOrderDetail(Integer orderId) {
//        Order order = orderMapper.selectByPrimaryKey(orderId);
//        return ServerResponse.createBySuccess(order);
//    }
//
//
//    private List<OrderVo> assembleOrderVoList(List<Order> orderList) {
//        List<OrderVo> orderVoList = Lists.newArrayList();
//        for (Order order : orderList) {
//            OrderVo orderVo = assembleOrderVo(order);
//            orderVoList.add(orderVo);
//        }
//        return orderVoList;
//    }
//
//    private OrderVo assembleOrderVo(Order order) {
//        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
//        String a = "";
//        if (shipping != null) {
//            a = shipping.toString();
//        }
//        return new OrderVo().setAddress(a).setCount(order.getCount()).setOrderId(order.getId())
//                .setPayment(order.getPayment()).setPaymentType(order.getPaymentType()).setPostage(order.getPostage())
//                .setProductId(order.getProductId()).setProductImage(order.getProductImage()).setProductName(order.getProductName())
//                .setStatus(order.getStatus()).setTotalPrice(order.getTotalPrice());
//    }
//
//
//    @Override
//    public ServerResponse<PageInfo> manageList(int pageNum, int pageSize) {
//        PageHelper.startPage(pageNum, pageSize);
//        List<Order> orderList = orderMapper.selectAllOrder();
//        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
//        PageInfo pageResult = new PageInfo(orderList);
//        pageResult.setList(orderVoList);
//        return ServerResponse.createBySuccess(pageResult);
//    }
//
//    @Override
//    public ServerResponse manageDetail(int orderId) {
//        Order order = orderMapper.selectByPrimaryKey(orderId);
//        return ServerResponse.createBySuccess(order);
//    }
//
//    @Override
//    public ServerResponse<String> manageUpdateGoods(int orderId, int status) {
//        if (!Const.OrderStatusEnum.con(status))
//            return ServerResponse.createByErrorMessage("状态修改失败");
//        Order order = new Order().setId(orderId).setStatus(status);
//        int ok = orderMapper.updateByPrimaryKeySelective(order);
//        if (ok > 0)
//            return ServerResponse.createBySuccessMessage("修改成功");
//        return ServerResponse.createByErrorMessage("状态修改失败");
//    }
//
//
//    @Override
//    public ServerResponse getByTime(String d1, String d2) {
//
//        List<Order> orders = orderMapper.selectByTime(d1, d2);
//        if (orders == null)
//            return ServerResponse.createByError();
//        List<OrderVo> orderVos = assembleOrderVoList(orders);
//        return ServerResponse.createBySuccess(orders);
//    }
//
//    @Override
//    public ServerResponse getByTimeNums(String d1, String d2) {
//        List<Order> orders = orderMapper.selectByTime(d1, d2);
//        int[] nums = new int[12];
//        int start = 0;
//        if (orders != null && orders.size() != 0) {
//            start = hour(orders.get(0));
//            if (start % 2 == 1)
//                start--;
//            for (Order order : orders) {
//                if (hour(order) - start >= 2)
//                    start += 2;
//                nums[start / 2]++;
//            }
//        }
//        return ServerResponse.createBySuccess(new Day(nums));
//    }
//
//    @Override
//    public ServerResponse categoryNums(String d1, String d2) {
//        List<OrderNums> orders = orderMapper.categoryNums(d1, d2);
//        HashMap<OrderNums, Integer> hs = new HashMap();
//        if (orders != null) {
//            List<OrderItem> orderItems = new ArrayList<>();
//            int sum = orders.size();
//            if (sum == 1) {
//                orderItems.add(new OrderItem(orders.get(0).getCategoryName(), 1, "100.00"));
//            } else if (sum > 1) {
//                Integer o;
//                for (OrderNums order : orders) {
//                    o = null;
//                    o = hs.get(order);
//                    hs.put(order, o != null ? o + 1 : 1);
//                }
//                List<Map.Entry<OrderNums, Integer>> list = new ArrayList<Map.Entry<OrderNums, Integer>>(hs.entrySet());
//                Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
//
//                DecimalFormat df = new DecimalFormat("0.00");//格式化小数
//                if (sum <= 5)
//                    for (Map.Entry<OrderNums, Integer> mapping : list) {
//                        orderItems.add(new OrderItem(mapping.getKey().getCategoryName(),
//                                mapping.getValue(),
//                                df.format((float) mapping.getValue() / sum * 100)));
//                    }
//                else {
//                    int ok = 0;
//                    int i = 0;
//                    for (Map.Entry<OrderNums, Integer> mapping : list) {
//                        orderItems.add(new OrderItem(mapping.getKey().getCategoryName(),
//                                mapping.getValue(),
//                                df.format((float) mapping.getValue() / sum * 100)));
//                        ok += mapping.getValue();
//                        if (++i > 5)
//                            break;
//                    }
//                    orderItems.add(new OrderItem("其他", sum - ok, df.format((float) (sum - ok) / sum * 100)));
//                }
//            }
//            return ServerResponse.createBySuccess(orderItems);
//        }
//        return ServerResponse.createByError();
//    }
//
//    private int hour(Order order) {
//        return Integer.parseInt(order.getCreateTime().substring(11, 13));
//    }
//
//    @Override
//    public ServerResponse datesNums(String d1, String d2) {
//        Date now = DateTimeUtil.strToDate(d1,"yyyy-MM-dd");
//        List<Order> orderList = orderMapper.selectByTime(d1, d2);
//        Datas datas = new Datas();
//        if (orderList != null) {
//            if (orderList.size() >= 1) {
//                int orders = 0, users = 0;
//                BigDecimal pays = new BigDecimal(0);
//                for (int i = 0; i < orderList.size(); i++) {
//                    if (DateUtils.isSameDay(now,DateTimeUtil.strToDate(orderList.get(i).getCreateTime()))) {
//                        orders++;
//                        pays.add(orderList.get(i).getPayment());
//                    } else {
//                      now =   DateUtils.addDays(now,1);
//                        datas.add(orders, pays, users);
//                        i--;
//                        orders = 0;
//                        //    users=0;
//                        pays = new BigDecimal(0);
//                    }
//                }
//                datas.add(orders, pays, users);
//            }
//            return ServerResponse.createBySuccess(datas);
//        }
//        return ServerResponse.createByError();
//    }
//
//    private int day(Order order) {
//        int a =  Integer.parseInt(order.getCreateTime().substring(5, 7));
//        return a;
//    }
//}
