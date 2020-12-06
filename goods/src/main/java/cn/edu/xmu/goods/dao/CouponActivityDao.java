package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CouponActivityPoMapper;
import cn.edu.xmu.goods.mapper.CouponSPUPoMapper;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.ooad.model.VoObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Repository
public class CouponActivityDao {

    @Autowired
    CouponActivityPoMapper couponActivityPoMapper;
    @Autowired
    CouponSPUPoMapper couponSPUPoMapper;

    public List<CouponActivityPo> getActivitiesBySPUId(long id){
        CouponSPUPoExample example = new CouponSPUPoExample();
        CouponSPUPoExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(id);

        List<CouponSPUPo> couponSPUList = couponSPUPoMapper.selectByExample(example);
        List<CouponActivityPo> activities = new ArrayList<>();
        for(CouponSPUPo couponSPU: couponSPUList){
            activities.add(couponActivityPoMapper.selectByPrimaryKey(couponSPU.getActivityId()));
        }
        return activities;
    }

    public List<CouponSPUPo> getSPUsInActivity(long activityId){
        CouponSPUPoExample couponSPUPoExample = new CouponSPUPoExample();
        var criteria = couponSPUPoExample.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        List<CouponSPUPo> list = couponSPUPoMapper.selectByExample(couponSPUPoExample);
        return list;
    }

    public PageInfo<CouponActivityPo> getInvalidActivities(int page, int pageSize, long shopId) {
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);

        List<CouponActivityPo> activityPoList = couponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(activityPoList);
    }

    public PageInfo<CouponActivityPo> getEffectiveActivities(int page, int pageSize, Long shopId, Byte timeline){
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();

        if (timeline != null) {
            if (timeline == 0) {
                /* 获取未开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == 1) {
                /* 获取明天开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                criteria.andBeginTimeLessThan(LocalDateTime.now().plusDays(1));
            } else if(timeline == 2) {
                /* 获取正在进行中的活动 */
                criteria.andBeginTimeLessThan(LocalDateTime.now());
                criteria.andEndTimeGreaterThan(LocalDateTime.now());
            } else if(timeline == 3) {
                criteria.andEndTimeLessThan(LocalDateTime.now());
            }
        }
        if(shopId != null){
            criteria.andShopIdEqualTo(shopId);
        }

        List<CouponActivityPo> activityPoList = couponActivityPoMapper.selectByExample(example);
        return new PageInfo<>(activityPoList);
    }

    public boolean changeActivityStatus(long id, Byte status){
        CouponActivityPo po = new CouponActivityPo();
        po.setId(id);
        po.setState(status);
        return couponActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }

    public boolean addActivity(CouponActivityPo po, long shopId){
        po.setShopId(shopId);
        po.setGmtCreate(LocalDateTime.now());
        return couponActivityPoMapper.insert(po) == 1;
    }

    public boolean delActivity(long id){
        return couponActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(CouponActivityPo po, long id) {
        po.setId(id);
        return couponActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }

    public CouponActivityPo getActivityById(long id){
        return couponActivityPoMapper.selectByPrimaryKey(id);
    }

    public Long addSpuToActivity(long activityId,long spuId){
        CouponSPUPo po = new CouponSPUPo();
        po.setActivityId(activityId);
        po.setSpuId(spuId);
        couponSPUPoMapper.insert(po);
        return po.getId();
    }

    public CouponSPUPo getCouponSPUPoById(long id){
        return couponSPUPoMapper.selectByPrimaryKey(id);
    }

    public boolean removeSpuFromActivity(long id){
        return couponSPUPoMapper.deleteByPrimaryKey(id)==1;
    }

    public PageInfo<CouponSPUPo> getSPUsInActivity(long activityID, int page, int pageSize){
        PageHelper.startPage(page,pageSize);

        CouponSPUPoExample couponSPUPoExample = new CouponSPUPoExample();
        var criteria = couponSPUPoExample.createCriteria();
        criteria.andActivityIdEqualTo(activityID);
        List<CouponSPUPo> list = couponSPUPoMapper.selectByExample(couponSPUPoExample);
        return PageInfo.of(list);
    }
}
