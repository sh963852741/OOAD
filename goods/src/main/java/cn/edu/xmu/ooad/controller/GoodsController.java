package cn.edu.xmu.ooad.controller;

import cn.edu.xmu.ooad.model.vo.*;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

/**
 * 商品控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "商品服务", tags = "goods")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/goods", produces = "application/json;charset=UTF-8")
public class GoodsController {
    /**
     * 获得商品SPU的所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 15:01
     */
    @ApiOperation(value = "获取商品SPU的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/spus/states")
    public Object getSpuStates(){
        return null;
    }

    /**
     * 查询sku
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "查询sku")
    @ApiImplicitParams({
            @ApiImplicitParam(name="shopId", dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="skuSn", dataType="String", paramType="query"),
            @ApiImplicitParam(name="spuId", dataType = "String",paramType = "query"),
            @ApiImplicitParam(name="spuSn",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name="page",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name="pageSize",dataType = "Integer",paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/skus")
    public Object getSkus( @RequestParam Object vo,@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        return null;
    }

    /**
     * 获得sky的详细信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询sku的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id",value = "skuId", required = true, dataType="String", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/skus/{id}")
    public Object getSkuDetails(@PathVariable String id){
        return null;
    }


    /**
     * 管理员添加新的SKU到SPU里
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员添加新的SKU到SPU里")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId",value = "商店id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "spuId",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{id}/skus")
    public Object addSkutoSpu(@PathVariable Integer shopId,@PathVariable Integer id,@RequestBody SkuVo skuVo){
        return null;
    }

    /**
     * sku上传图片
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "sku上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId",value = "商店id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "spuId",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/skus/{id}/uploadImg")
    public Object skuUploadImg(@PathVariable Integer shopId,@PathVariable Integer id){
        return null;
    }

    /**
     * 管理员或店家逻辑删除SKU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员或店家逻辑删除SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId",value = "商店id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "skuId",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/skus/{id}")
    public Object deleteSku(@PathVariable Integer shopId,@PathVariable Integer id){
        return null;
    }

    /**
     * 管理员或店家修改SKU信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员或店家修改SKU信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId",value = "商店id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "skuId",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/skus/{id}")
    public Object changeSku(@PathVariable Integer shopId, @PathVariable Integer id, @RequestBody SkuChangeVo skuChangeVo){
        return null;
    }

    /**
     * 查询商品分类关系
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询商品分类关系")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/categories/{id}/subcategories")
    public Object selectCategories(@PathVariable Integer id){
        return null;
    }

    /**
     * 管理员新增商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/categories/{id}/subcategories")
    public Object addCategories(@PathVariable Integer id,@RequestBody String name){
        return null;
    }

    /**
     * 管理员修改商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/categories/{id}")
    public Object changeCategories(@PathVariable Integer id,@RequestBody String name){
        return null;
    }

    /**
     * 管理员删除商品类目
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员删除商品类目")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "种类id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/categories/{id}")
    public Object deleteCategories(@PathVariable Integer id){
        return null;
    }

    /**
     * 查看一条商品SPU的详细信息（无需登录）
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看一条商品SPU的详细信息（无需登录）")
    @ApiImplicitParams({

            @ApiImplicitParam(name="id", value = "商品SPUid",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/spus/{id}")
    public Object getSpu(@PathVariable Integer id){
        return null;
    }

    /**
     * 店家新建商品SPU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家新建商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{id}/spus")
    public Object addSpu(@PathVariable Integer id, @RequestBody SpuVo spuVo){
        return null;
    }

    /**
     * spu上传图片
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "spu上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "skuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{id}/uploadImg")
    public Object uploadSpuImg(@PathVariable Integer id, @PathVariable Integer shopId){
        return null;
    }


    /**
     * 店家修改商品SPU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家修改商品SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/spus/{id}")
    public Object changeSpu(@PathVariable Integer id, @PathVariable Integer shopId,@RequestBody SpuVo spuVo){
        return null;
    }

    /**
     * 店家逻辑删除SPU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家逻辑删除SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{id}")
    public Object deleteSpu(@PathVariable Integer id, @PathVariable Integer shopId){
        return null;
    }

    /**
     * 店家商品上架
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家逻辑删除SPU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{id}/onshelves")
    public Object onShelvesSpu(@PathVariable Integer id, @PathVariable Integer shopId){
        return null;
    }

    /**
     * 店家商品下架
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{id}/offshelves")
    public Object offShelvesSpu(@PathVariable Integer id, @PathVariable Integer shopId){
        return null;
    }

    /**
     * 管理员新增商品价格浮动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "skuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{id}/floatPrices")
    public Object addFloatPrices(@PathVariable Integer id, @PathVariable Integer shopId, @RequestBody FloatPriceVo floatPriceVo){
        return null;
    }

    /**
     * 管理员失效商品价格浮动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员失效商品价格浮动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value = "浮动价格id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/floatPrices/{id}")
    public Object deleteFloatPrices(@PathVariable Integer id, @PathVariable Integer shopId){
        return null;
    }


    /**
     * 管理员新增品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{id}/brands")
    public Object addBrand(@PathVariable Integer id,@RequestBody BrandVo brandVo){
        return null;
    }

    /**
     * 上传图片
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "上传图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "品牌id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/brands/{id}/uploadImg")
    public Object uploadBrandImg(@PathVariable Integer id,@PathVariable Integer shopId){
        return null;
    }

    /**
     * 管理员修改品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "品牌id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{shopId}/brands/{id}")
    public Object changeBrand(@PathVariable Integer shopId,@PathVariable Integer id,@RequestBody BrandVo brandVo){
        return null;
    }

    /**
     * 查看所有品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看所有品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value = "页码",required = true, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", value = "每页数目",required = true, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/brands")
    public Object getAllBrands(@RequestParam Integer page,@RequestParam Integer pageSize){
        return null;
    }

    /**
     * 管理员删除品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员删除品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "品牌id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/brands/{id}")
    public Object changeBrand(@PathVariable Integer shopId,@PathVariable Integer id){
        return null;
    }


    /**
     * 将SPU加入分类
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU加入分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="spuId", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "分类",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object addSputoCategory(@PathVariable Integer shopId,@PathVariable Integer spuId,@PathVariable Integer id){
        return null;
    }

    /**
     * 将SPU移出分类
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU移出分类")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="spuId", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "分类",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/categories/{id}")
    public Object deleteSpufromCategory(@PathVariable Integer shopId,@PathVariable Integer spuId,@PathVariable Integer id){
        return null;
    }

    /**
     * 将SPU加入品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU加入品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="spuId", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "品牌id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object addSputoBrand(@PathVariable Integer shopId,@PathVariable Integer spuId,@PathVariable Integer id){
        return null;
    }

    /**
     * 将SPU移出品牌
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "将SPU移出品牌")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="shopId", value = "店铺id",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="spuId", value = "spuid",required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", value = "品牌id",required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{shopId}/spus/{spuId}/brands/{id}")
    public Object deleteSpufromBrand(@PathVariable Integer shopId,@PathVariable Integer spuId,@PathVariable Integer id){
        return null;
    }

















}
