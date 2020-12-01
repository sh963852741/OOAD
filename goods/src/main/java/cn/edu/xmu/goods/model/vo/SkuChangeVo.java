package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value = "修改SKU视图")
public class SkuChangeVo {
    String name;
    Long originalPrice;
    String configuration;
    Long weight;
    Integer inventory;
    String detail;
}
