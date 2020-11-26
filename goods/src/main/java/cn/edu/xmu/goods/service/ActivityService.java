package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ActivityDaoReflect;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.PresaleFinderVo;
import cn.edu.xmu.goods.model.vo.PresaleVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ActivityService {
    @Autowired
    PresaleActivityDao presaleActivityDao;
    @Autowired
    GoodsService goodsService;

    public ReturnObject getPresaleActivityStatus(){
        return new ReturnObject(PresaleActivity.PresaleStatus.values());
    }

    public ReturnObject getPresaleActivities(PresaleFinderVo presaleFinderVo, boolean all) {
        List presaleList = presaleActivityDao.getEffectiveActivities(
                presaleFinderVo.getPage(),presaleFinderVo.getPageSize(),presaleFinderVo.getShopId(),presaleFinderVo.getTimeline(),presaleFinderVo.getSpuId(),all);
        return null;
    }

    public ReturnObject addPresaleActivity(PresaleVo presaleVo) {
        if(presaleActivityDao.addActivity(presaleVo.createPo())){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleVo presaleVo) {
        if(presaleActivityDao.updateActivity(presaleVo.createPo(), id)){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delPresaleActivity(long id) {
        if(presaleActivityDao.delActivity(id)){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
}
