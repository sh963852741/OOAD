package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.GrouponVO;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 团购控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "团购服务", tags = "groupon")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/groupon", produces = "application/json;charset=UTF-8")
public class GrouponController {

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
    public Object getgrouponState(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization){
        return null;
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
    public Object queryGroupons(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的") @Valid @RequestParam(value = "timeline", required = false) Integer timeline,@ApiParam(value = "根据spu_id查询") @Valid @RequestParam(value = "spu_id", required = false) Integer spuId,@ApiParam(value = "根据shop id查询") @Valid @RequestParam(value = "shopId", required = false) Integer shopId,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }

    /**
     * 管理员查询所有团购(包括下线的)
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询所有团购(包括下线的)", nickname = "queryGroupon", notes = "", tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @GetMapping(value = "/shops/{id}/groupons")
    public Object queryGroupon(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "根据商铺id查询",required=true) @PathVariable("id") Integer id,@ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Integer state,@ApiParam(value = "根据SPUid查询") @Valid @RequestParam(value = "spuid", required = false) Integer spuid,@ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page,@ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }

    /**
     * 管理员查询所有团购(包括下线的)
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询某SPU所有团购活动", nickname = "queryGrouponofSPU", notes = "",tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "") })
    @GetMapping(value = "/shops/{shopId}/spus/{id}/groupons")
    public Object queryGrouponofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Integer id,@ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Integer state){
        return null;
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
    public Object createGrouponofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Integer id,@ApiParam(value = "可修改的团购活动信息" ,required=true )  @Valid @RequestBody GrouponVO grouponVO){
        return null;
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
    public Object changeGrouponofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "团购活动id",required=true) @PathVariable("id") Integer id,@ApiParam(value = "可修改的团购活动信息" ,required=true )  @Valid @RequestBody GrouponVO grouponVO){
        return null;
    }

    /**
     * 管理员下架SKU团购活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员下架SKU团购活动", nickname = "cancelGrouponofSPU", notes = "",  tags={ "groupon", })
    @ApiResponses(value = {
            @ApiResponse(code = 907, message = "团购活动状态禁止"),
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{shopId}/groupons/{id}")
    public Object cancelGrouponofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "根据商铺id查询",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "团购活动id",required=true) @PathVariable("id") Integer id){
        return null;
    }






}
