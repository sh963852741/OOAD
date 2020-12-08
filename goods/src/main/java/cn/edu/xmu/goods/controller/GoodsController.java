package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.*;
import cn.edu.xmu.goods.service.GoodsService;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获得商品SPU的所有状态
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 15:01
     */
    @ApiOperation(value = "获取商品SPU的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/spus/states")
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
    public Object getSkus(@Validated SkuSelectVo vo, @RequestParam(required = false, defaultValue = "1") Integer page, @RequestParam(required = false, defaultValue = "10") Integer pageSize, BindingResult bindingResult) {
        logger.debug("getAllSkus: page = " + page + "  pageSize =" + pageSize);
        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != obj) {
            logger.info("validate fail");
            return obj;
        }
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
    public Object getSkuDetails(@PathVariable Long id) {
        ReturnObject ret = goodsService.getSkuDetails(id);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 管理员添加新的SKU到SPU里
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
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
    @PostMapping("/shops/{shopId}/spus/{id}/skus")
    public Object addSkutoSpu(@PathVariable Long shopId, @PathVariable Long id, @RequestBody SkuVo skuVo) {
        if (shopId == null || id == null) {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.addSkuToSpu(shopId, id, skuVo);
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
    public Object skuUploadImg(@RequestParam("img") MultipartFile multipartFile, @PathVariable Integer shopId, @PathVariable Integer id) {
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
    public Object deleteSku(@PathVariable Integer shopId, @PathVariable Integer id) {
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
    @GetMapping("/share/{sid}/spus/{id}")
    public Object getSharedSpu(@PathVariable Long sid, @PathVariable Long id) {
        if (id == null || sid == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.getSpuById(id);
        return Common.decorateReturnObject(ret);

    }

    /**
     * 店家新建商品SPU
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
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
    public Object addSpu(@PathVariable Long id, @RequestBody SpuVo spuVo) {
        if (id == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.newSpu(id, spuVo);
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
    @ApiOperation(value = "店家逻辑上架SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/spus/{id}/onshelves")
    public Object onShelvesSpu(@PathVariable Integer id, @PathVariable Integer shopId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.onShelfSpu(id, shopId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 店家商品下架
     *
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "Token", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "spuid", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "shopId", value = "店铺id", required = true, dataType = "Integer", paramType = "path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/spus/{id}/offshelves")
    public Object offShelvesSpu(@PathVariable Integer id, @PathVariable Integer shopId) {
        if (id == null || shopId == null) {
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.offShelfSpu(id, shopId);
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
    @PostMapping("/shops/{shopId}/spus/{id}/floatPrices")
    public Object addFloatPrices(@PathVariable Integer id, @PathVariable Integer shopId, Long userId, @RequestBody @Validated FloatPriceVo floatPriceVo, BindingResult bindingResult) {
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
    public Object deleteFloatPrices(@PathVariable Integer id, @PathVariable Integer shopId, Long userId) {
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
    public Object addSputoCategory(@PathVariable Integer shopId, @PathVariable Integer spuId, @PathVariable Integer id) {
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
    public Object deleteSpufromCategory(@PathVariable Integer shopId, @PathVariable Integer spuId, @PathVariable Integer id) {
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
    public Object addSputoBrand(@PathVariable Integer shopId, @PathVariable Integer spuId, @PathVariable Integer id) {
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
    public Object deleteSpufromBrand(@PathVariable Integer shopId, @PathVariable Integer spuId, @PathVariable Integer id) {
        if(shopId==null || spuId==null || id==null){
            httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID));
        }
        ReturnObject ret = goodsService.removeSpuFromBrand(shopId, spuId, id);
        return Common.decorateReturnObject(ret);
    }


}
