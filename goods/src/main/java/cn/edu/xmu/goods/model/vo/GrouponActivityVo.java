package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@NoArgsConstructor
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

    Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    public HashMap<String, Object> subData = new HashMap<>();

    public GrouponActivityPo createPo() {
        GrouponActivityPo po = new GrouponActivityPo();
        po.setName(name);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setStrategy(strategy);
        return po;
    }

    public GrouponActivityVo (GrouponActivityPo po) {
        id = po.getId();
        name = po.getName();
        strategy = po.getStrategy();
        beginTime = po.getBeginTime();
        endTime = po.getEndTime();
        gmtCreate = po.getGmtCreated();
        gmtModified = po.getGmtModified();
    }
}
