package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.utility.Common;
import cn.edu.xmu.goods.utility.MyDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@NoArgsConstructor
@ApiModel(value = "优惠活动")
public class CouponActivityVo {

    @NotBlank
    @ApiModelProperty(value = "优惠名称")
    String name;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "优惠券数目，0表示不限量")
    Integer quantity;

    @NotNull
    @Min(0)
    @Max(1)
    @ApiModelProperty(value = "0表会每人数量，1表示总数控制")
    Byte quantityType;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "优惠券时长，0表示与活动相同，否则表示自领取后几日内有效")
    Byte validTerm;

    @DateTimeFormat
    @Future
    @ApiModelProperty(value = "开始领优惠券时间")
    LocalDateTime couponTime;

    @DateTimeFormat
    @Future
    @ApiModelProperty(value = "活动开始时间")
    LocalDateTime beginTime;

    @DateTimeFormat
    @Future
    @ApiModelProperty(value = "活动结束时间")
    LocalDateTime endTime;

    @NotBlank
    @JsonDeserialize(using = MyDeserializer.class)
    @ApiModelProperty(value = "优惠规则")
    String strategy;

    Long id;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private Byte state;
    public HashMap<String, Object> shop = new HashMap<>();
    public HashMap<String, Object> createdBy = new HashMap<>();
    public HashMap<String, Object> modiBy = new HashMap<>();

    public CouponActivityPo createPo() {
        CouponActivityPo po = new CouponActivityPo();
        po.setName(name);
        po.setQuantity(quantity);
        po.setQuantitiyType(quantityType);
        po.setValidTerm(validTerm);
        po.setCouponTime(couponTime);
        po.setBeginTime(beginTime);
        po.setEndTime(endTime);
        po.setStrategy(strategy);
        po.setState(state);
        return po;
    }

    public CouponActivityVo(CouponActivityPo po){
        id = po.getId();
        name = po.getName();
        quantity = po.getQuantity();
        quantityType = po.getQuantitiyType();
        validTerm = po.getValidTerm();
        couponTime = po.getCouponTime();
        beginTime = po.getBeginTime();
        endTime =po.getEndTime();
        strategy = po.getStrategy();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
    }
}
