package cn.edu.xmu.flashsale.dao;

import cn.edu.xmu.flashsale.mapper.FlashSaleItemPoMapper;
import cn.edu.xmu.flashsale.mapper.FlashSalePoMapper;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPo;
import cn.edu.xmu.flashsale.model.po.FlashSaleItemPoExample;
import cn.edu.xmu.flashsale.model.po.FlashSalePo;
import cn.edu.xmu.flashsale.model.po.FlashSalePoExample;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Slf4j
public class FlashSaleDao {
    @Autowired
    FlashSaleItemPoMapper flashSaleItemPoMapper;
    @Autowired
    FlashSalePoMapper flashSalePoMapper;

    public int addFlashSale(FlashSalePo po){
        po.setGmtCreate(LocalDateTime.now());
        return flashSalePoMapper.insert(po);
    }

    public int deleteFlashSale(long id){
        return flashSalePoMapper.deleteByPrimaryKey(id);
    }

    public FlashSalePo getFlashSale(long id){
        return flashSalePoMapper.selectByPrimaryKey(id);
    }

    public int setFlashSaleDate(long id, LocalDateTime dateTime){
        FlashSalePo po = new FlashSalePo();
        po.setId(id);
        po.setGmtModified(LocalDateTime.now());
        po.setFlashDate(dateTime);
        return flashSalePoMapper.updateByPrimaryKeySelective(po);
    }

    public int delFlashSaleItemByFlashSaleId(long flashSaleId){
        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
        criteria.andSaleIdEqualTo(flashSaleId);

        return flashSaleItemPoMapper.deleteByExample(example);
    }

    public int addFlashSaleItem(FlashSaleItemPo po){
        return flashSaleItemPoMapper.insert(po);
    }

    public int deleteFlashSaleItem(long id){
        return flashSaleItemPoMapper.deleteByPrimaryKey(id);
    }

    public List<FlashSalePo> selectFlashSaleByTimeAndDate(LocalDate date, List<Long> segmentIds){
        log.debug(segmentIds.toString());
        FlashSalePoExample example = new FlashSalePoExample();
        FlashSalePoExample.Criteria criteria = example.createCriteria();
        criteria.andFlashDateGreaterThanOrEqualTo(date.atStartOfDay());
        criteria.andFlashDateLessThanOrEqualTo(date.atStartOfDay().plusDays(1));
        criteria.andTimeSegIdIn(segmentIds);

        return flashSalePoMapper.selectByExample(example);
    }

    public List<FlashSaleItemPo> getFlashSaleItemByFlashSaleId(long id){
        FlashSaleItemPoExample example = new FlashSaleItemPoExample();
        FlashSaleItemPoExample.Criteria criteria = example.createCriteria();
        criteria.andSaleIdEqualTo(id);

        return flashSaleItemPoMapper.selectByExample(example);
    }
}
