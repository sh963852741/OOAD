package cn.edu.xmu.activity.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ActivityFinderVo {
    @ApiModelProperty(value = "商铺ID")
    Long shopId;

    @ApiModelProperty(value = "SPUID")
    Long spuId;

    @ApiModelProperty(value = "页数")
    Integer page;

    @ApiModelProperty(value = "每页数目")
    Integer pageSize;

    @ApiModelProperty(value = "时间线")
    Byte timeline;

    @ApiModelProperty(value = "活动状态")
    Byte state;

    @ApiModelProperty(value = "开始时间")
    LocalDateTime beginTime;

    @ApiModelProperty(value = "结束时间")
    LocalDateTime endTime;

    Long skuId;
}
