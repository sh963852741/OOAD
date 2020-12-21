package cn.edu.xmu.flashsale.model.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FlashSaleVo {

    @DateTimeFormat
    @NotNull
    private LocalDateTime flashDate;
}
