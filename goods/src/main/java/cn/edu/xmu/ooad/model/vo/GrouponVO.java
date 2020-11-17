package cn.edu.xmu.ooad.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "新建团购视图")
public class GrouponVO {

    @ApiModelProperty(value = "团购规则")
    String strategy;

    @ApiModelProperty(value = "团购开始时间")
    String beginTime;

    @ApiModelProperty(value = "团购结束时间")
    String endTime;
}
