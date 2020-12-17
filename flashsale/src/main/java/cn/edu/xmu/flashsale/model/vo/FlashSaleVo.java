package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;

import javax.validation.constraints.Future;
import java.time.LocalDate;

@Data
public class FlashSaleVo {
    @Future
    private LocalDate flashDate;
}
