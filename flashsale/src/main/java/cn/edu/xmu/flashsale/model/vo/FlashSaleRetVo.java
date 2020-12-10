package cn.edu.xmu.flashsale.model.vo;

import cn.edu.xmu.flashsale.model.bo.TimeSegment;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class FlashSaleRetVo {
    private Long id;
    private LocalDate flashDate;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private TimeSegment timeSegment;

    public FlashSaleRetVo(FlashSalePo po, TimeSegment timeSegment){
        id = po.getId();
        flashDate = po.getFlashDate().toLocalDate();
        gmtCreate = po.getGmtCreate();
        gmtModified = po.getGmtModified();
        this.timeSegment = timeSegment;
    }
}
