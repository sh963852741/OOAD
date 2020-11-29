package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.CouponActivityVo;
import cn.edu.xmu.goods.service.ActivityService;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获得优惠卷的所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "获得优惠卷的所有状态", nickname = "getCouponState", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/coupons/states")
    public Object getCouponState(){
        ReturnObject ret = activityService.getCouponStatus();
        return ret;
    }

    /**
     * 管理员新建己方优惠活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新建己方优惠活动", nickname = "addCouponactivity", notes = "- `管理员`新建己方的优惠活动，此时可批量添加限定范围 - 活动状态即`待上线`", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/spus/{id}/couponactivities")
    public Object addCouponactivity(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization, @ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId, @ApiParam(value = "spu ID",required=true) @PathVariable("id") Long id, @ApiParam(value = "可修改的优惠活动信息" ,required=true )  @Valid @RequestBody CouponActivityVo couponActivityVo){
        return null;
    }

    /**
     * 上传优惠活动图片
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "上传优惠活动图片", nickname = "shopsShopIdCouponactivitiesIdUploadImgPost", notes = "如果该优惠活动有图片，需修改该优惠活动的图片，并删除图片文件",  tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @PostMapping(value = "/shops/{shopId}/couponactivities/{id}/uploadImg")
    public Object shopsShopIdCouponactivitiesIdUploadImgPost(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "店铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "活动id",required=true) @PathVariable("id") Integer id,@ApiParam(value = "文件") @Valid @RequestPart(value="img", required=true) MultipartFile img){
        return null;
    }

    /**
     * 查看上线的优惠活动列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看上线的优惠活动列表", nickname = "showOwncouponactivities", notes = "无需登录",  tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/couponactivities")
    public Object showOwncouponactivities(@ApiParam(value = "商店ID") @Valid @RequestParam(value = "shopId", required = false) Long shopId,@ApiParam(value = "时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的") @Valid @RequestParam(value = "timeline", required = false) Integer timeline,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }

    /**
     * 查看本店下线的优惠活动列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看本店下线的优惠活动列表", nickname = "showOwnInvalidcouponactivities", notes = "`管理员`查看，仅可查看己方下线的优惠活动并获取简要信息",tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/shops/{id}/couponactivities/invalid")
    public Object showOwnInvalidcouponactivities(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("id") Long id,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }


    /**
     * 查看优惠活动中的商品
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看优惠活动中的商品", nickname = "couponactivitiesIdSpusGet", notes = "", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/couponactivities/{id}/spus")
    public Object couponactivitiesIdSpusGet(@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }

    /**
     * 查看优惠活动详情
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查看优惠活动详情", nickname = "shopsShopIdCouponactivitiesIdGet", notes = "",  tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/shops/{shopId}/couponactivities/{id}")
    public Object shopsShopIdCouponactivitiesIdGet(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        return null;
    }

    /**
     * 管理员修改己方某优惠活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改己方某优惠活动", nickname = "shopsShopIdCouponactivitiesIdPut", notes = "`管理员`可修改己方某优惠活动的属性，不含限定范围增删与下线", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 904, message = "优惠活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/couponactivities/{id}")
    public Object shopsShopIdCouponactivitiesIdPut(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id,@ApiParam(value = "可修改的优惠活动信息" ,required=true )  @Valid @RequestBody CouponActivityVo couponActivityVo){
        return null;
    }

    /**
     * 管理员下线己方某优惠活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员下线己方某优惠活动", nickname = "shopsShopIdCouponactivitiesIdDelete", notes = "`管理员`可下线己方的某优惠活动, 需要将已发行未用的优惠卷一并下线", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{shopId}/couponactivities/{id}")
    public Object shopsShopIdCouponactivitiesIdDelete(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        return null;
    }


    /**
     * 管理员下线己方某优惠活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员为己方某优惠券活动新增限定范围", nickname = "shopsShopIdCouponactivitiesIdSpusPost", notes = "`管理员`可为己方某优惠券活动新增某限定范围", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/couponactivities/{id}/spus")
    public Object shopsShopIdCouponactivitiesIdSpusPost(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long activityid,@ApiParam(value = "可修改的优惠券活动信息" ,required=true )  @RequestBody Integer id){
        return null;
    }

    /**
     * 店家删除己方某优惠券活动的某限定范围
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家删除己方某优惠券活动的某限定范围", nickname = "shopsShopIdCouponspusIdDelete", notes = "`商店管理员`可删除己方`待上线`的某优惠券活动对应的限定范围", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{shopId}/couponspus/{id}")
    public Object shopsShopIdCouponspusIdDelete(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "CouponSpu的id",required=true) @PathVariable("id") Long id){
        return null;
    }


    /**
     * 买家查看优惠券列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "买家查看优惠券列表", nickname = "showCoupons", notes = "`买家`查看自己的优惠券列表并获取简要信息",  tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/coupons")
    public Object showCoupons(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Integer state,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }

    /**
     * 买家使用自己某优惠券
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "买家使用自己某优惠券", nickname = "useCoupon", notes = "`买家`仅能在优惠有效期内使用自己的优惠，即`未使用`的优惠券", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 905, message = "优惠卷状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/coupons/{id}")
    public Object useCoupon(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "优惠卷ID",required=true) @PathVariable("id") Long id){
        return null;
    }

    /**
     * 买家删除自己某优惠券
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "买家删除自己某优惠券", nickname = "deleteCoupon", notes = "逻辑删除", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping("/coupons/{id}")
    public Object deleteCoupon(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization, @ApiParam(value = "优惠卷ID",required=true) @PathVariable("id") Long id) {
        return null;
    }


    /**
     * 买家领取活动优惠券
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "买家领取活动优惠券", nickname = "couponactivitiesIdUsercouponsPost", notes = "- 判断优惠活动的quantityType为0，且用户无此优惠卷，生成优惠卷的数目为quantity - 判断优惠活动的quantityType为1，且用户无此优惠卷，去从优惠卷中领一张优惠卷", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 909, message = "未到优惠卷领取时间"),
            @ApiResponse(code = 910, message = "优惠卷领罄"),
            @ApiResponse(code = 911, message = "优惠卷活动终止"),
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/couponactivities/{id}/usercoupons")
    public Object couponactivitiesIdUsercouponsPost(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        return null;
    }

    /**
     * 系统产生总数控制的优惠券
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "系统产生总数控制的优惠券", nickname = "couponactivitiesIdCouponsPost", notes = "其中910错误表示该优惠卷已经产生了足够的数目",  tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 909, message = "未到优惠卷领取时间"),
            @ApiResponse(code = 910, message = "优惠卷领罄"),
            @ApiResponse(code = 911, message = "优惠卷活动终止"),
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/couponactivities/{id}/coupons")
    public Object couponactivitiesIdCouponsPost(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        return null;
    }


    /**
     * 优惠券退回
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "优惠券退回", nickname = "shopsShopIdCouponsIdPut", notes = "系统退款后退回用户的优惠券", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/coupons/{id}")
    public Object shopsShopIdCouponsIdPut(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "店铺ID",required=true) @PathVariable("shopId") Long shopId,@ApiParam(value = "优惠卷ID",required=true) @PathVariable("id") Long id){
        return null;
    }
}
