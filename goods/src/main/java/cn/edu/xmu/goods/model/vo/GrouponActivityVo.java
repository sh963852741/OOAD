package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "新建团购视图")
public class GrouponActivityVo {

    @ApiModelProperty(value = "活动名称")
    String name;

    @ApiModelProperty(value = "团购规则")
    String strategy;

    @ApiModelProperty(value = "团购开始时间")
    LocalDateTime beginTime;

    @ApiModelProperty(value = "团购结束时间")
    LocalDateTime endTime;

    public GrouponActivityPo createPo() {
        GrouponActivityPo po = new GrouponActivityPo();
        po.setName(name);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setStrategy(strategy);
        return po;
    }
}
