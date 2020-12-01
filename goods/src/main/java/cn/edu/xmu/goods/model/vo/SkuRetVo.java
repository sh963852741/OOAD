package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SkuRetVo {
    private Long id;
    private String name;
    private String skuSn;
    private String detail;
    private String imageUrl;
    private Long originalPrice;
    private Long price;
    private Integer inventory;
    private String configuration;
    private Long weight;
    private String gmtCreate;
    private String gmtModified;
    private SpuRetVo spu;
}
