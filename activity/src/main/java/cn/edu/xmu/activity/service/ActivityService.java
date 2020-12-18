package cn.edu.xmu.activity.service;

import cn.edu.xmu.activity.dao.CouponActivityDao;
import cn.edu.xmu.activity.dao.CouponDao;
import cn.edu.xmu.activity.dao.GrouponActivityDao;
import cn.edu.xmu.activity.dao.PresaleActivityDao;
import cn.edu.xmu.activity.model.bo.*;
import cn.edu.xmu.activity.model.po.*;
import cn.edu.xmu.activity.model.vo.*;
import cn.edu.xmu.activity.service.dubbo.IUserService;
import cn.edu.xmu.activity.service.dubbo.UserDTO;
import cn.edu.xmu.activity.utility.MyPageHelper;
import cn.edu.xmu.goods.client.dubbo.ShopDTO;
import cn.edu.xmu.goods.client.dubbo.SkuDTO;
import cn.edu.xmu.goods.client.dubbo.SpuDTO;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.goods.client.IShopService;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import com.github.pagehelper.PageInfo;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ActivityService implements InitializingBean {
    @Autowired
    PresaleActivityDao presaleActivityDao;
    @Autowired
    GrouponActivityDao grouponActivityDao;
    @Autowired
    CouponActivityDao couponActivityDao;
    @Autowired
    CouponDao couponDao;

    private RedisTemplate redisTemplate;

    private RedisBloomFilter redisBloomFilter;

    @DubboReference(version = "0.0.1-SNAPSHOT",check = false)
    IGoodsService goodsService;
    @DubboReference(version = "0.0.1-SNAPSHOT",check = false)
    IShopService shopService;

    @Autowired
    IUserService userService;

    @Autowired
    public ActivityService(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
        redisBloomFilter =  new RedisBloomFilter<>(redisTemplate,
                new BloomFilterHelper<>(Funnels.stringFunnel(Charset.defaultCharset()), 100000, 0.03));
    }

    //region 预售活动部分
    public ReturnObject<PresaleActivity.PresaleStatus[]> getPresaleActivityStatus() {
        return new ReturnObject<>(PresaleActivity.PresaleStatus.values());
    }

    /**
     * 管理员查看SKU的预售活动
     * @param activityFinderVo
     * @return 预售活动的列表
     */
    public ReturnObject<List<PresaleActivityVo>> getAllPresaleActivities(ActivityFinderVo activityFinderVo) {
        List<PresaleActivityPo> presaleList;
        presaleList = presaleActivityDao.getAllActivityBySKUId(activityFinderVo.getState(),activityFinderVo.getSkuId());
        List<PresaleActivityVo> retList = presaleList.stream().map(PresaleActivityVo::new).collect(Collectors.toList());
        return new ReturnObject<>(retList);
    }

    /**
     * 用户查看预售活动
     * @param activityFinderVo
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getPresaleActivities(ActivityFinderVo activityFinderVo) {
        PageInfo<PresaleActivityPo> po;
        if (activityFinderVo.getSkuId() != null) {
            po = presaleActivityDao.getActivitiesBySKUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSkuId(), activityFinderVo.getTimeline());
        } else {
            po = presaleActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline());
        }

        List<PresaleActivityVo> retList = new ArrayList<>();
        for (PresaleActivityPo presaleActivityPo:po.getList()){
            var skuDTO = goodsService.getSku(presaleActivityPo.getGoodsSkuId());
            var shopDTO = goodsService.getShopBySKUId(presaleActivityPo.getGoodsSkuId());
            PresaleActivityVo presaleActivityVo = new PresaleActivityVo(presaleActivityPo);
            presaleActivityVo.shop.put("id", shopDTO.getId());
            presaleActivityVo.shop.put("name", shopDTO.getName());
            presaleActivityVo.goodsSku.put("id", skuDTO.getId());
            presaleActivityVo.goodsSku.put("name", skuDTO.getName());
            presaleActivityVo.goodsSku.put("skuSn", skuDTO.getSkuSn());
            presaleActivityVo.goodsSku.put("imageUrl", skuDTO.getImageUrl());
            presaleActivityVo.goodsSku.put("inventory", skuDTO.getInventory());
            presaleActivityVo.goodsSku.put("originalPrice", skuDTO.getOriginalPrice());
            presaleActivityVo.goodsSku.put("price", skuDTO.getPrice());
            presaleActivityVo.goodsSku.put("disable", skuDTO.getDisable());
            retList.add(presaleActivityVo);
        }

        PageInfo<PresaleActivityVo> ret = new PageInfo<>(retList);
        MyPageHelper.transferPageParams(po, ret);
        return new ReturnObject(ret);
    }



    public ReturnObject<PresaleActivityVo> addPresaleActivity(PresaleActivityVo presaleActivityVo, Long skuId, Long shopId) {
        PresaleActivityPo po = presaleActivityVo.createPo();
        SkuDTO skuDTO = goodsService.getSku(skuId);
        ShopDTO shopDTO = goodsService.getShopBySKUId(skuId);

        if(shopDTO == null || skuDTO == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "店铺或SKU不存在");
        }
        if(!shopDTO.getId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不允许使用其他店铺的SKU");
        }
        if(presaleActivityDao.getSameSKU(skuId)){
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW, "你已经添加了相同的SKU");
        }

        if (presaleActivityDao.addActivity(po, skuId, shopId) == 1) {
            presaleActivityVo = new PresaleActivityVo(po);
            presaleActivityVo.shop.put("id", shopDTO.getId());
            presaleActivityVo.shop.put("name", shopDTO.getName());
            presaleActivityVo.goodsSku.put("id", skuDTO.getId());
            presaleActivityVo.goodsSku.put("name", skuDTO.getName());
            presaleActivityVo.goodsSku.put("skuSn", skuDTO.getSkuSn());
            presaleActivityVo.goodsSku.put("imageUrl", skuDTO.getImageUrl());
            presaleActivityVo.goodsSku.put("inventory", skuDTO.getInventory());
            presaleActivityVo.goodsSku.put("originalPrice", skuDTO.getOriginalPrice());
            presaleActivityVo.goodsSku.put("price", skuDTO.getPrice());
            presaleActivityVo.goodsSku.put("disable", skuDTO.getDisable());

            return new ReturnObject<>(presaleActivityVo);
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    public ReturnObject modifyPresaleActivity(Long id, PresaleActivityVo presaleActivityVo, Long shopId) {
        var activity = presaleActivityDao.getActivityById(id);
        if(activity == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!activity.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        if(activity.getBeginTime().isBefore(LocalDateTime.now())){
            return new ReturnObject<>(ResponseCode.PRESALE_STATENOTALLOW,"仅可修改未开始的预售活动");
        }

        PresaleActivityPo po = presaleActivityVo.createPo();
        if (presaleActivityDao.updateActivity(po, id)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    public ReturnObject modifyPresaleActivity(long id, long shopId, Byte state) {
        PresaleActivityPo presaleActivityPo = presaleActivityDao.getActivityById(id);
        if(presaleActivityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "预售活动不存在");
        }
        if(presaleActivityPo.getShopId() != shopId){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "当前预售活动不属于您的店铺");
        }

        if (presaleActivityDao.changeActivityStatus(id, state)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法删除预售活动");
        }
    }
    //endregion

    //region 团购活动部分
    public ReturnObject<GrouponActivity.GrouponStatus[]> grouponActivityStatus() {
        return new ReturnObject<>(GrouponActivity.GrouponStatus.values());
    }

    /**
     * 获取团购活动列表
     * @param activityFinderVo
     * @param all
     * @return
     */
    @Transactional
    public ReturnObject<PageInfo<VoObject>> getGrouponActivities(ActivityFinderVo activityFinderVo, boolean all) {
        PageInfo<GrouponActivityPo> info;
        if (activityFinderVo.getSpuId() != null && !all) {
            // 普通用户根据SPU查询团购活动
            info = grouponActivityDao.getActivitiesBySPUId(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getSpuId(), activityFinderVo.getTimeline());
        } else {
            info = grouponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline(), activityFinderVo.getSpuId(), all);
        }
        List<GrouponActivityPo> grouponList = info.getList();
        List<VoObject> retList = grouponList.stream().map(GrouponActivity::new).collect(Collectors.toList());

        PageInfo<VoObject> ret = new PageInfo<>(retList);
        MyPageHelper.transferPageParams(info, ret);

        return new ReturnObject<>(ret);
    }

    /**
     * 管理员获取团购活动列表
     * @param vo
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getGrouponActivitiesByAdmin(ActivityFinderVo vo){
        PageInfo<GrouponActivityPo> info = grouponActivityDao.getGrouponsByAdmin(vo.getShopId(),vo.getState(),vo.getSpuId(),vo.getBeginTime(),vo.getEndTime(),vo.getPage(),vo.getPageSize());
        List<GrouponActivityPo> grouponList=info.getList();
        List<VoObject> retList = grouponList.stream().map(GrouponActivity::new).collect(Collectors.toList());
        PageInfo<VoObject> ret = new PageInfo<>(retList);

        MyPageHelper.transferPageParams(info, ret);
        return new ReturnObject<>(ret);
    }

    /**
     * 管理员获取单个团购活动
     * @param vo
     * @return
     */
    public ReturnObject<List> getGrouponBySpuIdAdmin(ActivityFinderVo vo){
        List<GrouponActivity> grouponActivities;
        grouponActivities=grouponActivityDao.getAllGrouponsBySpuAdmin(vo.getSpuId(),vo.getShopId(),vo.getState());
        return new ReturnObject<>(grouponActivities);
    }

    /**
     * 新增团购活动
     * @param grouponActivityVo
     * @param spuId
     * @param shopId
     * @return
     */
    public ReturnObject<GrouponActivityVo> addGrouponActivity(GrouponActivityVo grouponActivityVo, long spuId, long shopId) {
        SpuDTO spuDTO = goodsService.getSimpleSpuById(spuId);
        if (spuDTO == null) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "指定的SPU不存在");
        }
        if(spuDTO.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "指定的SPU不属于当前商铺");
        }

        if(grouponActivityDao.hasSameSpu(spuId)){
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW, "您已经添加了这个SPU");
        }

        GrouponActivityPo po = grouponActivityVo.createPo();
        if (grouponActivityDao.addActivity(po, spuId, shopId)) {
            ShopDTO shopDTO = shopService.getShopById(shopId);
            Map<String,Object> shopMap=new HashMap<>();
            shopMap.put("id", shopDTO.getId());
            shopMap.put("name", shopDTO.getName());
            GrouponActivityVo vo = new GrouponActivityVo(po);
            vo.setGoodsSpu(new SpuInActivityVo(spuDTO));
            vo.setShop(shopMap);
            return new ReturnObject<>(vo);
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    /**
     * 修改团购活动
     * @param id
     * @param grouponActivityVo
     * @param shopId
     * @return
     */
    public ReturnObject<GrouponActivityVo> modifyGrouponActivity(Long id, GrouponActivityVo grouponActivityVo, long shopId) {
        GrouponActivityPo activityPo = grouponActivityDao.getActivityById(id);
        if(activityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "团购活动不存在");
        }
        if(!activityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不能修改其他的店铺的团购活动");
        }
        if(activityPo.getBeginTime().isBefore(LocalDateTime.now())){
            return new ReturnObject<>(ResponseCode.GROUPON_STATENOTALLOW, "不能修改已经开始的活动");
        }

        if (grouponActivityDao.updateActivity(grouponActivityVo.createPo(), id)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 修改团购活动状态
     * @param id
     * @param shopId
     * @return
     */
    public ReturnObject<?> modifyGrouponActivity(long id, long shopId, Byte status) {
        GrouponActivityPo activityPo = grouponActivityDao.getActivityById(id);
        if(activityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "团购活动不存在");
        }
        if(!activityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "不能修改其他的店铺的团购活动");
        }

        GrouponActivityPo po = new GrouponActivityPo();
        po.setState(status);
        if (grouponActivityDao.updateActivity(po, id)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }
    //endregion

    //region 优惠活动部分

    /**
     * 获取优惠活动的的详细信息
     * @param activityId
     * @param shopId
     * @return 返回优惠活动，店铺，创建者，修改者
     */
    public ReturnObject<CouponActivityVo> getCouponActivity(long activityId, long shopId){
        CouponActivityPo activityPo = couponActivityDao.getActivityById(activityId);
        ShopDTO shopDTO = shopService.getShopById(shopId);
        if (activityPo == null || shopDTO == null
                || activityPo.getState().equals(CouponActivity.CouponStatus.DELETE.getCode())){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "活动或对应店铺不存在");
        }
        if(!activityPo.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "活动不是自己店铺的活动");
        }

        UserDTO createdBy = userService.getUserById(activityPo.getCreatedBy());
        UserDTO modiBy = userService.getUserById(activityPo.getModiBy());

        CouponActivityVo couponActivityVo = new CouponActivityVo(activityPo);
        couponActivityVo.shop.put("id", shopDTO.getId());
        couponActivityVo.shop.put("name", shopDTO.getName());
        couponActivityVo.createdBy = new UserVo(createdBy);
        couponActivityVo.ModiBy = new UserVo(modiBy);

        return new ReturnObject<>(couponActivityVo);
    }

    /**
     *
     * @return 优惠活动的所有状态（对前台而言）
     */
    public ReturnObject<CouponActivity.CouponStatus[]> getCouponActivityStatus() {
        return new ReturnObject<>(CouponActivity.CouponStatus.values());
    }

    public ReturnObject<Coupon.CouponStatus[]> getCouponStatus() {
        return new ReturnObject<>(Coupon.CouponStatus.values());
    }

    /**
     *查看优惠活动列表，可以查看上线的也可以查看下线的
     * @param activityFinderVo
     * @return 带分页的优惠活动列表
     */
    public ReturnObject<PageInfo<VoObject>> getCouponActivities(ActivityFinderVo activityFinderVo) {
        PageInfo<CouponActivityPo> couponList;
        if (!activityFinderVo.getState().equals(CouponActivity.CouponStatus.ONLINE.getCode())) {
            couponList = couponActivityDao.getInvalidActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId());
        } else {
            couponList = couponActivityDao.getEffectiveActivities(
                    activityFinderVo.getPage(), activityFinderVo.getPageSize(), activityFinderVo.getShopId(), activityFinderVo.getTimeline());
        }

        List<VoObject> list = couponList.getList().stream().map(CouponActivityVo::new).collect(Collectors.toList());
        PageInfo<VoObject> ret = new PageInfo<>(list);
        ret.setPages(couponList.getPages());
        ret.setPageNum(couponList.getPageNum());
        ret.setPageSize(couponList.getPageSize());
        ret.setTotal(couponList.getTotal());

        return new ReturnObject<>(ret);
    }

    /**
     * 新增优惠活动
     * @param couponActivityVo
     * @param shopId
     * @param userId
     * @return
     */
    public ReturnObject<CouponActivityVo> addCouponActivity(CouponActivityVo couponActivityVo, Long shopId, Long userId) {
        CouponActivityPo po = couponActivityVo.createPo();
        po.setCreatedBy(userId);
        po.setState(CouponActivity.CouponStatus.OFFLINE.getCode());

        UserDTO userDTO = userService.getUserById(userId);
        if (couponActivityDao.addActivity(po, shopId)) {
            var ret = new CouponActivityVo(po);
            ret.createdBy = new UserVo(userDTO);
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
        }
    }

    /**
     * 修改优惠活动
     * @param id
     * @param couponActivityVo
     * @param shopId
     * @return
     */
    public ReturnObject modifyCouponActivity(Long id, CouponActivityVo couponActivityVo, long shopId) {
        var activity = couponActivityDao.getActivityById(id);
        if(activity == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动不存在");
        }
        if(!activity.getShopId().equals(shopId)){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "这个优惠活动不属于你");
        }
        if(!activity.getState().equals(CouponActivity.CouponStatus.OFFLINE.getCode())){
            return new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW, "不允许修改未下线的优惠活动");
        }
