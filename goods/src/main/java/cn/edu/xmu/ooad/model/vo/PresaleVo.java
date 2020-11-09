package cn.edu.xmu.ooad.model.vo;

import lombok.Data;

@Data
public class PresaleVo {
    String name;
    Long advancePayPrice;
    Long restPayPrice;
    Long quantity;
    String beginTime;
    String endTime;
    String payTime;
}
