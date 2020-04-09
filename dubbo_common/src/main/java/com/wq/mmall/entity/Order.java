package com.wq.mmall.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Table(name = "mmall_order")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "order_no", nullable = false)
    private long orderNo;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "shipping_id", nullable = false)
    private Integer shippingId;

    //实付金额
    @Column(name = "payment", nullable = false)
    private BigDecimal payment;

    //支付类型,1-在线支付'
    @Column(name = "payment_type", nullable = false)
    private Integer paymentType;

    //'运费,单位是元'
    @Column(name = "postage", nullable = false)
    private Integer postage;

    //订单状态:0-已取消-10-未付款，20-已付款，40-已发货，50-交易成功，60-交易关闭
    @Column(name = "status", nullable = false)
    private Integer status;

    @Column(name = "payment_time", nullable = false)
    private Date paymentTime;

    @Column(name = "send_time", nullable = false)
    private Date sendTime;

    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "close_time", nullable = false)
    private Date closeTime;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;
}