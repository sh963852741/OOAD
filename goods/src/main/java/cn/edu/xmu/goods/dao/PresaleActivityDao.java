package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.goods.model.po.PresaleActivityPo;
import cn.edu.xmu.goods.model.po.PresaleActivityPoExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PresaleActivityDao {

    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    /**
     * 顾客获取某SPU的预售活动信息，此函数为高频读
     * @param page
     * @param pageSize
     * @param spuId SPU的ID
     * @return
     */
    public List<PresaleActivityPo> getActivitiesBySPUId(int page, int pageSize, long spuId, Byte timeline){
        PageHelper.startPage(page, pageSize);

        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(spuId);

        if(timeline != null) {
            if (timeline == 0) {
                /* 获取未开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == 1) {
                /* 获取明天开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                criteria.andBeginTimeLessThan(LocalDateTime.now().plusDays(1));
            } else if (timeline == 2) {
                /* 获取正在进行中的活动 */
                criteria.andBeginTimeLessThan(LocalDateTime.now());
                criteria.andEndTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == 3) {
                criteria.andEndTimeLessThan(LocalDateTime.now());
            }
        }

        return presaleActivityPoMapper.selectByExample(example);
    }

    public boolean changeActivityStatus(long id, byte state){
        PresaleActivityPo po = new PresaleActivityPo();
        po.setId(id);
        po.setState(state);
        po.setGmtModified(LocalDateTime.now());
        return presaleActivityPoMapper.updateByPrimaryKey(po) == 1;
    }

    public PresaleActivityPo getActivityById(long activityId){
        return presaleActivityPoMapper.selectByPrimaryKey(activityId);
    }

    public List<PresaleActivityPo> getEffectiveActivities(int page,int pageSize, Long shopId, Byte timeline, Long spuId, Boolean all){
        PageHelper.startPage(page, pageSize);

        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();

        if(timeline != null){
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

        if(spuId != null){
            criteria.andGoodsSpuIdEqualTo(spuId);
        }

        if(!all){
            criteria.andStateNotEqualTo(PresaleActivity.PresaleStatus.CANCELED.getCode());
        }

        return presaleActivityPoMapper.selectByExample(example);
    }

    public int addActivity(PresaleActivityPo po, long skuId, long shopId){
        po.setGoodsSpuId(skuId);
        po.setShopId(shopId);
        po.setGmtCreate(LocalDateTime.now());
        po.setState(PresaleActivity.PresaleStatus.NEW.getCode());
        return presaleActivityPoMapper.insert(po);
    }

    public boolean delActivity(long id){
        return presaleActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(PresaleActivityPo po, long primaryKeyId) {
        po.setId(primaryKeyId);
        return presaleActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }
}
