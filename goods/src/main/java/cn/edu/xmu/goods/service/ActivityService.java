package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.ActivityDaoReflect;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.PresaleFinderVo;
import cn.edu.xmu.goods.model.vo.PresaleVo;
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

    public ReturnObject getEffectivePresaleActivities(PresaleFinderVo presaleFinderVo) {
        List presaleList = presaleActivityDao.getEffectiveActivities(
                presaleFinderVo.getPage(),presaleFinderVo.getPageSize(),presaleFinderVo.getShopId(),presaleFinderVo.getTimeline(),presaleFinderVo.getSpuId());
        return null;
    }
}
