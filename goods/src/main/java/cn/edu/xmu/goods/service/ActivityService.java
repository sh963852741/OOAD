package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CouponActivityDao;
import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.dao.GrouponActivityDao;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.CouponPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.vo.ActivityFinderVo;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    PresaleActivityDao presaleActivityDao;
    @Autowired
    GrouponActivityDao grouponActivityDao;
    @Autowired
    CouponActivityDao couponActivityDao;
    @Autowired
    CouponDao couponDao;

    @Autowired
    GoodsService goodsService;

    //region 预售活动部分
    public ReturnObject getPresaleActivityStatus() {
        return new ReturnObject(PresaleActivity.PresaleStatus.values());
    }

    public ReturnObject getPresaleActivities(ActivityFinderVo activityFinderVo, boolean all) {
        List<PresaleActivityPo> presaleList;
        if (activityFinderVo.getSpuId() != null && !all) {
            presaleList = presaleActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            presaleList = presaleActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        return new ReturnObject(presaleList);
    }

    public ReturnObject addPresaleActivity(PresaleActivityVo presaleActivityVo, Long spuId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        if (presaleActivityDao.addActivity(po, spuId) == 1) {
            return new ReturnObject(po);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleActivityVo presaleActivityVo) {
        if (presaleActivityDao.updateActivity(presaleActivityVo.createPo(), id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delPresaleActivity(long id) {
        if (presaleActivityDao.delActivity(id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 团购活动部分
    public ReturnObject grouponActivityStatus() {
        return new ReturnObject(GrouponActivity.GrouponStatus.values());
    }

    public ReturnObject getGrouponActivities(ActivityFinderVo activityFinderVo, boolean all) {
        if (activityFinderVo.getSpuId() != null && !all) {
            List presaleList = grouponActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            List presaleList = grouponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        return null;
    }

    public ReturnObject addGrouponActivity(GrouponActivityVo grouponActivityVo) {
        if (grouponActivityDao.addActivity(grouponActivityVo.createPo())) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyGrouponActivity(Long id, GrouponActivityVo grouponActivityVo) {
        if (grouponActivityDao.updateActivity(grouponActivityVo.createPo(), id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delGrouponActivity(long id) {
        if (grouponActivityDao.delActivity(id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 优惠活动部分
    public ReturnObject couponActivityStatus() {
        return new ReturnObject(CouponActivity.CouponStatus.values());
    }

    public ReturnObject getCouponActivities(ActivityFinderVo activityFinderVo) {
        if (activityFinderVo.getTimeline() == CouponActivity.CouponStatus.CANCELED.getCode()) {
            List presaleList = couponActivityDao.getInvalidActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId());
        } else {
            List presaleList = couponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline());
        }
        return null;
    }

    public ReturnObject addCouponActivity(CouponActivityVo couponActivityVo) {
        if (couponActivityDao.addActivity(couponActivityVo.createPo())) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyCouponActivity(Long id, CouponActivityVo couponActivityVo) {
        if (couponActivityDao.updateActivity(couponActivityVo.createPo(), id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delCouponActivity(long id) {
        if (couponActivityDao.delActivity(id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 优惠券部分
    public ReturnObject getCouponList(Long userId, Byte state, Integer page, Integer pageSize) {
        List Coupons = couponDao.getCouponList(userId, state, page, pageSize);
        return new ReturnObject(Coupons);
    }

    public ReturnObject useCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.USED.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.DELETED.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject refundCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.AVAILABLE.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject claimCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.AVAILABLE.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    //endregion
}
