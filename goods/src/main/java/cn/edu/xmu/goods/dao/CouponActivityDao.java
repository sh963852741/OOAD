package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CouponActivityPoMapper;
import cn.edu.xmu.goods.mapper.CouponSPUPoMapper;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.model.po.CouponActivityPoExample;
import cn.edu.xmu.goods.model.po.CouponSPUPo;
import cn.edu.xmu.goods.model.po.CouponSPUPoExample;
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
        po.setGmtCreated(LocalDateTime.now());
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

    public boolean removeSpuFromActivity(long activityId,long spuId){
        CouponSPUPoExample example = new CouponSPUPoExample();
        var criteria = example.createCriteria();
        criteria.andActivityIdEqualTo(activityId);
        criteria.andSpuIdEqualTo(spuId);

        var x = couponSPUPoMapper.selectByExample(example);

        return couponSPUPoMapper.deleteByPrimaryKey(x.get(0).getId())==1;
    }
}
