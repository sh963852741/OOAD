package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SkuSimpleRetVo {
    private Long id;
    private String name;
    private String skuSn;
    private String imageUrl;
    private Long inventory;
    private Long originalPrice;
    private Long price;
    private Byte disable;
}
