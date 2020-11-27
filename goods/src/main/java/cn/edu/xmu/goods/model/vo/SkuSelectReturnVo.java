package cn.edu.xmu.goods.model.vo;


import cn.edu.xmu.ooad.model.VoObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SkuSelectReturnVo {

    private Long page;
    private Long pageSize;
    private Long total;
    private Long pages;
    private List<SkuSimpleRetVo> list=new ArrayList<>();

}
