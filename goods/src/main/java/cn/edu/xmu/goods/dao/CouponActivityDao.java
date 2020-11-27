package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CouponActivityPoMapper;
import cn.edu.xmu.goods.mapper.CouponSPUPoMapper;
import cn.edu.xmu.goods.model.po.CouponActivityPo;
import cn.edu.xmu.goods.model.po.CouponActivityPoExample;
import cn.edu.xmu.goods.model.po.CouponSPUPo;
import cn.edu.xmu.goods.model.po.CouponSPUPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public List<CouponActivityPo> getInvalidActivities(int page, int pageSize, long shopId) {
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andShopIdEqualTo(shopId);

        return couponActivityPoMapper.selectByExample(example);
    }

    public List<CouponActivityPo> getEffectiveActivities(int page, int pageSize, long shopId, int timeline){
        PageHelper.startPage(page, pageSize);

        CouponActivityPoExample example = new CouponActivityPoExample();
        CouponActivityPoExample.Criteria criteria = example.createCriteria();

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
        criteria.andShopIdEqualTo(shopId);

        return couponActivityPoMapper.selectByExample(example);
    }

    public boolean addActivity(CouponActivityPo po){
        return couponActivityPoMapper.insert(po) == 1;
    }

    public boolean delActivity(long id){
        return couponActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(CouponActivityPo po, long id) {
        po.setId(id);
        return couponActivityPoMapper.updateByPrimaryKey(po) == 1;
    }

    public CouponActivityPo getActivityById(long id){
        return couponActivityPoMapper.selectByPrimaryKey(id);
    }
}
