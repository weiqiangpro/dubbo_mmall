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
@Table(name = "mmall_order_item")
public class OrderItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "order_no", nullable = false)
    private Long orderNo;

    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    //'生成订单时的商品单价，单位是元,保留两位小数'
    @Column(name = "current_unit_price", nullable = false)
    private BigDecimal currentUnitPrice;

    //'商品数量
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    //商品总价,单位是元,保留两位小数
    @Column(name = "totalPrice", nullable = false)
    private BigDecimal totalPrice;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;
}