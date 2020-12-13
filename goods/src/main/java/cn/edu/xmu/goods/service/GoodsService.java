package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.BrandDao;
import cn.edu.xmu.goods.dao.CategoryDao;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.dao.ShopDao;
import cn.edu.xmu.goods.model.bo.Category;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.*;
import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.ooad.util.bloom.BloomFilterHelper;
import cn.edu.xmu.ooad.util.bloom.RedisBloomFilter;
import cn.edu.xmu.oomall.order.service.IFreightService;
import com.google.common.hash.Funnels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @Author: Yifei Wang
 * @Date: 2020/11/25 16:41
 */

@Service
public class GoodsService implements InitializingBean {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    //@DubboReference(version = "0.0.1-SNAPSHOT")
    private IFreightService freightService;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private CategoryDao categoryDao;

    @Autowired
    private BrandDao brandDao;

    @Autowired
    private ShopDao shopDao;

    @Value("${goodsservice.webdav.username}")
    private String davUsername;

    @Value("${goodsservice.webdav.password}")
    private String davPassWord;

    @Value("${goodsservice.webdav.baseUrl}")
    private String baseUrl;

    @Autowired
    private RedisTemplate redisTemplate;

    private BloomFilterHelper bloomFilterHelper;

    private RedisBloomFilter redisBloomFilter;

    private String skuBloomFilter = "SkuBloomFilter";

    private String spuBloomFilter = "SpuBloomFilter";

    @Override
    public void afterPropertiesSet() throws Exception {
        this.bloomFilterHelper =  new BloomFilterHelper<>(
                Funnels.longFunnel(),
                5000, 0.03);
        this.redisBloomFilter = new RedisBloomFilter<>(redisTemplate, bloomFilterHelper);
        SKUPoExample example = new SKUPoExample();
        List<SKUPo> skuPoList = goodsDao.getSkuList();
        for(SKUPo skuPo : skuPoList){
            redisBloomFilter.addByBloomFilter(skuBloomFilter, skuPo.getId());
        }
        SPUPoExample example1 = new SPUPoExample();
        List<SPUPo> spuPoList = goodsDao.getSpuList();
        for(SPUPo spuPo : spuPoList){
            redisBloomFilter.addByBloomFilter(spuBloomFilter, spuPo.getId());
        }
    }

    /**
     * 功能描述: 获取商品的所有状态
     * @Param: []
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/25 20:09
     */
    public ReturnObject getGoodsStates(){
            return goodsDao.getGoodState();
    }

    /**
     * 功能描述: 获取所有得sku
     * @Param: [vo, page, pageSize]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 16:35
     */
    public ReturnObject getAllSkus(SkuSelectVo vo, Integer page, Integer pageSize){
        //将spuSn转换为spuId
        Long spuId = null;
        if(vo.getSpuSn() != null){
            ReturnObject spuIdRet=goodsDao.getSpuIdBySpuSn(vo.getSpuSn());
            if(spuIdRet.getCode() == ResponseCode.OK){
                spuId = (Long)spuIdRet.getData();
            }
        }
        if(vo.getSpuId() != null && spuId != null){
            if(!vo.getSpuId().equals(spuId)){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
        }else{
            if(spuId != null){
                vo.setSpuId(spuId);
            }
        }
        //如果转换完spuId为空，则直接进行查询
        if(vo.getSpuId() == null){
            return goodsDao.getAllSkus(vo, page, pageSize);
        }
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter, vo.getSpuId())){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //spuId不为空 且shopId也不空 检查这个spuId是否是这个shopId的 如果不是 直接返回错误
        if(vo.getShopId() != null){
            ReturnObject shopIdRet = goodsDao.getShopIdBySpuId(vo.getSpuId());
            if(shopIdRet.getCode() != ResponseCode.OK){
                return shopIdRet;
            }
            if(!(vo.getShopId().equals((Long)shopIdRet.getData()))){
                return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
            }
            vo.setShopId(null);
            return goodsDao.getAllSkus(vo, page, pageSize);
        }
        return goodsDao.getAllSkus(vo, page, pageSize);
    }

    /**
     * 功能描述: 获取sku详细信息
     * @Param: [SkuId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/28 10:03
     */
    public ReturnObject getSkuDetails(Long skuId){
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter, skuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        ReturnObject ret=goodsDao.getSkuById(skuId);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        Sku sku=(Sku)ret.getData();
        SkuRetVo vo=sku.createVo();
        ReturnObject spuRet=getSpuById(sku.getGoodsSpuId());
        if(spuRet.getCode()!=ResponseCode.OK){
            vo.setSpu(null);
        }
        vo.setSpu((SpuRetVo) spuRet.getData());
        return new ReturnObject(vo);
    }

