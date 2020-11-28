package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FloatPriceRetVo {

    private Long id;
    private Long activityPrice;
    private Long quantity;
    private LocalDateTime beginTime;
    private LocalDateTime endTime;
    private Long createBy;
    private Long modifiedBy;
    private Byte valid;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;
}
