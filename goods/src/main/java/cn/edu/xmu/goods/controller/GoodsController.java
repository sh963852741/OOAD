package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.other.impl.IBeShareService;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 商品控制器
 *
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodsController {


    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

   // @DubboReference(version = "0.0.1-SNAPSHOT",check = false)
    private IBeShareService beShareService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 获得商品SPU的所有状态
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 15:01
     */
    @ApiOperation(value = "获取商品SKU的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/skus/states")
    public Object getSpuStates() {
        ReturnObject<List> returnObject = goodsService.getGoodsStates();
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 查询sku
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "查询sku")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "shopId", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "skuSn", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "spuId", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "spuSn", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "page", dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/skus")
    public Object getSkus(@RequestParam(value = "shopId",required = false) Long shopId,
                          @RequestParam(value = "skuSn",required = false) String skuSn,
                          @RequestParam(value = "spuId",required = false) Long spuId,
                          @RequestParam(value = "spuSn",required = false) String spuSn,
                          @RequestParam(required = false, defaultValue = "1") Integer page,
                          @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        logger.debug("getAllSkus: page = " + page + "  pageSize =" + pageSize);

        SkuSelectVo vo = new SkuSelectVo();
        vo.setShopId(shopId);
        vo.setSkuSn(skuSn);
        vo.setSpuId(spuId);
        vo.setSpuSn(spuSn);
        ReturnObject returnObject = goodsService.getAllSkus(vo, page, pageSize);
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 获得sku的详细信息
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询sku的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "skuId", required = true, dataType = "String", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/skus/{id}")
    //TODO 查询后添加足迹
    public Object getSkuDetails(@PathVariable Long id,Long loginUser) {
        ReturnObject ret = goodsService.getSkuDetails(id);
        if(ret.getCode() == ResponseCode.OK){
            //rocketMQTemplate.sendOneWay("");
        }
        if(ret.getCode() == ResponseCode.RESOURCE_ID_NOTEXIST){
            httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        }
        return Common.decorateReturnObject(ret);
    }


    /**
     * 管理员添加新的SKU到SPU里
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     *  modifiedby xuyue 2020/12/17 11:37
     */
    @ApiOperation(value = "管理员添加新的SKU到SPU里")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "商店id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "spuId", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @Audit
    @PostMapping("/shops/{shopId}/spus/{id}/skus")
    public Object addSkutoSpu(@PathVariable Long shopId, @PathVariable Long id,
                              @RequestBody SkuVo skuVo,BindingResult bindingResult) {
        var res = Common.processFieldErrors(bindingResult,httpServletResponse);
        if(res != null){
            return res;
        }
        if (shopId == null || id == null) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }


        var ret = goodsService.addSkuToSpu(shopId, id, skuVo);
        if(ret.getCode().equals(ResponseCode.OK))httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.decorateReturnObject(ret);
    }

    /**
     * sku上传图片
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "sku上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "商店id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "skuId", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/skus/{id}/uploadImg")
    @Audit
    public Object skuUploadImg(@RequestParam("img") MultipartFile multipartFile, @PathVariable Long shopId, @PathVariable Long id) {
        logger.debug("uploadImg: id = " + id + " img :" + multipartFile.getOriginalFilename());
        ReturnObject returnObject = goodsService.upLoadSkuImg(multipartFile, shopId, id);
        return Common.getNullRetObj(returnObject, httpServletResponse);
    }

    /**
     * 功能描述: 管理员或店家逻辑删除SKU
     * @Param: [shopId, id]
     * @Return: java.lang.Object
     * @Author: Yifei Wang
     * @Date: 2020/12/1 20:03
     */
    @ApiOperation(value = "管理员或店家逻辑删除SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "商店id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "skuId", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/skus/{id}")
    @Audit
    public Object deleteSku(@PathVariable Long shopId, @PathVariable Long id) {
        if (id == null || shopId ==null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.deleteSkuById(shopId, id);
        return Common.getNullRetObj(ret, httpServletResponse);
    }

    /**
     * 管理员或店家修改SKU信息
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员或店家修改SKU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "商店id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "skuId", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/skus/{id}")
    @Audit
    public Object changeSku(@PathVariable Integer shopId, @PathVariable Integer id, @RequestBody @Validated SkuChangeVo skuChangeVo, BindingResult bindingResult) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != obj) {
            logger.info("validate fail");
            return obj;
        }
        ReturnObject ret = goodsService.updateSku(shopId, id, skuChangeVo);
        return Common.getNullRetObj(ret, httpServletResponse);
    }

    /**
     * 查看一条商品SPU的详细信息（无需登录）
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看一条商品SPU的详细信息（无需登录）")
    @ApiImplicitParams({

            @ApiImplicitParam(name = "id", value = "商品SPUid", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/spus/{id}")
    public Object getSpu(@PathVariable Long id) {
        if (id == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.getSpuById(id);
        return Common.decorateReturnObject(ret);

    }

    /**
     * 查看一条分享商品SPU的详细信息（需登录）
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看一条分享商品SPU的详细信息（需登录）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "商品SPUid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "sid", value = "分享id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/share/{sid}/skus/{id}")
    @Audit
    public Object getSharedSpu(@PathVariable Long sid, @PathVariable Long id,@LoginUser Long userId) {
        if (id == null || sid == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        if(!beShareService.createBeShare(userId,sid,id)){
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE));
        }
        ReturnObject ret = goodsService.getSkuDetails(id);
        return Common.decorateReturnObject(ret);

    }

    /**
     * 店家新建商品SPU
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     * modifiedBy xuyue 2020/12/17 11:40
     */
    @ApiOperation(value = "店家新建商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{id}/spus")
    @Audit
    public Object addSpu(@PathVariable Long id, @RequestBody SpuVo spuVo,
                         BindingResult bindingResult) {
        if (id == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }

        var res = Common.processFieldErrors(bindingResult,httpServletResponse);
        if(res != null){
            return res;
        }

        var ret = goodsService.newSpu(id, spuVo);

        if(ret.getCode().equals(ResponseCode.OK))httpServletResponse.setStatus(HttpStatus.CREATED.value());
        return Common.decorateReturnObject(ret);
    }

    /**
     * spu上传图片
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "spu上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "skuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{id}/uploadImg")
    @Audit
    public Object uploadSpuImg(@RequestParam("img") MultipartFile multipartFile, @PathVariable Integer shopId, @PathVariable Integer id) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        logger.debug("uploadImg: id = " + id + " img :" + multipartFile.getOriginalFilename());
        ReturnObject returnObject = goodsService.upLoadSpuImg(multipartFile, shopId, id);
        return Common.getNullRetObj(returnObject, httpServletResponse);
    }


    /**
     * 店家修改商品SPU
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家修改商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/spus/{id}")
    @Audit
    public Object changeSpu(@PathVariable Integer id, @PathVariable Integer shopId, @RequestBody SpuVo spuVo) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.updateSpu(id, shopId, spuVo);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 店家逻辑删除SPU
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家逻辑删除SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{id}")
    @Audit
    public Object deleteSpu(@PathVariable Integer id, @PathVariable Integer shopId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.deleteSpuById(id, shopId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 店家商品上架
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家逻辑上架SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/skus/{id}/onshelves")
    @Audit
    public Object onShelvesSpu(@PathVariable Long id, @PathVariable Long shopId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.onShelfSku(id, shopId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 店家商品下架
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家SKU商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/skus/{id}/offshelves")
    @Audit
    public Object offShelvesSpu(@PathVariable Long id, @PathVariable Long shopId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.offShelfSku(id, shopId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员新增商品价格浮动
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "skuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/skus/{id}/floatPrices")
    @Audit
    public Object addFloatPrices(@PathVariable Long id, @PathVariable Long shopId, @LoginUser Long userId, @RequestBody @Validated FloatPriceVo floatPriceVo, BindingResult bindingResult) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != obj) {
            logger.info("validate fail");
            return obj;
        }
        ReturnObject ret = goodsService.newFloatPrice(id, shopId, floatPriceVo, userId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员失效商品价格浮动
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员失效商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "浮动价格id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/floatPrices/{id}")
    @Audit
    public Object deleteFloatPrices(@PathVariable Long id, @PathVariable Long shopId, @LoginUser Long userId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.deleteFloatPrice(id, shopId, userId);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 将SPU加入分类
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU加入分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "spuId", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "分类", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    @Audit
    public Object addSputoCategory(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long id) {
        if(shopId==null || spuId==null || id==null){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.addSpuToCategory(shopId, spuId, id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 将SPU移出分类
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU移出分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "spuId", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "分类", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    @Audit
    public Object deleteSpufromCategory(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long id) {
        if(shopId==null || spuId==null || id==null){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.removeSpuFromCategory(shopId, spuId, id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 将SPU加入品牌
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU加入品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "spuId", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    @Audit
    public Object addSputoBrand(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long id) {
        if(shopId==null || spuId==null || id==null){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.addSpuToBrand(shopId, spuId, id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 将SPU移出品牌
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU移出品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "spuId", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "id", value = "品牌id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    @Audit
    public Object deleteSpufromBrand(@PathVariable Long shopId, @PathVariable Long spuId, @PathVariable Long id) {
        if(shopId==null || spuId==null || id==null){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.removeSpuFromBrand(shopId, spuId, id);
        return Common.decorateReturnObject(ret);
    }


}
