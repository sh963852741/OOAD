package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class PresaleActivityDao {

    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    List<PresaleActivityPo> getActivitiesBySPUId(long id){
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);

        return presaleActivityPoMapper.selectByExample(example);
    }

    List<PresaleActivityPo> getEffectiveActivities(int page,int pageSize, long shopId, int timeline){
        PageHelper.startPage(page, pageSize);

        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();

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

        return presaleActivityPoMapper.selectByExample(example);
    }

    boolean addActivity(PresaleActivityPo po){
        return presaleActivityPoMapper.insert(po) == 1;
    }

    boolean delActivity(long id){
        return presaleActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    boolean updateActivity(PresaleActivityPo po) {
        return presaleActivityPoMapper.updateByPrimaryKey(po) == 1;
    }
}
