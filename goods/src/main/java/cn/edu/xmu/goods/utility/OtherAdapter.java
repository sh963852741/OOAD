package cn.edu.xmu.goods.utility;

import cn.edu.xmu.privilegeservice.client.IUserService;
import org.apache.dubbo.config.annotation.DubboReference;

import java.util.HashMap;
import java.util.Map;

public class OtherAdapter {

    /**
     * @problem order 适配器
     */
 public static Map<String, Object> adapterSimpleUser(Long userId,String userName){
        Map<String, Object> ret = new HashMap<>();
        ret.put("id",userId);
        ret.put("userName",userName);
        return ret;
    }

}
