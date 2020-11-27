package cn.edu.xmu.goods.dao;

import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;

import java.lang.reflect.Method;

public class ActivityDaoReflect {

    public Object getActivityById(String name, Long id){
        try{
            Class<?> mapper = Class.forName("cn.edu.xmu.goods.mapper." + name);
            Method method = mapper.getMethod("selectByPrimaryKey",Long.class);
            return new ReturnObject(method.invoke(id));
        } catch (Exception e) {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "没有此方法");
        }
    }
}
