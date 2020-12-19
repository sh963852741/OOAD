package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.activity.model.po.GrouponActivityPo;
import cn.edu.xmu.activity.utility.MyDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@ApiModel(value = "新建团购视图")
public class GrouponActivityVo {

    @ApiModelProperty(value = "活动名称")
    String name;

    @JsonDeserialize(using = MyDeserializer.class)
    @ApiModelProperty(value = "团购规则")
    @NotBlank
    String strategy;

    @ApiModelProperty(value = "团购开始时间")
    @DateTimeFormat
    @Future
    LocalDateTime beginTime;

    @ApiModelProperty(value = "团购结束时间")
    @DateTimeFormat
    @Future
    LocalDateTime endTime;

    Long id;

    @DateTimeFormat
    private LocalDateTime gmtCreate;

    @DateTimeFormat
    private LocalDateTime gmtModified;

    public SpuInActivityVo goodsSpu;

    public Map<String, Object> shop = new HashMap<>();

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
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
    }
}
