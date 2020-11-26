package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.GrouponActivityPoMapper;
import cn.edu.xmu.goods.model.po.GrouponActivityPo;
import cn.edu.xmu.goods.model.po.GrouponActivityPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class GrouponActivityDao {

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    List<GrouponActivityPo> getActivitiesBySPUId(long id){
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);

        return grouponActivityPoMapper.selectByExample(example);
    }

    List<GrouponActivityPo> getEffectiveActivities(int page, int pageSize, long shopId, int timeline){
        PageHelper.startPage(page, pageSize);

        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();

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

        return grouponActivityPoMapper.selectByExample(example);
    }

    boolean addActivity(GrouponActivityPo po){
        return grouponActivityPoMapper.insert(po) == 1;
    }

    boolean delActivity(long id){
        return grouponActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    boolean updateActivity(GrouponActivityPo po) {
        return grouponActivityPoMapper.updateByPrimaryKey(po) == 1;
    }
}
