package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ActivityDaoReflect;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;

public class ActivityService {
    @Autowired
    ActivityDaoReflect activityDao;

    public ReturnObject getPresaleActivity(Long id, Long shopId){
        return new ReturnObject();
    }
}
