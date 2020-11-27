package cn.edu.xmu.goods.controller;


import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

/**
 * 店铺控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "店铺服务", tags = "shop")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
public class ShopController {

    /**
     * 获得店铺的所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "获得店铺的所有状态", nickname = "getshopState", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/shops/states")
    public Object getshopState(){
        return null;
    }

    /**
     * 店家申请店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家申请店铺", nickname = "addShop", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 908, message = "用户已经有店铺"),
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops")
    public Object addShop(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization, @ApiParam(value = "" ,required=true )   @RequestBody String name){
        return null;
    }

    /**
     * 店家修改店铺信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家修改店铺信息", nickname = "modifyShop", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{id}")
    public Object modifyShop(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "shop ID",required=true) @PathVariable("id") Integer id,@ApiParam(value = "" ,required=true )  @RequestBody String name){
        return null;
    }

    /**
     * 管理员或店家关闭店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员或店家关闭店铺", nickname = "deleteShop", notes = "如果店铺从未上线则物理删除",  tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @DeleteMapping(value = "/shops/{id}")
    public Object deleteShop(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "shop ID",required=true) @PathVariable("id") Integer id){
        return null;
    }

    /**
     * 平台管理员审核店铺信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "平台管理员审核店铺信息", nickname = "shopsShopIdNewshopsIdAuditPut", notes = "",  tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{shopId}/newshops/{id}/audit")
    public Object shopsShopIdNewshopsIdAuditPut(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "shop ID",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "新店 ID",required=true) @PathVariable("id") Integer id,@ApiParam(value = "" ,required=true )   @RequestBody boolean conclusion){
        return null;
    }


    /**
     * 管理员上线店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员上线店铺", nickname = "shopsIdOnshelvesPut", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{id}/onshelves")
    public Object shopsIdOnshelvesPut(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "店 ID",required=true) @PathVariable("id") Integer id){
        return null;
    }

    /**
     * 管理员下线店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员下线店铺", nickname = "shopsIdOffshelvesPut", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PutMapping(value = "/shops/{id}/offshelves")
    public Object shopsIdOffshelvesPut(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "店 ID",required=true) @PathVariable("id") Integer id){
        return null;
    }







}
