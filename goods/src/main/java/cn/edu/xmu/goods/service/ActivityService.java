package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CouponActivityDao;
import cn.edu.xmu.goods.dao.GrouponActivityDao;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.vo.ActivityFinderVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ActivityService {
    @Autowired
    PresaleActivityDao presaleActivityDao;
    @Autowired
    GrouponActivityDao grouponActivityDao;
    @Autowired
    CouponActivityDao couponActivityDao;

    @Autowired
    GoodsService goodsService;

    //region 预售活动部分
    public ReturnObject getPresaleActivityStatus(){
        return new ReturnObject(PresaleActivity.PresaleStatus.values());
    }

    public ReturnObject getPresaleActivities(ActivityFinderVo activityFinderVo, boolean all) {
        if(activityFinderVo.getSpuId() != null && !all) {
            List presaleList = presaleActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            List presaleList = presaleActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(),all);
        }
        return null;
    }

    public ReturnObject addPresaleActivity(PresaleActivityVo presaleActivityVo) {
        if(presaleActivityDao.addActivity(presaleActivityVo.createPo())){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleActivityVo presaleActivityVo) {
        if(presaleActivityDao.updateActivity(presaleActivityVo.createPo(), id)){
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
    //endregion

    //region 团购活动部分
    public ReturnObject grouponActivityStatus(){
        return new ReturnObject(GrouponActivity.GrouponStatus.values());
    }

    public ReturnObject getGrouponActivities(ActivityFinderVo activityFinderVo, boolean all) {
        if(activityFinderVo.getSpuId() != null && !all) {
            List presaleList = grouponActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            List presaleList = grouponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(),all);
        }
        return null;
    }

    public ReturnObject addGrouponActivity(GrouponActivityVo grouponActivityVo) {
        if(grouponActivityDao.addActivity(grouponActivityVo.createPo())){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyGrouponActivity(Long id, GrouponActivityVo grouponActivityVo) {
        if(grouponActivityDao.updateActivity(grouponActivityVo.createPo(), id)){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delGrouponActivity(long id) {
        if(grouponActivityDao.delActivity(id)){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 优惠活动部分
    public ReturnObject conponActivityStatus(){
        return new ReturnObject(CouponActivity.CouponStatus.values());
    }

    //endregion
}
