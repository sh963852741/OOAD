package cn.edu.xmu.goods.service;


import cn.edu.xmu.goods.dao.CategoryDao;
import cn.edu.xmu.goods.dao.GoodsDao;
import cn.edu.xmu.goods.model.bo.Category;
import cn.edu.xmu.goods.model.bo.FloatPrice;
import cn.edu.xmu.goods.model.bo.Sku;
import cn.edu.xmu.goods.model.bo.Spu;
import cn.edu.xmu.goods.model.po.CategoryPo;
import cn.edu.xmu.goods.model.po.FloatPricePo;
import cn.edu.xmu.goods.model.po.SPUPo;
import cn.edu.xmu.goods.model.vo.FloatPriceVo;
import cn.edu.xmu.goods.model.vo.SkuChangeVo;
import cn.edu.xmu.goods.model.vo.SkuSelectVo;
import cn.edu.xmu.goods.model.vo.SpuVo;
import cn.edu.xmu.ooad.util.ImgHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: Yifei Wang
 * @Date: 2020/11/25 16:41
 */

@Service
public class GoodsService {

    private  static  final Logger logger = LoggerFactory.getLogger(GoodsService.class);

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private CategoryDao categoryDao;

    @Value("${goodsservice.webdav.username}")
    private String davUsername;

    @Value("${goodsservice.webdav.password}")
    private String davPassWord;

    @Value("${goodsservice.webdav.baseUrl}")
    private String baseUrl;

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
    public ReturnObject getAllSkus(SkuSelectVo vo,Integer page,Integer pageSize){
        return goodsDao.getAllSkus(vo, page, pageSize);
    }

    public ReturnObject getSkuDetails(Integer SkuId){
        return null;
    }

