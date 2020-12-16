package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.bo.GrouponActivity;
import cn.edu.xmu.activity.model.vo.ActivityFinderVo;
import cn.edu.xmu.activity.model.vo.GrouponActivityVo;
import cn.edu.xmu.activity.service.ActivityService;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * 团购控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "团购服务", tags = "groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
@Slf4j
public class GrouponController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 获得团购活动的所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "获得团购活动的所有状态", nickname = "getgrouponState", notes = "", tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/groupons/states")
    public Object getgrouponState(){
        return Common.decorateReturnObject(activityService.grouponActivityStatus());
    }

    /**
     * 查询所有团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "查询所有团购活动", nickname = "queryGroupons", notes = "", tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @GetMapping(value = "/groupons")
    public Object queryGroupons(
            @ApiParam(value = "时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的") @Valid @RequestParam(value = "timeline", required = false) Byte timeline,
            @ApiParam(value = "根据spu_id查询") @Valid @RequestParam(value = "spu_id", required = false) Long spuId,
            @ApiParam(value = "根据shop id查询") @Valid @RequestParam(value = "shopId", required = false) Long shopId,
            @ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false,defaultValue = "1") Integer page,
            @ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false,defaultValue = "10") Integer pageSize){
        ActivityFinderVo vo=new ActivityFinderVo();
        vo.setPage(page);
        vo.setPageSize(pageSize);
        vo.setShopId(shopId);
        vo.setTimeline(timeline);
        vo.setSpuId(spuId);

        ReturnObject<PageInfo<VoObject>> ret=activityService.getGrouponActivities(vo,false);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员查询所有团购(包括下线的)
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询所有团购", nickname = "queryGroupon", notes = "", tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @GetMapping(value = "/shops/{id}/groupons")
    public Object queryGrouponByAdmin(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization, @ApiParam(value = "根据商铺id查询",required=true) @PathVariable("id") Long id, @ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Byte state, @ApiParam(value = "根据SPUid查询") @Valid @RequestParam(value = "spuid", required = false) Long spuid, @ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false,defaultValue = "1") Integer page, @ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false,defaultValue = "10") Integer pageSize, @RequestParam(value = "state", required = false) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")LocalDateTime beginTime, @RequestParam(value = "state", required = false) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime){
        ActivityFinderVo vo=new ActivityFinderVo();
        vo.setSpuId(spuid);
        vo.setState(state);
        vo.setShopId(id);
        vo.setBeginTime(beginTime);
        vo.setEndTime(endTime);
        vo.setPage(page);
        vo.setPageSize(pageSize);
        ReturnObject ret=activityService.getGrouponActivitiesByAdmin(vo);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员查询某SPU所有团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询某SPU所有团购活动", nickname = "queryGrouponofSPU", notes = "",tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @GetMapping(value = "/shops/{shopId}/spus/{id}/groupons")
    public Object queryGrouponofSPU(
            @ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Long id,
            @ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Byte state){
        ActivityFinderVo vo=new ActivityFinderVo();
        vo.setSpuId(id);
        vo.setShopId(shopId);
        vo.setState(state);
        return Common.getListRetObject(activityService.getGrouponBySpuIdAdmin(vo));
    }


    /**
     * 管理员对SPU新增团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员对SPU新增团购活动", nickname = "createGrouponofSPU", notes = "",  tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/spus/{id}/groupons")
    public Object createGrouponofSPU(
            @ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,
            @ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Integer id,
            @RequestBody @Validated GrouponActivityVo grouponActivityVo,BindingResult bindingResult){
        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != obj) {
            return obj;
        }
        ReturnObject<GrouponActivityVo> ret=activityService.addGrouponActivity(grouponActivityVo,id,shopId);
        return Common.decorateReturnObject(ret);
    }


    /**
     * 管理员修改SPU团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员修改SPU团购活动", nickname = "changeGrouponofSPU", notes = "",  tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/groupons/{id}")
    public Object changeGrouponofSPU(
            @ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Long shopId,
            @ApiParam(value = "团购活动id",required=true) @PathVariable("id") Long id,
            @ApiParam(value = "可修改的团购活动信息" ,required=true ) @Valid @RequestBody GrouponActivityVo grouponActivityVO,BindingResult bindingResult){

        Object obj = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != obj) {
            return obj;
        }

        return Common.decorateReturnObject(activityService.modifyGrouponActivity(id,grouponActivityVO,shopId));
    }

    /**
     * 管理员下架SKU团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员删除SKU团购活动", nickname = "cancelGrouponofSPU", notes = "",  tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{shopId}/groupons/{id}")
    public Object cancelGrouponofSPU(
            @ApiParam(value = "根据商铺id查询",required=true) @PathVariable("shopId") Integer shopId,
            @ApiParam(value = "团购活动id",required=true) @PathVariable("id") Integer id){
        return Common.decorateReturnObject(activityService.modifyGrouponActivity(id,shopId, GrouponActivity.GrouponStatus.DELETE.getCode()));
    }

    /**
     * 管理员上线SKU团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @PutMapping(value = "/shops/{shopId}/groupons/{id}/onshelves")
    public Object onlineActivity(
            @ApiParam(value = "根据商铺id查询",required=true) @PathVariable("shopId") Integer shopId,
            @ApiParam(value = "团购活动id",required=true) @PathVariable("id") Integer id){
        return Common.decorateReturnObject(activityService.modifyGrouponActivity(id,shopId, GrouponActivity.GrouponStatus.ONLINE.getCode()));
    }


    /**
     * 管理员下线SKU团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @PutMapping(value = "/shops/{shopId}/groupons/{id}/offshelves")
    public Object offlineActivity(
            @ApiParam(value = "根据商铺id查询",required=true) @PathVariable("shopId") Integer shopId,
            @ApiParam(value = "团购活动id",required=true) @PathVariable("id") Integer id){
        return Common.decorateReturnObject(activityService.modifyGrouponActivity(id,shopId, GrouponActivity.GrouponStatus.OFFLINE.getCode()));
    }


}
