package com.wq.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Author: weiqiang
 * Time: 2020/3/31 下午12:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LoginSuccessResponse {
   private String mes;
   private String Token;
}