    /**
     * 功能描述: 上传sku图片
     * @Param: [multipartFile, shopId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 16:35
     */
    public ReturnObject upLoadSkuImg(MultipartFile multipartFile, Integer shopId, Integer id) {

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
        ReturnObject<Sku> skuRetObject=goodsDao.getSkuById(id.longValue());
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
    public ReturnObject deleteSkuById(Integer shopId, Integer id) {
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
        updateSku.setDisable(Sku.State.FORBID.getCode().byteValue());
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
    public ReturnObject updateSku(Integer shopId, Integer id, SkuChangeVo skuChangeVo) {
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
     * 功能描述: 获取商品分类关系
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/26 22:13
     */
    public ReturnObject getSubCategories(Integer id) {
        return goodsDao.getSubCategories(id.longValue());
    }

    /**
     * 功能描述: 新建商品分类
     * @Param: [id, name]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 8:51
     */
    public ReturnObject newCategory(Integer id, String name) {
        CategoryPo categoryPo=new CategoryPo();
        categoryPo.setPid(id.longValue());
        categoryPo.setName(name);
        ReturnObject ret=goodsDao.insertCategory(categoryPo);
        return ret;
    }

    /**
     * 功能描述: 修改商品类目
     * @Param: [id, name]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:40
     */
    public ReturnObject changeCategory(Integer id, String name) {
        CategoryPo po=new CategoryPo();
        po.setId(id.longValue());
        po.setName(name);
        ReturnObject ret=goodsDao.updateCategory(po);
        return ret;
    }

    /**
     * 功能描述: 删除商品类目
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 16:52
     */
    public ReturnObject deleteCategoryById(Integer id) {
        ReturnObject ret=goodsDao.deleteCategoryById(id.longValue());
        return ret;
    }

    /**
     * 功能描述: 获取spu详细信息
     * @Param: [id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:09
     */
    public ReturnObject getSpuById(Integer id) {
        ReturnObject ret=goodsDao.getSpuById(id.longValue());
        return ret;
    }

    /**
     * 功能描述: 新建spu
     * @Param: [id, spuVo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:25
     */
    public ReturnObject  newSpu(Integer id, SpuVo spuVo) {
        SPUPo po=new SPUPo();
        po.setShopId(id.longValue());
        po.setName(spuVo.getName());
        po.setDetail(spuVo.getDescription());
        po.setSpec(spuVo.getSpecs());

        ReturnObject ret=goodsDao.newSpu(po);
        return ret;
    }

    /**
     * 功能描述: 上传spu图片
     * @Param: [multipartFile, shopId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 17:58
     */
    public ReturnObject upLoadSpuImg(MultipartFile multipartFile, Integer shopId, Integer id) {

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
    public ReturnObject updateSpu(Integer id, Integer shopId, SpuVo spuVo) {
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
    public ReturnObject deleteSpuById(Integer id, Integer shopId) {
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
        spu.setDisabled(Spu.State.DELETE.getCode().byteValue());
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
    public ReturnObject onShelfSpu(Integer id, Integer shopId) {
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
        spu.setDisabled(Spu.State.NORM.getCode().byteValue());
        ReturnObject ret=goodsDao.updateSpu(spu);
        return ret;
    }

    public ReturnObject offShelfSpu(Integer id, Integer shopId) {
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
        spu.setDisabled(Spu.State.OFFSHELF.getCode().byteValue());
        ReturnObject ret=goodsDao.updateSpu(spu);
        return ret;
    }

    /**
     * 功能描述: 新建浮动价格
     * @Param: [id, shopId, floatPriceVo]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 18:58
     */
    public ReturnObject newFloatPrice(Integer id, Integer shopId, FloatPriceVo floatPriceVo ,Long userId) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySkuId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        FloatPricePo po=new FloatPricePo();
        po.setActivityPrice(floatPriceVo.getActivityPrice());
        po.setCreatedBy(userId);
        po.setGoodsSkuId(id.longValue());
        po.setQuantity(floatPriceVo.getQuantity().intValue());
        po.setBeginTime(LocalDateTime.parse(floatPriceVo.getBeginTime(), df));
        po.setEndTime(LocalDateTime.parse(floatPriceVo.getEndTime(),df));
        po.setValid((FloatPrice.State.NORM.getCode().byteValue()));
        ReturnObject ret=goodsDao.newFloatPrice(po);
        return ret;
    }


    /**
     * 功能描述: 使浮动价格失效
     * @Param: [id, shopId, userId]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 19:56
     */
    public ReturnObject deleteFloatPrice(Integer id, Integer shopId, Long userId) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdByFloatPriceId(id.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }

        FloatPrice price=new FloatPrice();
        price.setId(id.longValue());
        price.setValid(FloatPrice.State.VALID.getCode().byteValue());
        price.setInvalidBy(userId);
        ReturnObject ret=goodsDao.updateFloatPrice(price);
        return ret;
    }

    /**
     * 功能描述: 将spu加入分类
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:15
     */
    public ReturnObject addSpuToCategory(Integer shopId, Integer spuId, Integer id) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        ReturnObject<Category> ret=categoryDao.getCategoryById(id);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        Category category=ret.getData();
        if(category.getPid()!=0){
            return new ReturnObject(ResponseCode.CATEGORY_SET_ERROR);
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setCategoryId(id.longValue());
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
    public ReturnObject removeSpuFromCategory(Integer shopId, Integer spuId, Integer id) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        ReturnObject<Category> ret=categoryDao.getCategoryById(id);
        if(ret.getCode()!=ResponseCode.OK){
            return ret;
        }
        Category category=ret.getData();
        if(category.getPid()!=0){
            return new ReturnObject(ResponseCode.CATEGORY_SET_ERROR);
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setCategoryId(null);
        ReturnObject returnObject=goodsDao.updateSpu(spu);
        return returnObject;
    }


    /**
     * 功能描述: 将spu添加到品牌中
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:24
     */
    public ReturnObject addSpuToBrand(Integer shopId, Integer spuId, Integer id) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setBrandId(id.longValue());
        ReturnObject returnObject=goodsDao.updateSpu(spu);
        return returnObject;
    }

    /**
     * 功能描述: 将spu移出品牌
     * @Param: [shopId, spuId, id]
     * @Return: cn.edu.xmu.ooad.util.ReturnObject
     * @Author: Yifei Wang
     * @Date: 2020/11/27 20:25
     */
    public ReturnObject removeSpuFromBrand(Integer shopId, Integer spuId, Integer id) {
        if(shopId!=0){
            ReturnObject<Long> check=goodsDao.getShopIdBySpuId(spuId.longValue());
            if(check.getCode()!=ResponseCode.OK){
                return check;
            }
            if(shopId.longValue()!=check.getData()){
                return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
            }
        }
        Spu spu=new Spu();
        spu.setId(spuId.longValue());
        spu.setBrandId(null);
        ReturnObject returnObject=goodsDao.updateSpu(spu);
        return returnObject;
    }
}
