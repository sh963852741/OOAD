package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@ApiModel(value = "新建团购视图")
public class GrouponActivityVo {

    @ApiModelProperty(value = "活动名称")
    String name;

    @ApiModelProperty(value = "团购规则")
    String strategy;

    @ApiModelProperty(value = "团购开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dbeginTime;

    @ApiModelProperty(value = "团购结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dendTime;

    Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModified;

    public Map<String, Object> goodsSpu = new HashMap<>();

    public Map<String, Object> shop = new HashMap<>();

    public GrouponActivityPo createPo() {
        GrouponActivityPo po = new GrouponActivityPo();
        po.setName(name);
        po.setBeginTime(dbeginTime);
        po.setEndTime(dendTime);
        po.setStrategy(strategy);
        return po;
    }

    public GrouponActivityVo (GrouponActivityPo po) {
        id = po.getId();
        name = po.getName();
        strategy = po.getStrategy();
        dbeginTime = po.getBeginTime();
        dendTime = po.getEndTime();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
    }
}
