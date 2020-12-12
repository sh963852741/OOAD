package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.bo.Coupon;
import cn.edu.xmu.activity.model.bo.CouponActivity;
import cn.edu.xmu.activity.model.vo.ActivityFinderVo;
import cn.edu.xmu.activity.model.vo.CouponActivityVo;
import cn.edu.xmu.activity.service.ActivityService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(value = "coupon", produces = "application/json;charset=UTF-8", consumes = "application/json;charset=UTF-8")
public class CouponController {

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
        ReturnObject<Coupon.CouponStatus[]> ret = activityService.getCouponStatus();
        return Common.decorateReturnObject(ret) ;
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
    @Audit
    @PostMapping(value = "/shops/{shopId}/couponactivities")
    public Object addCouponActivity(@PathVariable("shopId") Long shopId,
                                    @Valid @RequestBody CouponActivityVo couponActivityVo,
                                    BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        if(couponActivityVo.getBeginTime().isAfter(couponActivityVo.getEndTime())||couponActivityVo.getCouponTime().isAfter(couponActivityVo.getEndTime())){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "活动开始时间必须小于活动结束时间，优惠券一定要在活动结束前发放");
        }

        ReturnObject ret = activityService.addCouponActivity(couponActivityVo, shopId);
        return ret;
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
    public Object getOnlineCouponActivities(@Valid @RequestBody ActivityFinderVo activityFinderVo,
                                            BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        activityFinderVo.setState(CouponActivity.CouponStatus.OFFLINE.getCode());
        ReturnObject ret = activityService.getCouponActivities(activityFinderVo);

        return Common.getListRetObject(ret);
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
    @Audit
    public Object showOwnInvalidCouponActivities(@Valid @RequestBody ActivityFinderVo activityFinderVo,
                                                 BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        activityFinderVo.setState(CouponActivity.CouponStatus.DELETE.getCode());
        ReturnObject<PageInfo<VoObject>> ret = activityService.getCouponActivities(activityFinderVo);
        return Common.getPageRetObject(ret);
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
    @GetMapping(value = "/couponactivities/{id}/skus")
    public Object getSpuInCouponActivity(@ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id,
                                         @ApiParam(value = "页码") @Valid @RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        ReturnObject<PageInfo<VoObject>> ret = activityService.getSKUInCouponActivity(id,page,pageSize);

        return Common.getPageRetObject(ret);
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
    public Object getCouponActivity(
            @ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        var ret = activityService.getCouponActivity(id, shopId);
        return Common.decorateReturnObject(ret);
    }

//    @DeleteMapping(value = "/shops/{shopId}/presales/{id}")
//    public Object delCouponActivity(@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
//                                    @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
//        CouponActivityVo vo = new CouponActivityVo();
//        vo.setState(CouponActivity.CouponStatus.CANCELED.getCode());
//
//        var ret = activityService.modifyCouponActivity(id,vo,shopId);
//        return Common.decorateReturnObject(ret);
//    }

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
    public Object modifyCouponActivity(@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
                                                   @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id,
                                                   @ApiParam(value = "可修改的优惠活动信息" ,required=true )  @Valid @RequestBody CouponActivityVo couponActivityVo,
                                                   BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        if(couponActivityVo.getBeginTime().isAfter(couponActivityVo.getEndTime())||couponActivityVo.getCouponTime().isAfter(couponActivityVo.getEndTime())){
            return new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "活动开始时间必须小于活动结束时间，优惠券一定要在活动结束前发放");
        }

        ReturnObject ret = activityService.modifyCouponActivity(id, couponActivityVo, shopId);

        if(ret.getCode() == ResponseCode.OK){
            return new ReturnObject<>();
        }else{
            return ret;
        }
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
    public Object cancelCouponActivity(@ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
                                       @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        CouponActivityVo vo =  new CouponActivityVo();
        vo.setState(CouponActivity.CouponStatus.DELETE.getCode());
        ReturnObject ret = activityService.modifyCouponActivity(id, vo, shopId);
//        ReturnObject ret = activityService.modifyCouponActivityStatus(id, CouponActivity.CouponStatus.CANCELED);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 管理员为己方某优惠券活动新增限定范围
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员为己方某优惠券活动新增限定范围", nickname = "shopsShopIdCouponactivitiesIdSpusPost", notes = "`管理员`可为己方某优惠券活动新增某限定范围", tags={ "coupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/couponactivities/{id}/skus")
    public Object shopsShopIdCouponactivitiesIdSpusPost(
            @ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long activityId,
            @ApiParam(value = "SPU的ID列表" ,required=true )  @RequestBody List<Long> ids){
        var ret = activityService.addSPUToCouponActivity(ids, shopId, activityId);
        return Common.decorateReturnObject(ret);
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
    public Object removeSPUFromCouponActivity(
            @ApiParam(value = "商店ID",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "CouponSpu的id",required=true) @PathVariable("id") Long id){
        var ret = activityService.removeSKUFromCouponActivity(id,shopId);
        return Common.decorateReturnObject(ret);
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
    public Object showCoupons(Long userId,
                              @ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Byte state,
                              @ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,
                              @ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize,
                              BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }
        ReturnObject ret = activityService.getCouponList(userId,state,page,pageSize);

        return ret;
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
    public Object useCoupon(
            @LoginUser Long userId,
            @ApiParam(value = "优惠卷ID",required=true) @PathVariable("id") Long id){
        var ret = activityService.useCoupon(id,userId);
        return Common.decorateReturnObject(ret);
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
    public Object couponactivitiesIdUsercouponsPost(
            @LoginUser Long userId,
            @ApiParam(value = "活动ID",required=true) @PathVariable("id") Long id){
        var ret = activityService.claimCoupon(id,userId);
        return Common.decorateReturnObject(ret);
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
    public Object shopsShopIdCouponsIdPut(
            @LoginUser Long userId,
            @ApiParam(value = "店铺ID",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "优惠卷ID",required=true) @PathVariable("id") Long id){
        var x = activityService.refundCoupon(id,userId);
        return Common.decorateReturnObject(x);
    }
}
