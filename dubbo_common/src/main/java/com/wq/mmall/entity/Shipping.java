package com.wq.mmall.entity;

import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Accessors(chain = true)
@Table(name = "mmall_shipping")
public class Shipping implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "receiver_name", nullable = false)
    private String receiverName;

    @Column(name = "receiver_phone", nullable = false)
    private String receiverPhone;

    @Column(name = "receiver_address", nullable = false)
    private String receiverAddress;

    @Column(name = "receiver_province", nullable = false)
    private String receiverProvince;

    @Column(name = "receiver_city", nullable = false)
    private String receiverCity;

    @Column(name = "receiver_district", nullable = false)
    private String receiverDistrict;

    @Column(name = "receiver_zip", nullable = false)
    private String receiverZip;

    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    public Shipping(Integer userId,
                    String receiverName,
                    String receiverPhone,
                    String receiverAddress,
                    String receiverProvince,
                    String receiverCity,
                    String receiverDistrict,
                    String receiverZip) {
        this.userId = userId;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.receiverProvince = receiverProvince;
        this.receiverCity = receiverCity;
        this.receiverDistrict = receiverDistrict;
        this.receiverZip = receiverZip;
    }

}