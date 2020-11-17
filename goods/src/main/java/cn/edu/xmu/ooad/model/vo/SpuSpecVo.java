package cn.edu.xmu.ooad.model.vo;

import cn.edu.xmu.ooad.model.bo.SpecItem;
import lombok.Data;

import java.util.List;

@Data
public class SpuSpecVo {

    Long id;
    String name;
    List<SpecItem> specItems;
}
