package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.activity.model.po.PresaleActivityPo;
import cn.edu.xmu.ooad.model.VoObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@NoArgsConstructor
@ApiModel(value = "新建预售视图")
public class PresaleActivityVo implements VoObject {

    @NotBlank
    @ApiModelProperty(value = "预售名称")
    String name;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "预售款")
    Long advancePayPrice;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "尾款")
    Long restPayPrice;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "预售数量")
    Integer quantity;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @ApiModelProperty(value = "预售开始时间")
    LocalDateTime beginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @ApiModelProperty(value = "预售结束时间")
    LocalDateTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @ApiModelProperty(value = "付款时间")
    LocalDateTime payTime;

    private Long id;
    private Byte state;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    public HashMap<String, Object> goodsSku = new HashMap<>();
    public HashMap<String, Object> shop = new HashMap<>();


    public PresaleActivityPo createPo(){
        PresaleActivityPo po = new PresaleActivityPo();
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setAdvancePayPrice(advancePayPrice);
        po.setPayTime(payTime);
        po.setRestPayPrice(restPayPrice);
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
        restPayPrice = po.getRestPayPrice();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
}