//        if(activity.getBeginTime().isAfter(LocalDateTime.now())){
//            return new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW, "不允许修改已经开始的优惠活动");
//        }
//        if(activity.getState().equals(CouponActivity.CouponStatus.DELETE.getCode())){
//            return new ReturnObject<>(ResponseCode.COUPONACT_STATENOTALLOW, "不允许修改已经取消的优惠活动");
//        }
        // 如果修改为下线，那需要取消优惠券
        if(couponActivityVo.getState() != null && couponActivityVo.getState().equals(CouponActivity.CouponStatus.DELETE.getCode())){
            couponDao.cancelCoupon(id);
        }

        if (couponActivityDao.updateActivity(couponActivityVo.createPo(), id, shopId)) {
            return new ReturnObject<>();
        } else {
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "未知错误，请联系管理员");
        }
    }


    @Deprecated
    public ReturnObject modifyCouponActivityStatus(Long id, CouponActivity.CouponStatus status){
        if (couponActivityDao.changeActivityStatus(id, status.getCode())) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    @Deprecated
    public ReturnObject delCouponActivity(long id) {
        if (couponActivityDao.delActivity(id)) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    /**
     * 将SKU加入到优惠活动之中
     * @param skuIds
     * @param shopId
     * @param activityId
     * @return
     */
    public ReturnObject addSKUToCouponActivity(List<Long> skuIds, long shopId, long activityId){
        CouponActivityPo couponActivityPo = couponActivityDao.getActivityById(activityId);
        if(!couponActivityPo.getState().equals(CouponActivity.CouponStatus.OFFLINE.getCode())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "只允许修改下线的活动");
        }
        if (!couponActivityPo.getShopId().equals(shopId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "不允许修改其他店铺的活动");
        }

        for(Long skuId:skuIds){
            SkuDTO skuDTO = goodsService.getSku(skuId);
            ShopDTO shopDTO = goodsService.getShopBySKUId(skuId);
            if(skuDTO == null){
                return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "SKU ID不存在");
            }
            if(!shopDTO.getId().equals(shopId)) {
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "SPU 不属于你的店铺");
            }
            if(couponActivityDao.hasSameSKU(skuId)){
                return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "本SKU已经处于一个尚未结束的优惠活动中");
            }
            Long insertId = couponActivityDao.addSkuToActivity(activityId, skuId);
            if(insertId == null){
                return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "无法执行插入程序");
            }
        }
        return new ReturnObject<>();
    }

    /**
     * 将SKU从优惠活动中移除
     * @param id 活动-商品对应表的ID
     * @param shopId 店铺ID
     * @return
     */
    public ReturnObject removeSKUFromCouponActivity(long id, long shopId){
        CouponSKUPo couponSPUPo = couponActivityDao.getCouponSPUPoById(id);
        if(couponSPUPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "该商品不在该活动中");
        }

        var activityPo = couponActivityDao.getActivityById(couponSPUPo.getActivityId());
        if(activityPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "活动不存在");
        }
        if(activityPo.getShopId() != shopId) {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_OUTSCOPE, "活动不属于你的店铺");
        }

        boolean success = couponActivityDao.removeSkuFromActivity(id);
        if(!success){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }else{
            return new ReturnObject<>();
        }

    }

    /**
     * 获取优惠活动中的SKU
     * @param activityId
     * @param page
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> getSKUInCouponActivity(long activityId, int page, int pageSize){
        CouponActivityPo po = couponActivityDao.getActivityById(activityId);
        if(po == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        PageInfo<CouponSKUPo> couponSPUPoPageInfo = couponActivityDao.getSKUsInActivity(activityId, page, pageSize);
        List<VoObject> simpleSkuList = new ArrayList<>();
        for(CouponSKUPo couponSPUPo:couponSPUPoPageInfo.getList()){
            log.debug(couponSPUPo.getSkuId().toString());
            SkuDTO skuDTO = goodsService.getSku(couponSPUPo.getSkuId());
            log.debug(skuDTO.toString());
            simpleSkuList.add(new SKUInActivityVo(skuDTO));
        }

        PageInfo<VoObject> ret = new PageInfo<>(simpleSkuList);
        ret.setPageNum(couponSPUPoPageInfo.getPageNum());
        ret.setPages(couponSPUPoPageInfo.getPages());
        ret.setTotal(couponSPUPoPageInfo.getTotal());
        ret.setPageSize(couponSPUPoPageInfo.getPageSize());

        return new ReturnObject<>(ret);
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
    public ReturnObject<PageInfo<VoObject>> getCouponList(Long userId, Byte state, Integer page, Integer pageSize) {
        PageInfo<CouponPo> couponPoList = couponDao.getCouponList(userId, state, page, pageSize);

        List<VoObject> couponList = new ArrayList<>();
        /* 获取每个优惠券所参加的活动 */
        for(CouponPo couponPo:couponPoList.getList()){
            var activityPo = couponActivityDao.getActivityById(couponPo.getActivityId());
            SingleCouponVo singleCouponVo =new SingleCouponVo(activityPo, couponPo);
            couponList.add(singleCouponVo);
        }

        PageInfo<VoObject> ret = new PageInfo<>(couponList);
        ret.setPageNum(couponPoList.getPageNum());
        ret.setPages(couponPoList.getPages());
        ret.setPageSize(couponPoList.getPageSize());
        ret.setTotal(couponPoList.getTotal());

        return new ReturnObject<>(ret);
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
        }
        if(couponToUse.getCustomerId() != userId){
            new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE, "不可使用不属于您的优惠券");
        }
        if(couponToUse.getBeginTime().isAfter(LocalDateTime.now())
                ||couponToUse.getEndTime().isBefore(LocalDateTime.now())){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券过期或未到使用时间");
        }
        if(couponToUse.getState() != Coupon.CouponStatus.NORMAL.getCode()){
            new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠券状态不可用");
        }

        CouponPo po = new CouponPo();
        po.setId(couponToUse.getId());
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
    @Deprecated
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
        po.setState(Coupon.CouponStatus.NORMAL.getCode());

        if (couponDao.modifyCoupon(po) == 1) {
            return new ReturnObject();
        } else {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
    }

    public ReturnObject claimCouponQuickly(Long activityId, Long userId){
        /* 从Redis里面获取活动数据，验证优惠活动的有效性 */
        CouponActivityPo couponActivityPo = couponActivityDao.getActivityById(activityId);
        if (couponActivityPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动不存在");
        } else if (couponActivityPo.getCouponTime().isAfter(LocalDateTime.now())
                || couponActivityPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动已结束或者未开始");
        } else if (!couponActivityPo.getState().equals(CouponActivity.CouponStatus.ONLINE.getCode())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动状态不可用");
        }
        ActivityInCouponVo activityInCouponVo = new ActivityInCouponVo(couponActivityPo);
        /* 从布隆过滤器里面查看用户是否已经领取了此优惠券 */
        if(redisBloomFilter.includeByBloomFilter("Claimed" + activityId.toString(), activityId.toString() + userId.toString())){
            return new ReturnObject(ResponseCode.COUPON_FINISH, "你已经领取过本活动的优惠券了");
        }
        /* 构造优惠券对象 */
        List<CouponVo> retList = new ArrayList<>();
        CouponPo po = new CouponPo();

        po.setCouponSn(Common.genSeqNum());
        /* 异步要求RocketMQ写入 */
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
                retList.add(new CouponVo(po,activityInCouponVo));
            }
        } else if (couponActivityPo.getQuantitiyType() == 1) {
            // 总数控制，每人领取一张
            Long res = redisTemplate.boundValueOps("CouponCount" + couponActivityPo.getId().toString()).decrement();
            if (res == null){
                new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR, "找不到优惠券数量");
            }else if(res < 0){
                return new ReturnObject(ResponseCode.COUPON_FINISH, "优惠券已售罄");
            }

            redisBloomFilter.addByBloomFilter("Claimed" + activityId.toString(), activityId.toString() + userId.toString());
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
            retList.add(new CouponVo(po,activityInCouponVo));
        }

        /* 构造返回值 */
        CouponVo couponVo = new CouponVo(po,activityInCouponVo);
        return new ReturnObject(couponVo);
    }

    /**
     * 用户领取优惠券
     * @param activityId 优惠活动ID
     * @param userId 用户ID
     * @return
     */
    public ReturnObject claimCoupon(Long activityId, Long userId) {
        /* 验证优惠活动的有效性 */
        CouponActivityPo couponActivityPo = couponActivityDao.getActivityById(activityId);
        if (couponActivityPo == null) {
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST, "优惠活动不存在");
        } else if (couponActivityPo.getBeginTime().isAfter(LocalDateTime.now())
                || couponActivityPo.getEndTime().isBefore(LocalDateTime.now())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动已结束或者未开始");
        } else if (!couponActivityPo.getState().equals(CouponActivity.CouponStatus.OFFLINE.getCode())) {
            return new ReturnObject(ResponseCode.COUPONACT_STATENOTALLOW, "优惠活动状态不可用");
        }

        /* 保证用户没有重复领取优惠券 */
        var couponPoList = couponDao.getCouponList(userId, null, 1, 1);
        if (couponPoList.getList().size() != 0) {
            return new ReturnObject(ResponseCode.COUPON_FINISH, "你已经领取过本活动的优惠券了");
        }

        CouponPo po = new CouponPo();

        po.setCouponSn(Common.genSeqNum());
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
                couponActivityDao.updateActivity(couponActivityPo, couponActivityPo.getId(),couponActivityPo.getShopId());
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

    // 初始化布隆过滤器和Redis
    @Override
    public void afterPropertiesSet() throws Exception {

    }
    //endregion
}
