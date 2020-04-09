package com.wq.mmall.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @Author: weiqiang
 * @Time: 2020/1/21 下午9:45
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class OrderNums {
    private Integer id;
    private Integer categoryId;
    private String categoryName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderNums orderNums = (OrderNums) o;
        return Objects.equals(categoryId, orderNums.categoryId);
    }

    @Override
    public int hashCode() {
        return categoryId;
    }
}
