package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CouponActivityDao;
import cn.edu.xmu.goods.dao.CouponDao;
import cn.edu.xmu.goods.dao.GrouponActivityDao;
import cn.edu.xmu.goods.dao.PresaleActivityDao;
import cn.edu.xmu.goods.model.bo.*;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    ShopService shopService;

    //region 预售活动部分
    public ReturnObject getPresaleActivityStatus() {
        return new ReturnObject(PresaleActivity.PresaleStatus.values());
    }

    public ReturnObject<List<PresaleActivityVo>> getAllPresaleActivities(ActivityFinderVo activityFinderVo) {
        List<PresaleActivityPo> presaleList;
        presaleList = presaleActivityDao.getAllActivityBySPUId(activityFinderVo.getTimeline(),activityFinderVo.getSpuId());
        List<PresaleActivityVo> retList = presaleList.stream().map(e -> new PresaleActivityVo(e)).collect(Collectors.toList());
        return new ReturnObject(retList);
    }

    public ReturnObject<PageInfo<VoObject>> getPresaleActivities(ActivityFinderVo activityFinderVo) {
            PageInfo<PresaleActivityPo> po;
            if (activityFinderVo.getSpuId() != null) {
                po = presaleActivityDao.getActivitiesBySPUId(
                        activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
            } else {
                po = presaleActivityDao.getEffectiveActivities(
                        activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline());
            }
            List<PresaleActivityVo> retList = po.getList().stream().map(e -> new PresaleActivityVo(e)).collect(Collectors.toList());

            PageInfo<PresaleActivityVo> ret = new PageInfo(retList);
            ret.setPageNum(po.getPageNum());
            ret.setPages(po.getPages());
            ret.setTotal(po.getTotal());
            ret.setPageSize(po.getPageSize());
            return new ReturnObject(ret);

    }

    public ReturnObject<PresaleActivityVo> addPresaleActivity(PresaleActivityVo presaleActivityVo, Long spuId, Long shopId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        HashMap<String,Object> spuVo = goodsService.getSimpleSpuById(spuId).getData();
        Shop shop = shopService.getShopByShopId(shopId).getData();
        if(shop == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "店铺不存在");
        }
        if(spuVo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "对应的SPU不存在");
        }
        if(!((Long)(spuVo.get("shopId"))).equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不允许使用其他店铺的SPU");
        }

        if (presaleActivityDao.addActivity(po, spuId, shopId) == 1) {
            presaleActivityVo = new PresaleActivityVo(po);
            spuVo.remove("shopId");
            presaleActivityVo.goodsSpu = spuVo;
            presaleActivityVo.shop.put("id", shop.getId());
            presaleActivityVo.shop.put("name", shop.getName());

            return new ReturnObject(presaleActivityVo);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleActivityVo presaleActivityVo, Long shopId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        if (presaleActivityDao.updateActivity(po, id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject delPresaleActivity(long id, long shopId) {
        PresaleActivityPo presaleActivityPo = presaleActivityDao.getActivityById(id);
        if(presaleActivityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "预售活动不存在");
        }
        if(presaleActivityPo.getShopId() != shopId){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "当前预售活动不属于您的店铺");
        }

        if (presaleActivityDao.delActivity(id)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法删除预售活动");
        }
    }
    //endregion

    //region 团购活动部分
    public ReturnObject grouponActivityStatus() {
        return new ReturnObject(GrouponActivity.GrouponStatus.values());
    }

    @Transactional
    public ReturnObject getGrouponActivities(ActivityFinderVo activityFinderVo, boolean all) {
        List<GrouponActivityPo> grouponList;
        PageInfo info;
        if (activityFinderVo.getSpuId() != null && !all) {
            info = grouponActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            info = grouponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        grouponList=info.getList();
        List<GrouponActivity> retList = grouponList.stream().map(e -> new GrouponActivity(e)).collect(Collectors.toList());
        info.setList(retList);
        Byte state=null;
        if(activityFinderVo.getTimeline()!=null) {
            switch (activityFinderVo.getTimeline()) {
                case 0:
                    state = GrouponActivity.GrouponStatus.NEW.getCode().byteValue();
                    break;
                case 1:
                    state = GrouponActivity.GrouponStatus.NEW.getCode().byteValue();
                    break;
                case 2:
                    state = GrouponActivity.GrouponStatus.RUNNING.getCode().byteValue();
                    break;
                case 3:
                    state = GrouponActivity.GrouponStatus.CANCELED.getCode().byteValue();
                    break;
                default:
                    state = null;
                    break;
            }
        }
        List<Long> changList=new ArrayList<>();
        for(GrouponActivityPo po : grouponList){
            if(!po.getState().equals(state)){
                changList.add(po.getId());
            }
        }
        if(state!=null)
        grouponActivityDao.setGroupsState(state,changList);
        return new ReturnObject(info);
    }

    public ReturnObject getGrouponActivitiesByAdmin(ActivityFinderVo vo){
        List<GrouponActivityPo> grouponList;
        PageInfo info;
        info=grouponActivityDao.getGrouponsByAdmin(vo.getShopId(),vo.getState(),vo.getSpuId(),vo.getBeginTime(),vo.getEndTime(),vo.getPage(),vo.getPageSize());
        grouponList=info.getList();
        List<GrouponActivity> retList = grouponList.stream().map(e -> new GrouponActivity(e)).collect(Collectors.toList());
        info.setList(retList);
        return new ReturnObject(info);
    }

    public ReturnObject getGrouponBySpuIdAdmin(ActivityFinderVo vo){
        List<GrouponActivity> grouponActivities;
        grouponActivities=grouponActivityDao.getAllGrouponsBySpuAmdin(vo.getSpuId(),vo.getShopId(),vo.getState());
        return new ReturnObject(grouponActivities);
    }

    public ReturnObject<GrouponActivityVo> addGrouponActivity(GrouponActivityVo grouponActivityVo, long spuId, long shopId) {
        if(shopId!=0){
            ReturnObject shopIdret=goodsService.getShopIdBySpuId(spuId);
            if(shopIdret.getCode()!=ResponseCode.OK){
                return shopIdret;
            }
            if(((Long)shopIdret.getData()).equals(shopId)){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        GrouponActivityPo po = grouponActivityVo.createPo();
        if (grouponActivityDao.addActivity(po, spuId, shopId)) {
            ReturnObject spuRet=goodsService.getSimpleSpuById(spuId);
            Map<String,Object> spu = (Map<String, Object>)spuRet.getData();
            ReturnObject shopRet = shopService.getShopByShopId(shopId);
            Map<String,Object> shopMap=new HashMap<>();
            if(shopRet.getCode()==ResponseCode.OK){
                Shop shop=(Shop) shopRet.getData();
                shopMap.put("id",shop.getId());
                shopMap.put("name",shop.getName());
            }
            var vo=new GrouponActivityVo(po);
            vo.setGoodsSpu(spu);
            vo.setShop(shopMap);
            return new ReturnObject(vo);
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject<GrouponActivityVo> modifyGrouponActivity(Long id, GrouponActivityVo grouponActivityVo, long shopId) {
        ReturnObject ret=goodsService.getShopIdBySpuId(id);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        if(!((Long)ret.getData()).equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        if (grouponActivityDao.updateActivity(grouponActivityVo.createPo(), id)) {
            return new ReturnObject(grouponActivityVo);
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject<?> delGrouponActivity(long id,long shopId) {
        ReturnObject<Long> ret=goodsService.getShopIdBySpuId(id);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        if(!ret.getData().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
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
        var spu = goodsService.getSpuById(spuId).getData();
        if(spu == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "SPU ID不存在");
        } /*else if((long)spu.getShop().get("id")!= shopId) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "SPU 不属于你的店铺");
        }*/

        Long insertId = couponActivityDao.addSpuToActivity(activityId, spuId);
        if(insertId != null){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject removeSPUFromCouponActivity(long spuId, long shopId, long activityId){
        var spu = goodsService.getSpuById(spuId).getData();
        if(spu == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "SPU ID不存在");
        } /*else if((long)spu.getShop().get("id") != shopId) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "SPU 不属于你的店铺");
        }*/

        boolean success = couponActivityDao.removeSpuFromActivity(activityId, spuId);
        if(success){
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR, "无法执行删除程序");
        }
    }

    public ReturnObject getSPUInCouponActivity(long activityId,int page,int pageSize){
        PageInfo<CouponSPUPo> couponSPUPoPageInfo = couponActivityDao.getSPUsInActivity(activityId, page, pageSize);
        List<HashMap<String,Object>> simpleSpuList = new ArrayList();
        for(CouponSPUPo couponSPUPo:couponSPUPoPageInfo.getList()){
            ReturnObject<SpuRetVo> ret = goodsService.getSpuById(couponSPUPo.getId());
            HashMap<String,Object> hm = new HashMap<String,Object>(){
                {
                    put("id", ret.getData().getId());
                    put("name", ret.getData().getName());
                }
            };
            simpleSpuList.add(hm);
        }
        PageInfo<HashMap<String,Object>> ret = new PageInfo<>(simpleSpuList);
        ret.setPageNum(couponSPUPoPageInfo.getPageNum());
        ret.setPages(couponSPUPoPageInfo.getPages());
        ret.setTotal(couponSPUPoPageInfo.getTotal());
        ret.setPageSize(couponSPUPoPageInfo.getPageSize());

        return new ReturnObject(ret);
    }


    //endregion

    //region 优惠券部分

    /**
     * 用户获取自己的优惠券
     * @param userId
     * @param state 优惠券状态
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject getCouponList(Long userId, Byte state, Integer page, Integer pageSize) {
        PageInfo<CouponPo> couponPoList = couponDao.getCouponList(userId, state, page, pageSize);

        List<HashMap<String,Object>> couponList = new ArrayList<>();
        for(CouponPo couponPo:couponPoList.getList()){
            HashMap<String, Object> singleCoupon = new HashMap<>();
            HashMap<String, Object> singleActivity = new HashMap<>();

            var activityPo = couponActivityDao.getActivityById(couponPo.getActivityId());

            singleCoupon.put("id", couponPo.getId());
            singleCoupon.put("activity", new ActivityInCouponVo(activityPo));
            singleCoupon.put("name",couponPo.getName());
            singleCoupon.put("couponSn",couponPo.getCouponSn());

            couponList.add(singleCoupon);
        }

        PageInfo ret = new PageInfo(couponList);
        ret.setPageNum(couponPoList.getPageNum());
        ret.setPages(couponPoList.getPages());
        ret.setPageSize(couponPoList.getPageSize());
        ret.setTotal(couponPoList.getTotal());

        return new ReturnObject(ret);
    }

    /**
     * 用户使用优惠券
     * @param couponId 优惠券ID
     * @param userId 用户ID
     * @return
     */
    public ReturnObject useCoupon(Long couponId, Long userId) {
        CouponPo couponToUse = couponDao.getCoupon(couponId);
        if(couponToUse == null){
            new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "您使用的优惠券不存在");
        }else if(couponToUse.getCustomerId() != userId){
            new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "不可使用不属于您的优惠券");
        }else if(couponToUse.getBeginTime().isAfter(LocalDateTime.now())
                ||couponToUse.getEndTime().isBefore(LocalDateTime.now())){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券过期或未到使用时间");
        }else if(couponToUse.getState() != Coupon.CouponStatus.AVAILABLE.getCode()){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券状态不可用");
        }

        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.USED.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 用户删除优惠券
     * @param couponId
     * @param userId
     * @return
     */
    public ReturnObject delCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.EXPIRED.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 系统退还优惠券
     * @param couponId
     * @param userId
     * @return
     */
    public ReturnObject refundCoupon(Long couponId, Long userId) {
        CouponPo po = new CouponPo();
        po.setState(Coupon.CouponStatus.AVAILABLE.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 用户领取优惠券
     * @param activityId 优惠活动ID
     * @param userId 用户ID
     * @return
     */
    public ReturnObject claimCoupon(Long activityId, Long userId) {
        CouponActivityPo couponActivityPo = couponActivityDao.getActivityById(activityId);
        if (couponActivityPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动不存在");
        } else if (couponActivityPo.getBeginTime().isAfter(LocalDateTime.now())
                || couponActivityPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动已结束或者未开始");
        } else if (couponActivityPo.getState() != CouponActivity.CouponStatus.RUNNING.getCode()) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动状态不可用");
        }

        var couponPoList = couponDao.getCouponList(userId, null, 1, 1);
        if (couponPoList.getList().size() != 0) {
            return new ReturnObject(ResponseCode.COUPON_FINISH, "你已经领取过本活动的优惠券了");
        }

        CouponPo po = new CouponPo();

        if (couponActivityPo.getQuantitiyType() == 0) {
            // 每人限领取一定数量，生成quantity张
            for (int i = 0; i < couponActivityPo.getQuantity(); ++i) {

                if (couponActivityPo.getValidTerm() == 0) {
                    po.setBeginTime(couponActivityPo.getCouponTime());
                    po.setEndTime(couponActivityPo.getEndTime());
                } else if (couponActivityPo.getValidTerm() > 0) {
                    po.setBeginTime(LocalDateTime.now());
                    po.setEndTime(
                            LocalDateTime.now().plusDays(couponActivityPo.getValidTerm()).isAfter(couponActivityPo.getEndTime()) ?
                                    couponActivityPo.getEndTime() : LocalDateTime.now().plusDays(couponActivityPo.getValidTerm())
                    );
                }
                couponDao.addCoupon(po, activityId, userId);
            }
        } else if (couponActivityPo.getQuantitiyType() == 1) {
            // 总数控制，每人领取一张
            if (couponActivityPo.getQuantity() > 0) {
                couponActivityPo.setQuantity(couponActivityPo.getQuantity() - 1);
                couponActivityDao.updateActivity(couponActivityPo, couponActivityPo.getId());
            } else {
                return new ReturnObject(ResponseCode.COUPON_FINISH, "优惠券已售罄");
            }

            if (couponActivityPo.getValidTerm() == 0) {
                po.setBeginTime(couponActivityPo.getCouponTime());
                po.setEndTime(couponActivityPo.getEndTime());
            } else if (couponActivityPo.getValidTerm() > 0) {
                po.setBeginTime(LocalDateTime.now());
                po.setEndTime(
                        LocalDateTime.now().plusDays(couponActivityPo.getValidTerm()).isAfter(couponActivityPo.getEndTime()) ?
                                couponActivityPo.getEndTime() : LocalDateTime.now().plusDays(couponActivityPo.getValidTerm())
                );
            }
            couponDao.addCoupon(po, activityId, userId);
        }
        ActivityInCouponVo activityInCouponVo = new ActivityInCouponVo(couponActivityPo);
        CouponVo couponVo = new CouponVo(po,activityInCouponVo);
        return new ReturnObject(couponVo);
    }
    //endregion
}
