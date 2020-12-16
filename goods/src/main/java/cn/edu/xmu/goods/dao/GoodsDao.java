package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CategoryPoMapper;
import cn.edu.xmu.goods.mapper.FloatPricePoMapper;
import cn.edu.xmu.goods.mapper.SKUPoMapper;
import cn.edu.xmu.goods.mapper.SPUPoMapper;
import cn.edu.xmu.goods.model.vo.SkuSelectVo;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.bo.Good;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.FloatPriceRetVo;
import cn.edu.xmu.goods.model.vo.SkuSelectReturnVo;
import cn.edu.xmu.goods.model.vo.SkuSimpleRetVo;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @Author: Yifei Wang
 * @Date: 2020/11/25 16:25
 **/
@Repository
public class GoodsDao {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodsDao.class);

    @Autowired
    SKUPoMapper skuPoMapper;
    @Autowired
    SPUPoMapper spuPoMapper;
    @Autowired
    FloatPricePoMapper floatPricePoMapper;
    @Autowired
    CategoryPoMapper categoryPoMapper;
    @Autowired
    RedisTemplate redisTemplate;

    public List<SKUPo> getSkuList(){
        SKUPoExample example = new SKUPoExample();
        List<SKUPo> skuPoList = skuPoMapper.selectByExample(example);
        return skuPoList;
    }

    public List<SPUPo> getSpuList(){
        SPUPoExample example = new SPUPoExample();
        List<SPUPo> spuPoList = spuPoMapper.selectByExample(example);
        return spuPoList;
    }

    /**
     * 功能描述: 获取商品的所有状态
     * @Param: []
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 20:10
     */
    public ReturnObject getGoodState(){
        List<Map<String, Object>> stateList=new ArrayList<>();
        for (Good.State enum1 : Good.State.values()) {
            Map<String,Object> temp=new HashMap<>();
            temp.put("code",enum1.getCode());
            temp.put("name",enum1.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }

    /*
     * 功能描述:通过spusn获取spuid
     * @Param:
     * @Return:
     * @Author: Yifei Wang
     * @Date: 2020/12/1 15:11
     */
    public ReturnObject getSpuIdBySpuSn(String goodsSn){
        SPUPoExample example=new SPUPoExample();
        SPUPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSnEqualTo(goodsSn);
        try{
            List<SPUPo>list=spuPoMapper.selectByExample(example);
            if(list==null||list.size()==0){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(list.get(0).getId());
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    /**
     * 功能描述: 查询所有Sku
     * @Param: [vo, page, pageSize]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 21:03
     */
    public ReturnObject getAllSkus(SkuSelectVo vo, Integer page, Integer pageSize){
        SKUPoExample example =new SKUPoExample();
        SKUPoExample.Criteria criteria=example.createCriteria();
        if(null != vo.getSkuSn() && !"".equals(vo.getSkuSn())){
            if(vo.getShopId() == null){
                criteria.andSkuSnEqualTo(vo.getSkuSn());
            }
        }
        if(null != vo.getSpuId()){
            criteria.andGoodsSpuIdEqualTo(vo.getSpuId());
        }
        //TODO 查询所有sku disable 现在是4  应该是0 1  现在不涉及此条件 下面的查询也是一样
        //criteria.andDisabledEqualTo((byte)0);
        criteria.andStateEqualTo(Sku.State.NORM.getCode().byteValue());
        if(null != vo.getShopId()){
            List<SPUPo> list = getSpusByShopId(vo.getShopId());
            if(list != null){
                for(SPUPo po: list){
                    SKUPoExample.Criteria temp = example.createCriteria();
                    temp.andGoodsSpuIdEqualTo(po.getId());
                    //temp.andDisabledEqualTo((byte)0);
                    temp.andStateEqualTo(Sku.State.NORM.getCode().byteValue());
                    if(null != vo.getSkuSn() && !"".equals(vo.getSkuSn())){
                        criteria.andSkuSnEqualTo(vo.getSkuSn());
                    }
                    example.or(temp);
                }
            }

        }
        List<SKUPo> skuPoList =new ArrayList<>();
        List<SkuSimpleRetVo> skuSimpleRetVos = new ArrayList<>();
        try {
            PageHelper.startPage(page, pageSize);
            skuPoList = skuPoMapper.selectByExample(example);
            for (SKUPo po : skuPoList) {
                Sku sku = new Sku(po);

                //查询浮动价格
                FloatPricePoExample example1=new FloatPricePoExample();
                FloatPricePoExample.Criteria criteria1=example1.createCriteria();
                criteria1.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                criteria1.andEndTimeGreaterThan(LocalDateTime.now());
                criteria1.andGoodsSkuIdEqualTo(po.getId());
                criteria1.andValidEqualTo(FloatPrice.State.NORM.getCode().byteValue());
                List<FloatPricePo> pricePos=floatPricePoMapper.selectByExample(example1);

                if(pricePos.size()==0){
                    //没查到浮动价格
                    sku.setPrice(po.getOriginalPrice());
                }else{
                    //查询到浮动价格
                    sku.setPrice(pricePos.get(0).getActivityPrice());
                }
                skuSimpleRetVos.add(sku.createSimpleVo());
            }
        }
        catch (Exception e) {
            logger.error("selectAllSkus: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        PageInfo<SKUPo> skuPoPageInfo=PageInfo.of(skuPoList);
        SkuSelectReturnVo retVo=new SkuSelectReturnVo();
        retVo.setList(skuSimpleRetVos);
        retVo.setPage((long)page);
        retVo.setPageSize((long)pageSize);
        retVo.setPages((long)skuPoPageInfo.getPages());
        retVo.setTotal(skuPoPageInfo.getTotal());
        return new ReturnObject<>(retVo);
    }

    /**
     * 功能描述: 根据SPU获取sku
     * @Param:
     * @Return:
     * @Author: Yifei Wang
     * @Date: 2020/11/30 19:07
     */
    public ReturnObject getSkusBySpu(Long spuId){
        SKUPoExample example =new SKUPoExample();
        SKUPoExample.Criteria criteria=example.createCriteria();
        criteria.andGoodsSpuIdEqualTo(spuId);
        // TODO disable
        //criteria.andDisabledEqualTo((byte)0);
        criteria.andStateEqualTo(Sku.State.NORM.getCode().byteValue());
        try {
            List<SKUPo> skuPoList = skuPoMapper.selectByExample(example);
            List<SkuSimpleRetVo> skuSimpleRetVos = new ArrayList<>();
            for (SKUPo po : skuPoList) {
                Sku sku = new Sku(po);

                //查询浮动价格
                FloatPricePoExample example1 = new FloatPricePoExample();
                FloatPricePoExample.Criteria criteria1 = example1.createCriteria();
                criteria1.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
                criteria1.andEndTimeGreaterThan(LocalDateTime.now());
                criteria1.andGoodsSkuIdEqualTo(po.getId());
                List<FloatPricePo> pricePos = floatPricePoMapper.selectByExample(example1);

                if (pricePos.size() == 0) {
                    //没查到浮动价格
                    sku.setPrice(po.getOriginalPrice());
                } else {
                    //查询到浮动价格
                    sku.setPrice(pricePos.get(0).getActivityPrice());
                }
                skuSimpleRetVos.add(sku.createSimpleVo());
            }
            return new ReturnObject(skuSimpleRetVos);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 通过shop获取spus
     * @Param:
     * @Return:
     * @Author: Yifei Wang
     * @Date: 2020/12/9 18:06
     */
    public List<SPUPo> getSpusByShopId(Long shopId){
        SPUPoExample example = new SPUPoExample();
        SPUPoExample.Criteria criteria= example.createCriteria();
        criteria.andShopIdEqualTo(shopId);
        try {
            List<SPUPo> lists = spuPoMapper.selectByExample(example);
            if (lists == null) {
                return null;
            }
            return lists;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 功能描述: 新建sku
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/1 10:32
     */
    public ReturnObject newSku(SKUPo po){
        po.setGmtCreate(LocalDateTime.now());
        po.setGmtModified(po.getGmtCreate());
        po.setSkuSn(Common.genSeqNum());
        po.setState(Sku.State.OFFSHELF.getCode().byteValue());
        try{
            int ret;
            ret=skuPoMapper.insertSelective(po);
            if(ret==0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }
            Sku sku=new Sku(po);
            SkuSimpleRetVo vo=sku.createSimpleVo();
            vo.setPrice(vo.getOriginalPrice());
            return new ReturnObject(vo);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 根据id获取sku
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 16:43
     */
    public ReturnObject getSkuById(Long id){
        SKUPo skuPo = new SKUPo();
        try {
            if(redisTemplate.hasKey("skuPo_"+id)){
                skuPo = (SKUPo) redisTemplate.opsForValue().get("skuPo_"+id);
            }else{
                skuPo = skuPoMapper.selectByPrimaryKey(id);
                if(skuPo!=null){
                    redisTemplate.opsForValue().set("skuPo_"+id,skuPo);
                }
            }
        }catch (Exception e){
            logger.error("selectSkuDetails: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(skuPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(skuPo.getState().equals(Sku.State.OFFSHELF.getCode().byteValue()) ||
        skuPo.getState().equals(Sku.State.FORBID.getCode().byteValue()) ||
        skuPo.getDisabled().equals((byte)1))
        {
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Sku sku = new Sku(skuPo);
        ReturnObject<Long> priceRet = getActivityPrice(id);
        if(priceRet.getCode() != ResponseCode.OK){
            sku.setPrice(sku.getOriginalPrice());
        }
        if(priceRet.getData() == null){
            sku.setPrice(sku.getOriginalPrice());
        }
        sku.setPrice(priceRet.getData());
        return new ReturnObject<>(sku);
    }

    /**
     * 功能描述: 内部调用获取sku 不用 判断是否被删除 未上架 以及店铺关闭
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/15 10:18
     */
    public ReturnObject getSkuByDubbo(Long id){
        SKUPo skuPo = new SKUPo();
        try {
            if(redisTemplate.hasKey("skuPo_"+id)){
                skuPo = (SKUPo) redisTemplate.opsForValue().get("skuPo_"+id);
            }else{
                skuPo = skuPoMapper.selectByPrimaryKey(id);
                if(skuPo!=null){
                    redisTemplate.opsForValue().set("skuPo_"+id,skuPo);
                }
            }
        }catch (Exception e){
            logger.error("selectSkuDetails: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if(skuPo == null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        Sku sku = new Sku(skuPo);
        ReturnObject<Long> priceRet = getActivityPrice(id);
        if(priceRet.getCode() != ResponseCode.OK || priceRet.getData() == null){
            sku.setPrice(sku.getOriginalPrice());
        }else {
            sku.setPrice(priceRet.getData());
        }
        return new ReturnObject<>(sku);
    }


    /**
     * 功能描述: 更改商品库存
     * @Param: [skuId, quantity]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/9 9:15
     */
    public ReturnObject changSkuInventory(Long skuId, Integer quantity){
        try{
            SKUPo skuPo = new SKUPo();
            if(redisTemplate.hasKey("skuPo_"+skuId)){
                skuPo = (SKUPo) redisTemplate.opsForValue().get("skuPo_"+skuId);
            }else{
                skuPo = skuPoMapper.selectByPrimaryKey(skuId);
            }
            //如果查询不到 或者被删除 为上架 和 店家已关店 返回失败
            if(skuPo == null || skuPo.getState().equals(Sku.State.FORBID.getCode().byteValue()) ||
            skuPo.getState().equals(Sku.State.OFFSHELF.getCode().byteValue()) ||
            skuPo.getDisabled().equals((byte)1)){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if(skuPo.getInventory() < quantity){
                return new ReturnObject(ResponseCode.SKU_NOTENOUGH);
            }
            skuPo.setInventory(skuPo.getInventory() - quantity);
            skuPo.setGmtModified(LocalDateTime.now());
            int ret;
            ret=skuPoMapper.updateByPrimaryKeySelective(skuPo);
            if(ret == 0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }
            redisTemplate.delete("skuPo_"+skuId);
            return new ReturnObject(ResponseCode.OK);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 更新sku
     * @Param: [sku]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 17:26
     */
    public ReturnObject updateSku(Sku sku) {
        SKUPo po;
        if(redisTemplate.hasKey("skuPo_"+sku.getId())){
            po = (SKUPo) redisTemplate.opsForValue().get("skuPo_"+sku.getId());
        }else{
            po = skuPoMapper.selectByPrimaryKey(sku.getId());
        }
        if(po == null || po.getState() == Sku.State.FORBID.getCode().byteValue()){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        SKUPo skuPo=sku.createPo();
        int ret;
        try{
            skuPo.setGmtModified(LocalDateTime.now());
            ret=skuPoMapper.updateByPrimaryKeySelective(skuPo);
        }catch (Exception e){
            logger.error("updateSkuImg: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateSku: update fail. sku id: " + sku.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateSku: update sku success : " + sku.toString());
            redisTemplate.delete("skuPo_"+sku.getId());
            redisTemplate.delete("sku_"+sku.getId());
            return new ReturnObject();
        }
    }

    /**
     * 功能描述: 通过SkuId获取ShopId
     * @Param: id
     * @Return: ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 20:16
     */
    public ReturnObject<Long> getShopIdBySkuId(Long id){
        try {
            SKUPo skuPo;
            if(redisTemplate.hasKey("skuPo_"+id)){
                skuPo = (SKUPo) redisTemplate.opsForValue().get("skuPo_"+id);
            }else{
                skuPo = skuPoMapper.selectByPrimaryKey(id);
                if(skuPo != null)
                redisTemplate.opsForValue().set("skuPo_"+id,skuPo);
            }
            if(skuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(skuPo.getGoodsSpuId());
            if(spuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo.getShopId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 通过SpuId获取ShopId
     * @Param: id
     * @Return: ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 20:16
     */
    public ReturnObject getShopIdBySpuId(Long id){
        try {
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(id);
            if(spuPo==null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo.getShopId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 获取spu
     * @Param: [longValue]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:10
     */
    public ReturnObject getSpuById(Long id) {
        try {
            SPUPo spuPo=spuPoMapper.selectByPrimaryKey(id);
            if(spuPo == null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            if(spuPo.getDisabled() == 1){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return new ReturnObject(spuPo);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 新建spu
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:29
     */
    public ReturnObject newSpu(SPUPo po) {
        try{
            int ret;
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(LocalDateTime.now());
            po.setDisabled((byte) 0);
//            po.setState(Spu.State.OFFSHELF.getCode().byteValue());
            po.setGoodsSn(Common.genSeqNum());
            ret=spuPoMapper.insertSelective(po);
            if(ret == 0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                return new ReturnObject(po);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 更新spu
     * @Param: [updateSpu]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:00
     */
    public ReturnObject updateSpu(Spu updateSpu) {
        SPUPo po = spuPoMapper.selectByPrimaryKey(updateSpu.getId());
        if(po == null || po.getDisabled() == 1){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        SPUPo spuPo=updateSpu.createPo();
        int ret;
        try{
            spuPo.setGmtModified(LocalDateTime.now());
            ret=spuPoMapper.updateByPrimaryKeySelective(spuPo);
        }catch (Exception e){
            logger.error("updateSkuImg: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateSku: update fail. spu id: " + updateSpu.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        logger.debug("updateSku: update spu success : " + updateSpu.toString());
        return new ReturnObject();
    }

    /**
     * 功能描述: 新建浮动价格
     * @Param: [po]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:09
     */
    public ReturnObject newFloatPrice(FloatPricePo po) {
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria1 = example.createCriteria();
        FloatPricePoExample.Criteria criteria2 = example.createCriteria();
        try{
            criteria1.andGoodsSkuIdEqualTo(po.getGoodsSkuId());
            criteria1.andBeginTimeGreaterThanOrEqualTo(po.getBeginTime());
            criteria1.andBeginTimeLessThan(po.getEndTime());
            criteria1.andValidEqualTo((byte)1);
            criteria2.andGoodsSkuIdEqualTo(po.getGoodsSkuId());
            criteria2.andEndTimeGreaterThan(po.getBeginTime());
            criteria2.andEndTimeLessThanOrEqualTo(po.getEndTime());
            criteria2.andValidEqualTo((byte)1);
            example.or(criteria2);
            List<FloatPricePo> list=floatPricePoMapper.selectByExample(example);
            if(list.size() != 0){
                return new ReturnObject(ResponseCode.SKUPRICE_CONFLICT);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
        try{
            po.setGmtCreate(LocalDateTime.now());
            po.setGmtModified(po.getGmtCreate());
            int ret;
            ret=floatPricePoMapper.insertSelective(po);
            if(ret == 0){
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }else{
                FloatPrice bo = new FloatPrice(po);
                return new ReturnObject(bo);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }
    
    /**
     * 功能描述: 更新floatPrice
     * @Param: FloatPrice
     * @Return: ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:44
     */
    public ReturnObject updateFloatPrice(FloatPrice floatPrice){

        FloatPricePo po=floatPrice.createPo();
        int ret;
        po.setGmtModified(LocalDateTime.now());

        try{
            FloatPricePo temp = floatPricePoMapper.selectByPrimaryKey(floatPrice.getId());
            if(temp == null || temp.getValid() == 0){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
        try{
            ret = floatPricePoMapper.updateByPrimaryKeySelective(po);
        }catch (Exception e){
            logger.error("updateFLoatPrice: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }

        if (ret == 0) {
            logger.debug("updateFloatPrice: update fail. id: " + po.getId());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        }
        logger.debug("updateFloatPrice: update success : " + po.toString());
        return new ReturnObject();

    }

    /**
     * 功能描述: 通过浮动价格id获取商铺id
     * @Param: id
     * @Return: ReturnObject<Long>
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:58
     */
    public ReturnObject getShopIdByFloatPriceId(Long id){
        try {
            FloatPricePo po = floatPricePoMapper.selectByPrimaryKey(id);
            if(po == null){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            return getShopIdBySkuId(po.getGoodsSkuId());
        }catch (Exception e){
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 功能描述: 获取生效的浮动价格
     * @Param: [skuId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/8 16:55
     */
    public ReturnObject getActivityPrice(Long skuId){
        FloatPricePoExample example = new FloatPricePoExample();
        FloatPricePoExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsSkuIdEqualTo(skuId);
        criteria.andBeginTimeLessThanOrEqualTo(LocalDateTime.now());
        criteria.andEndTimeGreaterThan(LocalDateTime.now());
        criteria.andValidEqualTo(FloatPrice.State.NORM.getCode().byteValue());
        try{
            List<FloatPricePo> floatPricePoList = floatPricePoMapper.selectByExample(example);
            if(floatPricePoList == null || floatPricePoList.size()==0){
                return new ReturnObject(null);
            }
            return new ReturnObject(floatPricePoList.get(0).getActivityPrice());
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

}