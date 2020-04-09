package com.wq.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;

/**
 * Created by weiqiang
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CartResponse {

    private Page<CartProductResponse> cartProductResponsePage;
    private BigDecimal cartTotalPrice;
    private String imageHost;

}
