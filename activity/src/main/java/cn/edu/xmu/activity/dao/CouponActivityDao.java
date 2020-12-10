package cn.edu.xmu.activity.dao;

import cn.edu.xmu.activity.mapper.CouponActivityPoMapper;
import cn.edu.xmu.activity.mapper.CouponSKUPoMapper;
import cn.edu.xmu.activity.model.Timeline;
import cn.edu.xmu.activity.model.bo.CouponActivity;
import cn.edu.xmu.activity.model.po.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CouponActivityDao {

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;
    @Autowired
    CouponSKUPoMapper couponSKUPoMapper;

    @Autowired
    RedisTemplate redisTemplate;

    public Boolean hasSameSKU(long skuId){
        CouponSKUPoExample example = new CouponSKUPoExample();
        CouponSKUPoExample.Criteria criteria = example.createCriteria();
        criteria.andSkuIdEqualTo(skuId);
        List<CouponSKUPo> couponSKUPo = couponSKUPoMapper.selectByExample(example);
        if(couponSKUPo.isEmpty()){
            return false;
        }

        CouponActivityPoExample activityExample = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria activityCriteria = activityExample.createCriteria();
        activityCriteria.andIdIn(
                couponSKUPo.stream().map(CouponSKUPo::getActivityId).collect(Collectors.toList()));
        var x = couponActivityPoMapper.selectByExample(activityExample).stream().filter(z -> !z.getState().equals(CouponActivity.CouponStatus.DELETE.getCode()));
        return x.count() > 0;
    }

    public List<CouponActivityPo> getActivitiesBySPUId(long id){
        CouponSKUPoExample example = new CouponSKUPoExample();
        CouponSKUPoExample.Criteria criteria = example.createCriteria();
        criteria.andSkuIdEqualTo(id);

        List<CouponSKUPo> couponSPUList = couponSKUPoMapper.selectByExample(example);
        List<CouponActivityPo> activities = new ArrayList<>();
        for(CouponSKUPo couponSPU: couponSPUList){
            activities.add(couponActivityPoMapper.selectByPrimaryKey(couponSPU.getActivityId()));
        }
        return activities;
    }

    public List<CouponSKUPo> getSKUsInActivity(long activityId){
        CouponSKUPoExample couponSPUPoExample = new CouponSKUPoExample();
        var criteria = couponSPUPoExample.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        List<CouponSKUPo> list = couponSKUPoMapper.selectByExample(couponSPUPoExample);
        return list;
    }

    /**
     * 获取已经被取消的优惠活动
     * @param page
     * @param pageSize
     * @param shopId 店铺ID
     * @return
     */
    public PageInfo<CouponActivityPo> getInvalidActivities(int page, int pageSize, long shopId) {
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        criteria.andStateEqualTo(CouponActivity.CouponStatus.DELETE.getCode());

        List<CouponActivityPo> activityPoList = couponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(activityPoList);
    }

    /**
     * 获取有效的优惠活动
     * @param page
     * @param pageSize
     * @param shopId
     * @param timeline 时间判断
     * @return
     */
    public PageInfo<CouponActivityPo> getEffectiveActivities(int page, int pageSize, Long shopId, Byte timeline){
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();

        if (timeline != null) {
            if (timeline == Timeline.PENDING.ordinal()) {
                /* 获取未开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == Timeline.TOMORROW.ordinal()) {
                /* 获取明天开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                criteria.andBeginTimeLessThan(LocalDateTime.now().plusDays(1));
            } else if(timeline == Timeline.RUNNING.ordinal()) {
                /* 获取正在进行中的活动 */
                criteria.andBeginTimeLessThan(LocalDateTime.now());
                criteria.andEndTimeGreaterThan(LocalDateTime.now());
            } else if(timeline == Timeline.FINISHED.ordinal()) {
                criteria.andEndTimeLessThan(LocalDateTime.now());
            }
        }
        if(shopId != null){
            criteria.andShopIdEqualTo(shopId);
        }
        criteria.andStateEqualTo(CouponActivity.CouponStatus.OFFLINE.getCode());

        List<CouponActivityPo> activityPoList = couponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(activityPoList);
    }

    public boolean changeActivityStatus(long id, Byte status){
        redisTemplate.boundHashOps("coupon-activity").delete(id);
        CouponActivityPo po = new CouponActivityPo();
        po.setId(id);
        po.setState(status);
        return couponActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }

    public boolean addActivity(CouponActivityPo po, long shopId){
        po.setShopId(shopId);
        po.setGmtCreate(LocalDateTime.now());
        po.setCreatedBy(shopId);
        return couponActivityPoMapper.insert(po) == 1;
    }

    public boolean delActivity(long id){
        redisTemplate.boundHashOps("coupon-activity").delete(id);
        return couponActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(CouponActivityPo po, long id, long shopId) {
        po.setId(id);
        po.setModiBy(shopId);
        po.setGmtModified(LocalDateTime.now());
        return couponActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }

    /**
     * 获取优惠活动详情（加了缓存）
     * @param id
     * @return
     */
    public CouponActivityPo getActivityById(long id){
        var x = redisTemplate.boundHashOps("coupon-activity").get(id);
        if(x != null){
            return (CouponActivityPo)x;
        }else{
            var res = couponActivityPoMapper.selectByPrimaryKey(id);
            if (res != null){
                redisTemplate.boundHashOps("coupon-activity").put(id, res);
            }
            return res;
        }
    }

    public Long addSpuToActivity(long activityId,long skuId){
        CouponSKUPo po = new CouponSKUPo();
        po.setActivityId(activityId);
        po.setSkuId(skuId);
        couponSKUPoMapper.insert(po);
        return po.getId();
    }

    public CouponSKUPo getCouponSPUPoById(long id){
        return couponSKUPoMapper.selectByPrimaryKey(id);
    }

    public boolean removeSkuFromActivity(long id){
        return couponSKUPoMapper.deleteByPrimaryKey(id)==1;
    }

    public PageInfo<CouponSKUPo> getSKUsInActivity(long activityID, int page, int pageSize){
        PageHelper.startPage(page,pageSize);

        CouponSKUPoExample couponSPUPoExample = new CouponSKUPoExample();
        var criteria = couponSPUPoExample.createCriteria();
        criteria.andActivityIdEqualTo(activityID);
        List<CouponSKUPo> list = couponSKUPoMapper.selectByExample(couponSPUPoExample);
        return PageInfo.of(list);
    }
}
