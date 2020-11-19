package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.SpecItem;
import lombok.Data;

import java.util.List;

@Data
public class SpuSpecVo {

    Long id;
    String name;
    List<SpecItem> specItems;
}
