package cn.edu.xmu.activity.dao;

import cn.edu.xmu.activity.mapper.GrouponActivityPoMapper;
import cn.edu.xmu.activity.model.bo.GrouponActivity;
import cn.edu.xmu.activity.model.bo.PresaleActivity;
import cn.edu.xmu.activity.model.po.GrouponActivityPo;
import cn.edu.xmu.activity.model.po.GrouponActivityPoExample;
import cn.edu.xmu.activity.model.vo.GrouponActivityVo;
import cn.edu.xmu.ooad.model.VoObject;
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

    public PageInfo<GrouponActivityPo> getActivitiesBySPUId(int page, int pageSize, Long id, Byte timeline){
        PageHelper.startPage(page, pageSize);

        GrouponActivityPoExample example = new GrouponActivityPoExample();
        GrouponActivityPoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(id);

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
        PageInfo<GrouponActivityPo> info=PageInfo.of(grouponActivityPoMapper.selectByExample(example));
        return info;
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
        if(beginTime!=null && endTime!=null){
            criteria.andBeginTimeBetween(beginTime,endTime);
            criteria.andEndTimeBetween(beginTime,endTime);
        }
        PageInfo<GrouponActivityPo> info=PageInfo.of(grouponActivityPoMapper.selectByExample(example));
        return info;
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
            criteria.andStateNotEqualTo(PresaleActivity.PresaleStatus.CANCELED.getCode());
        }

        List<GrouponActivityPo> list=grouponActivityPoMapper.selectByExample(example);
        PageInfo<GrouponActivityPo> info=PageInfo.of(list);
        return info;
    }

    public List<GrouponActivity> getAllGrouponsBySpuAmdin(Long id,Long shopId,Byte state){
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
        List<GrouponActivity> retList = pos.stream().map(e -> new GrouponActivity(e)).collect(Collectors.toList());
        return retList;
    }

    public boolean addActivity(GrouponActivityPo po, long spuId, long shopId){
        po.setShopId(shopId);
        po.setGoodsSpuId(spuId);
        po.setGmtCreate(LocalDateTime.now());
        po.setGmtModified(po.getGmtCreate());
        po.setState(GrouponActivity.GrouponStatus.NORMAL.getCode());
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
