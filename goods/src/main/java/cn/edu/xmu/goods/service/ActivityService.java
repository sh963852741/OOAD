package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CouponActivityDao;
import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.dao.GrouponActivityDao;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.GrouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.ActivityFinderVo;
import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.model.vo.GrouponActivityVo;
import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public ReturnObject<List<PresaleActivityVo>> getPresaleActivities(ActivityFinderVo activityFinderVo, boolean all) {
        List<PresaleActivityPo> presaleList;
        if (activityFinderVo.getSpuId() != null && !all) {
            presaleList = presaleActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            presaleList = presaleActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        List<PresaleActivityVo> retList = presaleList.stream().map(e -> new PresaleActivityVo(e)).collect(Collectors.toList());
        return new ReturnObject(retList);
    }

    public ReturnObject<PresaleActivityVo> addPresaleActivity(PresaleActivityVo presaleActivityVo, Long spuId, Long shopId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        if (presaleActivityDao.addActivity(po, spuId, shopId) == 1) {
            return new ReturnObject(new PresaleActivityVo(po));
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleActivityVo presaleActivityVo, Long shopId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        if (presaleActivityDao.updateActivity(po, id)) {
            return new ReturnObject(new PresaleActivityVo(po));
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
        List<GrouponActivityPo> grouponList;
        if (activityFinderVo.getSpuId() != null && !all) {
            grouponList = grouponActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            grouponList = grouponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        List<GrouponActivityVo> retList = grouponList.stream().map(e -> new GrouponActivityVo(e)).collect(Collectors.toList());
        return new ReturnObject(retList);
    }

    public ReturnObject<GrouponActivityVo> addGrouponActivity(GrouponActivityVo grouponActivityVo, long spuId, long shopId) {
        GrouponActivityPo po = grouponActivityVo.createPo();
        if (grouponActivityDao.addActivity(po, spuId, shopId)) {
            return new ReturnObject(new GrouponActivityVo(po));
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject<GrouponActivityVo> modifyGrouponActivity(Long id, GrouponActivityVo grouponActivityVo, long shopId) {
        if (grouponActivityDao.updateActivity(grouponActivityVo.createPo(), id)) {
            return new ReturnObject(grouponActivityVo);
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject<?> delGrouponActivity(long id) {
        if (grouponActivityDao.delActivity(id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 优惠活动部分
    public ReturnObject getCouponActivityStatus() {
        return new ReturnObject(CouponActivity.CouponStatus.values());
    }

    public ReturnObject getCouponStatus() {
        return new ReturnObject(Coupon.CouponStatus.values());
    }

    public ReturnObject<PageInfo<CouponActivityVo>> getCouponActivities(ActivityFinderVo activityFinderVo) {
        PageInfo<CouponActivityPo> couponList;
        if (activityFinderVo.getTimeline() == CouponActivity.CouponStatus.CANCELED.getCode()) {
            couponList = couponActivityDao.getInvalidActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId());
        } else {
            couponList = couponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline());
        }

        List<CouponActivityVo> list = couponList.getList().stream().map(e -> new CouponActivityVo(e)).collect(Collectors.toList());
        PageInfo<CouponActivityVo> ret = new PageInfo<>(list);
        ret.setPages(couponList.getPages());
        ret.setPageNum(couponList.getPageNum());
        ret.setPageSize(couponList.getPageSize());
        ret.setTotal(couponList.getTotal());
        return new ReturnObject(ret);
    }

    public ReturnObject addCouponActivity(CouponActivityVo couponActivityVo, Long shopId) {
        CouponActivityPo po = couponActivityVo.createPo();
        if (couponActivityDao.addActivity(po, shopId)) {
            return new ReturnObject(new CouponActivityVo(po));
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyCouponActivity(Long id, CouponActivityVo couponActivityVo, long shopId) {
        if (couponActivityDao.updateActivity(couponActivityVo.createPo(), id)) {
            return new ReturnObject(couponActivityVo);
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject modifyCouponActivityStatus(Long id, CouponActivity.CouponStatus status){
        if (couponActivityDao.changeActivityStatus(id, status.getCode())) {
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

    public ReturnObject addSPUToCouponActivity(long spuId, long shopId, long activityId){
        SPUPo spu = goodsService.getSpuById(spuId).getData();
        if(spu == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "SPU ID不存在");
        } else if(spu.getShopId() != shopId) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "SPU 不属于你的店铺");
        }

        Long insertId = couponActivityDao.addSpuToActivity(activityId, spuId);
        if(insertId != null){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject removeSPUFromCouponActivity(long spuId, long shopId, long activityId){
        return null;
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
