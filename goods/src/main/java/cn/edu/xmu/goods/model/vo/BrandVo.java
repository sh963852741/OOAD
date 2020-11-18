package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "品牌视图")
public class BrandVo {

    @ApiModelProperty(value = "品牌名称")
    String name;

    @ApiModelProperty(value = "品牌详细描述")
    String detail;

}
