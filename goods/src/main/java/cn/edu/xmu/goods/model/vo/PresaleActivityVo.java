package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@NoArgsConstructor
@ApiModel(value = "新建预售视图")
public class PresaleActivityVo {

    @ApiModelProperty(value = "预售名称")
    String name;

    @ApiModelProperty(value = "预售款")
    Long advancePayPrice;

    @ApiModelProperty(value = "尾款")
    Long restPayPrice;

    @ApiModelProperty(value = "预售数量")
    Integer quantity;

    @ApiModelProperty(value = "预售开始时间")
    LocalDateTime beginTime;

    @ApiModelProperty(value = "预售结束时间")
    LocalDateTime endTime;

    @ApiModelProperty(value = "付款时间")
    LocalDateTime payTime;

    private Long id;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    public HashMap<String, Object> subData = new HashMap<>();


    public PresaleActivityPo createPo(){
        PresaleActivityPo po = new PresaleActivityPo();
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setAdvancePayPrice(advancePayPrice);
        po.setPayTime(payTime);
        po.setRestPayPrice(restPayPrice);
        po.setAdvancePayPrice(advancePayPrice);
        po.setName(name);
        po.setQuantity(quantity);

        return po;
    }

    public PresaleActivityVo(PresaleActivityPo po) {
        id = po.getId();
        name = po.getName();
        state = po.getState();
        quantity = po.getQuantity();
        beginTime = po.getBeginTime();
        endTime = po.getEndTime();
        payTime = po.getPayTime();
        advancePayPrice = po.getAdvancePayPrice();
        restPayPrice = po.getAdvancePayPrice();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
    }
}
