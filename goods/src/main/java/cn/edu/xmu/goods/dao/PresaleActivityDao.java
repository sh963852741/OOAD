package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

public class PresaleActivityDao {

    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    /**
     * 顾客获取某SPU的预售活动信息，此函数为高频读
     * @param page
     * @param pageSize
     * @param id SPU的ID
     * @return
     */
    public List<PresaleActivityPo> getActivitiesBySPUId(int page, int pageSize, long id, int timeline){
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);

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

        return presaleActivityPoMapper.selectByExample(example);
    }


    public List<PresaleActivityPo> getEffectiveActivities(int page,int pageSize, long shopId, int timeline, long spuId, boolean all){
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
        criteria.andGoodsSpuIdEqualTo(spuId);
        if(!all){
            criteria.andStateNotEqualTo(PresaleActivity.PresaleStatus.CANCELED.getCode());
        }

        return presaleActivityPoMapper.selectByExample(example);
    }

    public boolean addActivity(PresaleActivityPo po){
        return presaleActivityPoMapper.insert(po) == 1;
    }

    public boolean delActivity(long id){
        return presaleActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(PresaleActivityPo po, long primaryKeyId) {
        po.setId(primaryKeyId);
        return presaleActivityPoMapper.updateByPrimaryKey(po) == 1;
    }
}
