package cn.edu.xmu.ooad.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@ApiModel(value = "Sku视图")
public class SkuVo {

    //api上面参数有点不懂 暂时这样
    String sn;
    String name;
    Long originalPrice;
    String configuration;
    Long weight;
    String imageUrl;
    Long inventory;
    String detail;

}
