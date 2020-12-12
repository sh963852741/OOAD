package cn.edu.xmu.activity.dao;

import cn.edu.xmu.activity.mapper.GrouponActivityPoMapper;
import cn.edu.xmu.activity.model.Timeline;
import cn.edu.xmu.activity.model.bo.GrouponActivity;
import cn.edu.xmu.activity.model.bo.PresaleActivity;
import cn.edu.xmu.activity.model.po.GrouponActivityPo;
import cn.edu.xmu.activity.model.po.GrouponActivityPoExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GrouponActivityDao {

    @Autowired
    GrouponActivityPoMapper grouponActivityPoMapper;

    public GrouponActivityPo getActivityById(long id){
        return grouponActivityPoMapper.selectByPrimaryKey(id);
    }

    /**
     * 普通用户获取SPU的活动
     * @param page
     * @param pageSize
     * @param spuId
     * @param timeline
     * @return
     */
    public PageInfo<GrouponActivityPo> getActivitiesBySPUId(int page, int pageSize, long spuId, Byte timeline){
        PageHelper.startPage(page, pageSize);

        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(spuId);

        if(timeline != null) {
            if (timeline == Timeline.PENDING.ordinal()) {
                /* 获取未开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == Timeline.TOMORROW.ordinal()) {
                /* 获取明天开始的活动 */
                criteria.andBeginTimeGreaterThan(LocalDateTime.now());
                criteria.andBeginTimeLessThan(LocalDateTime.now().plusDays(1));
            } else if (timeline == Timeline.RUNNING.ordinal()) {
                /* 获取正在进行中的活动 */
                criteria.andBeginTimeLessThan(LocalDateTime.now());
                criteria.andEndTimeGreaterThan(LocalDateTime.now());
            } else if (timeline == Timeline.FINISHED.ordinal()) {
                criteria.andEndTimeLessThan(LocalDateTime.now());
            }
        }
        criteria.andStateNotEqualTo(GrouponActivity.GrouponStatus.DELETE.getCode());

        return PageInfo.of(grouponActivityPoMapper.selectByExample(example));
    }

    /**
     * 查询相同的SPU是否被重复添加
     * @param id
     * @return
     */
    public boolean hasSameSpu(long id){
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();

        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        criteria.andStateNotEqualTo(GrouponActivity.GrouponStatus.DELETE.getCode());

        return grouponActivityPoMapper.selectByExample(example).size() > 0;
    }

    public PageInfo<GrouponActivityPo> getGrouponsByAdmin(Long shopId, Byte state, Long spuId, LocalDateTime beginTime, LocalDateTime endTime, Integer page, Integer pageSize){
        PageHelper.startPage(page, pageSize);

        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        if(shopId!=null){
            criteria.andShopIdEqualTo(shopId);
        }
        if(state!=null){
            criteria.andStateEqualTo(state);
        }
        if(spuId!=null){
            criteria.andGoodsSpuIdEqualTo(spuId);
        }
        if(beginTime!=null){
            criteria.andBeginTimeGreaterThan(beginTime);
        }
        if(endTime!=null){
            criteria.andEndTimeLessThan(endTime);
        }
        return PageInfo.of(grouponActivityPoMapper.selectByExample(example));
    }

    public PageInfo<GrouponActivityPo> getEffectiveActivities(int page, int pageSize, Long shopId, Byte timeline, Long spuId, Boolean all){
        PageHelper.startPage(page, pageSize);

        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();

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
            criteria.andStateEqualTo(GrouponActivity.GrouponStatus.ONLINE.getCode());
        }

        List<GrouponActivityPo> list=grouponActivityPoMapper.selectByExample(example);
        return PageInfo.of(list);
    }

    public List<GrouponActivity> getAllGrouponsBySpuAdmin(Long id, Long shopId, Byte state){
        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        if(id!=null){
            criteria.andGoodsSpuIdEqualTo(id);
        }
        if(shopId!=null){
            criteria.andShopIdEqualTo(shopId);
        }
        if(state!=null){
            criteria.andStateEqualTo(state);
        }
        List<GrouponActivityPo> pos=grouponActivityPoMapper.selectByExample(example);
        return pos.stream().map(GrouponActivity::new).collect(Collectors.toList());
    }

    public boolean addActivity(GrouponActivityPo po, long spuId, long shopId){
        po.setShopId(shopId);
        po.setGoodsSpuId(spuId);
        po.setGmtCreate(LocalDateTime.now());
        po.setState(GrouponActivity.GrouponStatus.OFFLINE.getCode());
        return grouponActivityPoMapper.insert(po) == 1;
    }

    public boolean delActivity(long id){
        return grouponActivityPoMapper.deleteByPrimaryKey(id) == 1;
    }

    public boolean updateActivity(GrouponActivityPo po, long id) {
        po.setId(id);
        po.setGmtModified(LocalDateTime.now());
        return grouponActivityPoMapper.updateByPrimaryKeySelective(po) == 1;
    }

    public boolean setGroupsState(Byte state,List<Long> ids){
        for(Long id:ids){
            GrouponActivityPo po=new GrouponActivityPo();
            po.setId(id);
            po.setState(state);
            grouponActivityPoMapper.updateByPrimaryKeySelective(po);
        }
        return true;
    }

}
