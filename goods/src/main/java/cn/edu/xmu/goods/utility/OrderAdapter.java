package cn.edu.xmu.goods.utility;


import cn.edu.xmu.oomall.order.dto.FreightModelDto;

import java.util.HashMap;
import java.util.Map;

public class OrderAdapter {

    public static Map<String, Object> adapterFreigthModel(FreightModelDto dto){
        Map<String, Object> ret = new HashMap<>();
        ret.put("id", dto.getId());
        ret.put("name", dto.getName());
        ret.put("type", dto.getType());
        ret.put("unit", dto.getUnit());
        ret.put("default", dto.getDefaultModel());
        ret.put("gmtCreate", dto.getGmtCreate());
        ret.put("gmtModified", dto.getGmtModified());
        return ret;
    }

}
