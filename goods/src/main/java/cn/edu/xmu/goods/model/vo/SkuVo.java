package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "Sku视图")
public class SkuVo {

    SpuSpecVo spuSpec;
    String sn;
    String name;
    Long originalPrice;
    String configuration;
    Long weight;
    String imageUrl;
    Long inventory;
    String detail;

}
