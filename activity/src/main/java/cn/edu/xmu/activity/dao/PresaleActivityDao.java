package cn.edu.xmu.activity.dao;

import cn.edu.xmu.activity.mapper.PresaleActivityPoMapper;
import cn.edu.xmu.activity.model.bo.PresaleActivity;
import cn.edu.xmu.activity.model.po.PresaleActivityPo;
import cn.edu.xmu.activity.model.po.PresaleActivityPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PresaleActivityDao {

    @Autowired
    PresaleActivityPoMapper presaleActivityPoMapper;

    public List<PresaleActivityPo> getAllActivityBySKUId(Byte state, Long skuId){
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        if(skuId != null){
            criteria.andGoodsSkuIdEqualTo(skuId);
        }
        if(state != null){
            criteria.andStateEqualTo(state);
        }
        return presaleActivityPoMapper.selectByExample(example);
    }

    public boolean getSameSKU(long skuId){
        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        criteria.andStateNotEqualTo(PresaleActivity.PresaleStatus.DELETE.getCode());
        criteria.andGoodsSkuIdEqualTo(skuId);

        return presaleActivityPoMapper.selectByExample(example).size() > 0;
    }

    /**
     * 顾客获取某上线的SKU预售活动信息，此函数为高频读
     * @param page
     * @param pageSize
     * @param skuId SPU的ID
     * @return
     */
    public PageInfo<PresaleActivityPo> getActivitiesBySKUId(int page, int pageSize, long skuId, Byte timeline){
        PageHelper.startPage(page, pageSize);

        PresaleActivityPoExample example = new PresaleActivityPoExample();
        PresaleActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);

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
        criteria.andStateEqualTo(PresaleActivity.PresaleStatus.ONLINE.getCode());

        var list = presaleActivityPoMapper.selectByExample(example);
        return PageInfo.of(list);
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

    public PageInfo<PresaleActivityPo> getEffectiveActivities(int page,int pageSize, Long shopId, Byte timeline){
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
        criteria.andStateEqualTo(PresaleActivity.PresaleStatus.ONLINE.getCode());

        var list = presaleActivityPoMapper.selectByExample(example);
        return PageInfo.of(list);
    }

    public int addActivity(PresaleActivityPo po, long skuId, long shopId){
        po.setGoodsSkuId(skuId);
        po.setShopId(shopId);
        po.setGmtCreate(LocalDateTime.now());
        po.setState(PresaleActivity.PresaleStatus.OFFLINE.getCode());
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
