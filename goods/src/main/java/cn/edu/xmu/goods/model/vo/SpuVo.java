package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "Spu视图")
public class SpuVo {

    @ApiModelProperty(value = "spu名称")
    String name;

    @ApiModelProperty(value = "spu描述")
    String description;

    @ApiModelProperty(value = "spu规格描述")
    String specs;
}
