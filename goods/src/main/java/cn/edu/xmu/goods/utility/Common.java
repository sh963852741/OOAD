package cn.edu.xmu.goods.utility;

import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ResponseUtil;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.github.pagehelper.PageInfo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.NoArgsConstructor;
import springfox.documentation.spring.web.json.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Common {
    public static Object getPageRetObjectWisely(ReturnObject<Object> returnObject) {
        ResponseCode code = returnObject.getCode();
        switch (code){
            case OK:
                if (returnObject.getData() instanceof PageInfo<?>){
                    PageInfo objs = (PageInfo)returnObject.getData();
                    Map<String, Object> ret = new HashMap<>();
                    ret.put("list", objs.getList());
                    ret.put("total", objs.getTotal());
                    ret.put("page", objs.getPageNum());
                    ret.put("pageSize", objs.getPageSize());
                    ret.put("pages", objs.getPages());

                    return ResponseUtil.ok(ret);
                }else{
                    return ResponseUtil.ok();
                }
            default:
                return ResponseUtil.fail(returnObject.getCode(), returnObject.getErrmsg());
        }
    }
}
