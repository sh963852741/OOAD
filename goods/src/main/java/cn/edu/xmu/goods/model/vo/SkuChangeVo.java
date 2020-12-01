package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel(value = "修改SKU视图")
public class SkuChangeVo {
    String name;
    @Min(0)
    Long originalPrice;
    String configuration;
    @Min(0)
    Long weight;
    @Min(0)
    Integer inventory;
    String detail;
}