    /**
     * 功能描述: 上传sku图片
     * @Param: [multipartFile, shopId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 16:35
     */
    @Transactional
    public ReturnObject upLoadSkuImg(MultipartFile multipartFile, Integer shopId, Integer id) {
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //判断是否是属于自己商铺的SKU
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }

        //查看是否存在sku
        ReturnObject<Sku> skuRetObject = goodsDao.getSkuById(id.longValue());
        if(skuRetObject.getCode()== ResponseCode.INTERNAL_SERVER_ERR ||
                skuRetObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            return skuRetObject;
        }

        Sku sku=skuRetObject.getData();
        ReturnObject returnObject=new ReturnObject();

        try{
            returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername, davPassWord,baseUrl);
            //文件上传错误
            if(returnObject.getCode()!=ResponseCode.OK){
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            //更新数据库
            String oldFilename = sku.getImageUrl();
            Sku updateSku=new Sku();
            updateSku.setImageUrl(returnObject.getData().toString());
            updateSku.setId(sku.getId());
            ReturnObject updateReturnObject = goodsDao.updateSku(updateSku);

            //数据库更新失败，需删除新增的图片
            if(updateReturnObject.getCode()==ResponseCode.FIELD_NOTVALID || updateReturnObject.getCode()==ResponseCode.INTERNAL_SERVER_ERR){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername, davPassWord,baseUrl);
                return updateReturnObject;
            }

            //数据库更新成功，删除原来的图片
            if(updateReturnObject.getCode()==ResponseCode.OK){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassWord,baseUrl);
                return updateReturnObject;
            }

        }
        catch (IOException e){
            logger.debug("uploadImg: I/O Error:" + baseUrl);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }

    /**
     * 功能描述: 逻辑删除SKU
     * @Param: [shopId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 20:36
     */
    @Transactional
    public ReturnObject deleteSkuById(Integer shopId, Integer id) {
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Sku updateSku=new Sku();
        updateSku.setId(id.longValue());
        updateSku.setDisable((byte)0);
        ReturnObject ret=goodsDao.updateSku(updateSku);
        return ret;
    }

    /**
     * 功能描述: 修改SKU
     * @Param: [shopId, id, skuChangeVo]
     * @Return: void
     * @Author: Yifei Wang
     * @Date: 2020/11/26 21:59
     */
    @Transactional
    public ReturnObject updateSku(Integer shopId, Integer id, SkuChangeVo skuChangeVo) {
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }

        Sku sku=new Sku(skuChangeVo);
        sku.setId(id.longValue());
        ReturnObject ret=goodsDao.updateSku(sku);

        return ret;
    }

    /**
     * 功能描述: 获取spu详细信息
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:09
     */
    public ReturnObject<SpuRetVo> getSpuById(Long id) {
        ReturnObject ret = goodsDao.getSpuById(id);
        if(ret.getCode() != ResponseCode.OK){
            return ret;
        }
        Spu spu=new Spu((SPUPo)ret.getData());
        SpuRetVo vo=spu.createVo();
       // FreightModelDto dto = freightService.getFreightModel(spu.getFreightId());
       // vo.setFreight(OrderAdapter.adapterFreigthModel(dto));
        return new ReturnObject<>(vo);
    }

    /**
     * 功能描述: 获取简单的spu
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/1 20:14
     */
    public ReturnObject<HashMap<String,Object>> getSimpleSpuById(Long id){
        ReturnObject ret = goodsDao.getSpuById(id);
        if(ret.getCode() != ResponseCode.OK){
            return ret;
        }
        Spu spu=new Spu((SPUPo)ret.getData());
        Map<String,Object> simpleSpu=spu.createSimpleSpu();
        return new ReturnObject(simpleSpu);
    }

    /**
     * 功能描述: 新建spu
     * @Param: [id, spuVo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:25
     */
    @Transactional
    public ReturnObject  newSpu(Long id, SpuVo spuVo) {
        SPUPo po=new SPUPo();
        po.setShopId(id.longValue());
        po.setName(spuVo.getName());
        po.setDetail(spuVo.getDescription());
        po.setSpec(spuVo.getSpecs());

        ReturnObject ret=goodsDao.newSpu(po);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        Spu spu=new Spu((SPUPo) ret.getData());
        SpuRetVo vo=spu.createVo();
        vo.setFreight(null);
        return new ReturnObject(vo);
    }

    /**
     * 功能描述: 上传spu图片
     * @Param: [multipartFile, shopId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:58
     */
    public ReturnObject upLoadSpuImg(MultipartFile multipartFile, Integer shopId, Integer id) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        //判断是否是属于自己商铺的Spu
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }

        //查看是否存在spu
        ReturnObject<Spu> spuRetObject =goodsDao.getSpuById(id.longValue());
        if(spuRetObject.getCode()== ResponseCode.INTERNAL_SERVER_ERR ||
                spuRetObject.getCode()==ResponseCode.RESOURCE_ID_NOTEXIST){
            return spuRetObject;
        }

        Spu spu = spuRetObject.getData();
        ReturnObject returnObject=new ReturnObject();

        try{
            returnObject = ImgHelper.remoteSaveImg(multipartFile,2,davUsername, davPassWord,baseUrl);
            //文件上传错误
            if(returnObject.getCode()!=ResponseCode.OK){
                logger.debug(returnObject.getErrmsg());
                return returnObject;
            }

            //更新数据库
            String oldFilename = spu.getImageUrl();
            Spu updateSpu=new Spu();
            updateSpu.setImageUrl(returnObject.getData().toString());
            updateSpu.setId(spu.getId());
            ReturnObject updateReturnObject = goodsDao.updateSpu(updateSpu);

            //数据库更新失败，需删除新增的图片
            if(updateReturnObject.getCode()==ResponseCode.FIELD_NOTVALID || updateReturnObject.getCode()==ResponseCode.INTERNAL_SERVER_ERR){
                ImgHelper.deleteRemoteImg(returnObject.getData().toString(),davUsername, davPassWord,baseUrl);
                return updateReturnObject;
            }

            //数据库更新成功，删除原来的图片
            if(updateReturnObject.getCode()==ResponseCode.OK){
                ImgHelper.deleteRemoteImg(oldFilename,davUsername,davPassWord,baseUrl);
                return updateReturnObject;
            }

        }
        catch (IOException e){
            logger.debug("uploadImg: I/O Error:" + baseUrl);
            return new ReturnObject(ResponseCode.FILE_NO_WRITE_PERMISSION);
        }
        return returnObject;
    }

    /**
     * 功能描述: 更新spu
     * @Param: [id, shopId, spuVo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:59
     */
    @Transactional
    public ReturnObject updateSpu(Integer id, Integer shopId, SpuVo spuVo) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId != 0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(id.longValue());
        spu.setName(spuVo.getName());
        spu.setDetail(spuVo.getDescription());
        spu.setSpec(spuVo.getSpecs());
        ReturnObject ret=goodsDao.updateSpu(spu);
        return ret;
    }

    /**
     * 功能描述: 逻辑删除spu
     * @Param: [id, shopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:58
     */
    @Transactional
    public ReturnObject deleteSpuById(Integer id, Integer shopId) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(id.longValue());
        spu.setDisabled((byte)1);
