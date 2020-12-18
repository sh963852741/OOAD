package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.activity.utility.MyDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponActicityChangeVo {
    @NotBlank
    @ApiModelProperty(value = "优惠名称")
    String name;

    @NotNull
    @Min(0)
    @ApiModelProperty(value = "优惠券数目，0表示不限量")
    Integer quantity;

    @Future
    @DateTimeFormat
    @ApiModelProperty(value = "活动结束时间")
    LocalDateTime endTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @DateTimeFormat
    @ApiModelProperty(value = "活动开始时间")
    LocalDateTime beginTime;

    @NotBlank
    @JsonDeserialize(using = MyDeserializer.class)
    @ApiModelProperty(value = "优惠规则")
    String strategy;
}
