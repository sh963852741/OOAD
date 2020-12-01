package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ShopRetVo {
    private Long id;
    private String name;
    private byte state;
    private String createdBy;
    private String modifyBy;
}
