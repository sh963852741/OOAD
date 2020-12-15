package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.bo.PresaleActivity;
import cn.edu.xmu.activity.model.vo.ActivityFinderVo;
import cn.edu.xmu.activity.model.vo.PresaleActivityVo;
import cn.edu.xmu.activity.service.ActivityService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static cn.edu.xmu.ooad.util.Common.*;

/**
 * 预售控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "预售服务", tags = "presale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PreSaleController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获得预售活动的所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "获得预售活动的所有状态", nickname = "getpresaleState", notes = "", tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/presales/states")
    public Object getPresaleState(){
        return decorateReturnObject(activityService.getPresaleActivityStatus());
    }


    /**
     * 查询所有有效的预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询所有有效的预售活动", nickname = "queryPresale", notes = "", tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/presales")
    public Object queryPresale(
            @RequestParam(name = "page",defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize",defaultValue = "1") Integer pageSize,
            @RequestParam(name = "timeline",defaultValue = "1") Byte timeline,
            @RequestParam(name = "skuId", required = false) Long skuId,
            @RequestParam(name = "shopId", required = false) Long shopId){
        ActivityFinderVo activityFinderVo =new ActivityFinderVo();
        activityFinderVo.setPage(page);
        activityFinderVo.setPageSize(pageSize);
        activityFinderVo.setSkuId(skuId);
        activityFinderVo.setShopId(shopId);
        activityFinderVo.setTimeline(timeline);

        var x= activityService.getPresaleActivities(activityFinderVo);
        return getPageRetObject(x);
    }


    /**
     * 管理员查询SKU所有预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询SPU所有预售活动(包括下线的)", nickname = "queryPresaleofSPU", notes = "",  tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/shops/{shopId}/presales")
    @Audit
    public Object queryPresaleActivity(@ApiParam(value = "商品SPUid",required=true) @RequestParam("skuId") Long skuId,
                                       @ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Byte state){
        ActivityFinderVo activityFinderVo = new ActivityFinderVo();
        activityFinderVo.setSkuId(skuId);
        activityFinderVo.setState(state);
        var ret=  activityService.getAllPresaleActivities(activityFinderVo);
        return decorateReturnObject(ret);
    }

    /**
     * 管理员新增SKU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增SKU预售活动", nickname = "createPresaleofSPU", notes = "", tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/skus/{id}/presales")
    @Audit
    public Object addPresaleActivity(@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
                                     @ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Long spuId,
                                     @ApiParam(value = "可修改的预售活动信息" ,required=true )  @Valid @RequestBody PresaleActivityVo presaleActivityVo,
                                     BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        if(presaleActivityVo.getBeginTime().isAfter(presaleActivityVo.getPayTime()) || presaleActivityVo.getPayTime().isAfter(presaleActivityVo.getEndTime())){
            return decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "预售开始时间必然大于尾款支付时间，必然大于结束时间"));
        }

        ReturnObject ret = activityService.addPresaleActivity(presaleActivityVo, spuId, shopId);
        return decorateReturnObject(ret);
    }

    /**
     * 管理员修改SPU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改SPU预售活动", nickname = "changePresaleofSPU", notes = "",tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 906, message = "预售活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/presales/{id}")
    @Audit
    public Object changePresaleActivity(@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
                                     @ApiParam(value = "预售活动id",required=true) @PathVariable("id") Long activityId,
                                     @ApiParam(value = "可修改的预售活动信息" ,required=true )  @Valid @RequestBody PresaleActivityVo presaleActivityVo,
                                     BindingResult bindingResult, HttpServletResponse httpServletResponse){
        var res = Common.processFieldErrors(bindingResult, httpServletResponse);
        if(res != null){
            return res;
        }

        if(presaleActivityVo.getBeginTime().isAfter(presaleActivityVo.getPayTime()) || presaleActivityVo.getPayTime().isAfter(presaleActivityVo.getEndTime())){
            return decorateReturnObject(new ReturnObject<>(ResponseCode.FIELD_NOTVALID, "预售开始时间必然大于尾款支付时间，必然大于结束时间"));
        }
        ReturnObject ret = activityService.modifyPresaleActivity(activityId,presaleActivityVo,shopId);

        return decorateReturnObject(ret);
    }

    /**
     * 管理员逻辑删除SPU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员逻辑删除SPU预售活动", nickname = "offlinePresaleofSPU", notes = "`商店管理员`可逻辑删除预售活动",  tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 906, message = "预售活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{shopId}/presales/{id}")
    @Audit
    public Object delPresaleActivity(@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
                                      @ApiParam(value = "预售活动id",required=true) @PathVariable("id") Long id){
        ReturnObject ret = activityService.modifyPresaleActivity(id,shopId, PresaleActivity.PresaleStatus.DELETE.getCode());

        return decorateReturnObject(ret);
    }

    /**
     * 管理员上架SPU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员上线SPU预售活动", nickname = "offlinePresaleofSPU", notes = "`商店管理员`可逻辑删除预售活动",  tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 906, message = "预售活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/presales/{id}/onshelves")
    @Audit
    public Object onlinePresaleActivity(@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
                                         @ApiParam(value = "预售活动id",required=true) @PathVariable("id") Long id){
        ReturnObject ret = activityService.modifyPresaleActivity(id,shopId, PresaleActivity.PresaleStatus.ONLINE.getCode());

        return decorateReturnObject(ret);
    }

    /**
     * 管理员下架SPU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员逻辑删除SPU预售活动", nickname = "offlinePresaleofSPU", notes = "`商店管理员`可逻辑删除预售活动",  tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 906, message = "预售活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/presales/{id}/offshelves")
    @Audit
    public Object offlinePresaleActivity(@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
                                         @ApiParam(value = "预售活动id",required=true) @PathVariable("id") Long id){
        ReturnObject ret = activityService.modifyPresaleActivity(id,shopId, PresaleActivity.PresaleStatus.OFFLINE.getCode());

        return decorateReturnObject(ret);
    }
}
