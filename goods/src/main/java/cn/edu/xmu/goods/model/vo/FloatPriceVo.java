package cn.edu.xmu.goods.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(value = "浮动价格视图")
public class FloatPriceVo {

    @Min(0)
    @ApiModelProperty(value = "Sku生效价格")
    @NotNull
    Long activityPrice;

    @ApiModelProperty(value = "生效开始时间")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @DateTimeFormat
    LocalDateTime beginTime;

    @ApiModelProperty(value = "生效结束时间")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Future
    @DateTimeFormat
    LocalDateTime endTime;

    @Min(0)
    @ApiModelProperty(value = "对应数量")
    @NotNull
    Integer quantity;

}