//        spu.setState(Spu.State.DELETE.getCode().byteValue());
        ReturnObject ret=goodsDao.updateSpu(spu);
        return ret;
    }

    /**
     * 功能描述: 上架spu
     * @Param: [id, shopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 18:03
     */
    @Transactional
    public ReturnObject onShelfSku(Long id, Long shopId) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Sku sku=new Sku();
        sku.setId(id);
        sku.setState(Sku.State.NORM.getCode().byteValue());
//        spu.setState(Spu.State.NORM.getCode().byteValue());
        ReturnObject ret=goodsDao.updateSku(sku);
        return ret;
    }

    /**
     * 功能描述: 下架spu
     * @Param: [id, shopId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/28 10:01
     */
    @Transactional
    public ReturnObject offShelfSku(Long id, Long shopId) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId != 0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id);
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Sku sku=new Sku();
        sku.setId(id);
        sku.setState(Sku.State.OFFSHELF.getCode().byteValue());
//        spu.setState(Spu.State.NORM.getCode().byteValue());
        ReturnObject ret=goodsDao.updateSku(sku);
        return ret;
    }

    /**
     * 功能描述: 新建浮动价格
     * @Param: [id, shopId, floatPriceVo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 18:58
     */
    @Transactional
    public ReturnObject newFloatPrice(Long id, Long shopId, FloatPriceVo floatPriceVo ,Long userId) {
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId != 0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        FloatPricePo po=new FloatPricePo();
        po.setActivityPrice(floatPriceVo.getActivityPrice());
        po.setCreatedBy(userId);
        po.setGoodsSkuId(id.longValue());
        po.setQuantity(floatPriceVo.getQuantity());
        po.setBeginTime(floatPriceVo.getBeginTime());
        po.setEndTime(floatPriceVo.getEndTime());
        po.setValid((FloatPrice.State.NORM.getCode().byteValue()));
        ReturnObject<FloatPrice> ret=goodsDao.newFloatPrice(po);
        if(ret.getCode() == ResponseCode.OK){
            FloatPrice floatPrice = ret.getData();
            FloatPriceRetVo retVo = floatPrice.createVo();
            /** @problem  需要添加simpleUser
             *
             */
        }
        return ret;
    }

    /**
     * 功能描述: 使浮动价格失效
     * @Param: [id, shopId, userId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:56
     */
    @Transactional
    public ReturnObject deleteFloatPrice(Long id, Long shopId, Long userId) {
        if(shopId != 0){
            ReturnObject<Long> check=goodsDao.getShopIdByFloatPriceId(id);
            if(check.getCode() != ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }

        FloatPrice price=new FloatPrice();
        price.setId(id);
        price.setValid(FloatPrice.State.VALID.getCode().byteValue());
        price.setInvalidBy(userId);
        ReturnObject ret=goodsDao.updateFloatPrice(price);
        return ret;
    }

    /**
     * 功能描述: 获取当前生效的浮动价格
     * @Param: [skuId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/8 16:56
     */
    public ReturnObject getActicityPrice(Long skuId){
        if(skuId == null){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(!redisBloomFilter.includeByBloomFilter(skuBloomFilter,skuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        ReturnObject ret = goodsDao.getActivityPrice(skuId);
        if(ret.getCode() != ResponseCode.OK){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return ret;
    }

    /**
     * 功能描述: 将spu加入分类
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:15
     */
    @Transactional
    public ReturnObject addSpuToCategory(Long shopId, Long spuId, Long id) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,spuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId);
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        ReturnObject<Category> ret = categoryDao.getCategoryById(id);
        if(ret.getCode() != ResponseCode.OK){
            return ret;
        }
        Category category = ret.getData();
        if(category.getPid() == 0){
            return new ReturnObject(ResponseCode.CATEGORY_SET_ERROR);
        }
        Spu spu=new Spu();
        spu.setId(spuId);
        spu.setCategoryId(id);
        ReturnObject returnObject=goodsDao.updateSpu(spu);
        return returnObject;
    }

    /**
     * 功能描述: 将spu移出分类
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:22
     */
    @Transactional
    public ReturnObject removeSpuFromCategory(Long shopId, Long spuId, Long id) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,spuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId != 0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId);
            if(check.getCode() != ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setCategoryId(null);
        ReturnObject returnObject = goodsDao.updateSpu(spu);
        return returnObject;
    }

    /**
     * 功能描述: 将spu添加到品牌中
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:24
     */
    @Transactional
    public ReturnObject addSpuToBrand(Long shopId, Long spuId, Long id) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,spuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check = goodsDao.getShopIdBySpuId(spuId);
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(spuId);
        spu.setBrandId(id);
        ReturnObject returnObject = goodsDao.updateSpu(spu);
        return returnObject;
    }

    /**
     * 功能描述: 将spu移出品牌
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:25
     */
    @Transactional
    public ReturnObject removeSpuFromBrand(Long shopId, Long spuId, Long id) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,spuId)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId);
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setBrandId(null);
        ReturnObject returnObject=goodsDao.updateSpu(spu);
        return returnObject;
    }

    /**
     * 功能描述: 将sku添加到spu中
     * @Param: [shopId, id, skuVo]
     * @Return: void
     * @Author: Yifei Wang
     * @Date: 2020/12/1 10:24
     */
    @Transactional
    public ReturnObject addSkuToSpu(Long shopId, Long id, SkuVo skuVo) {
        if(!redisBloomFilter.includeByBloomFilter(spuBloomFilter,id)){
            return new ReturnObject(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        if(shopId!=0){
            ReturnObject<Long> check = goodsDao.getShopIdBySpuId(id);
            if(check.getCode() != ResponseCode.OK){
                return check;
            }
            if(shopId.longValue() != check.getData().longValue()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        SKUPo po = new SKUPo();
        ReturnObject ret = goodsDao.newSku(po);
        if(ret.getCode() != ResponseCode.OK){
            return ret;
        }
        //redisBloomFilter.addByBloomFilter(skuBloomFilter,);
        Spu spu=new Spu();
        spu.setId(id.longValue());
        ReturnObject spuRet=goodsDao.updateSpu(spu);
        //TODO 需要更新spu的规格 但是规格参数不明;
        /**ReturnObject spuRet=goodsDao.updateSpu(spu);
        if(spuRet.getCode()!=ResponseCode.OK){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }**/
        return spuRet;
    }

    /**
     * 功能描述: 通过spuid获取shopid
     * @Param:
     * @Return:
     * @Author: Yifei Wang
     * @Date: 2020/11/30 20:08
     */
    public ReturnObject getShopIdBySpuId(Long id){
        return goodsDao.getShopIdBySpuId(id);
    }

    /**
     * 功能描述: 通过skuId获取shopid
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/12/5 17:19
     */
    public ReturnObject<Long> getShopIdBySkuId(Long id){
        return goodsDao.getShopIdBySkuId(id);
    }

    public ReturnObject changSkuInventory(Long skuId, Integer quantity){
        return goodsDao.changSkuInventory(skuId,quantity);
    }

}
