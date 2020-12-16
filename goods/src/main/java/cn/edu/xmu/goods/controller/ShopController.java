package cn.edu.xmu.goods.controller;

import ch.qos.logback.classic.Logger;
import cn.edu.xmu.goods.model.bo.Shop;
import cn.edu.xmu.goods.model.vo.ShopConclusionVo;
import cn.edu.xmu.goods.model.vo.ShopVo;
import cn.edu.xmu.goods.service.ShopService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 店铺控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "店铺服务", tags = "shop")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/shop", produces = "application/json;charset=UTF-8")
public class ShopController {
    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private ShopService shopService;
    private Logger logger;

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
    public Object getshopState()
    {
        ReturnObject<List> returnObject=shopService.getShopStates();
        return Common.decorateReturnObject(returnObject);
    }

    /**
     * 店家申请店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家申请店铺", nickname = "addShop", notes = "", tags={ "shop", })
    @ApiImplicitParam(name = "authorization", value = "shopToken", required = true, dataType = "String", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 908, message = "用户已经有店铺"),
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops")
    @Audit
    public Object addShop(@Depart Long did, @RequestBody @Validated ShopVo shopvo, BindingResult bindingResult){
        Object obj = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != obj) {
            return obj;
        }
        if(did == -1)
        {
            ReturnObject ret=shopService.newShop(shopvo);
            return Common.decorateReturnObject(ret);
        }
        else if(did == 1) return Common.decorateReturnObject(new ReturnObject(ResponseCode.APPLYAGAIN_ERROR, "您已经拥有店铺，无法重新申请"));
        else return Common.decorateReturnObject(new ReturnObject(ResponseCode.FIELD_NOTVALID, "商铺名称不能为空"));
    }

    /**
     * 店家修改店铺信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "店家修改店铺信息", nickname = "modifyShop", notes = "", tags={ "shop", })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "shopToken", required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", value = "商店id", required = true, dataType = "Long", paramType = "path")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 140, message = "该店铺无法修改")})
    @PutMapping(value = "/shops/{id}")
    public Object modifyShop(@PathVariable Long id,@RequestBody @Validated ShopVo shopVo,BindingResult bindingResult){
        Object obj = Common.processFieldErrors(bindingResult,httpServletResponse);
        if (null != obj) {
            return obj;
        }
        else
        {
            ReturnObject ret=shopService.updateShop(id,shopVo);
            return Common.decorateReturnObject(ret);
        }
    }

    /**
     * 管理员或店家关闭店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员或店家关闭店铺", nickname = "deleteShop", notes = "如果店铺从未上线则物理删除",  tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") ,
            @ApiResponse(code = 180, message = "该店铺无法被执行关闭操作")
    })
    @ApiImplicitParams({
            //不太对，要两个都用这个
            @ApiImplicitParam(name = "authorization", value = "shopToken", required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping(value = "/shops/{id}")
    public Object deleteShop(@ApiParam(value = "shop ID",required=true) @PathVariable("id") Long id){
        var shop = shopService.getShopByShopId(id).getData();
        if(shop == null) {
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "店铺不存在"));
        }

        if(shop.getState() == Shop.State.OFFLINE.getCode().byteValue()
                ||shop.getState() == Shop.State.ONLINE.getCode().byteValue())
        {
            ReturnObject ret=shopService.deleteShopById(id);
            return Common.decorateReturnObject(ret);
        }
        else
        {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.SHUTSHOP_ERROR));
        }
    }

    /**
     * 平台管理员审核店铺信息
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "平台管理员审核店铺信息", nickname = "shopsShopIdNewshopsIdAuditPut", notes = "",  tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") ,
            @ApiResponse(code = 150, message = "该店铺不是待审核状态")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "adminToken", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping(value = "/shops/{shopId}/newshops/{id}/audit")
    public Object auditShop(@PathVariable("shopId") Long shopId,@ApiParam(value = "新店 ID",required=true) @PathVariable("id") Long id,@ApiParam(value = "" ,required=true )   @RequestBody ShopConclusionVo conclusion){
        var shop = shopService.getShopByShopId(id).getData();
        if(shop == null) {
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "店铺不存在"));
        }

        if(shop.getState() == Shop.State.EXAME.getCode().byteValue())
        {
            ReturnObject ret=shopService.passShop(id,conclusion);
            return Common.decorateReturnObject(ret);
        }
        else
        {
            return Common.decorateReturnObject(new ReturnObject(ResponseCode.PASSSHOP_ERROR));
        }
    }

    /**
     * 管理员上线店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员上线店铺", nickname = "shopsIdOnshelvesPut", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 160, message = "该店铺无法上线")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "adminToken", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping(value = "/shops/{id}/onshelves")
    @Audit
    public Object shopsIdOnshelvesPut(@PathVariable("id") long id){
        ReturnObject ret= shopService.onShelfShop(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员下线店铺
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员下线店铺", nickname = "shopsIdOffshelvesPut", notes = "", tags={ "shop", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功"),
            @ApiResponse(code = 170, message = "该店铺无法下线")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authorization", value = "adminToken", required = true, dataType = "String", paramType = "header")
    })
    @PutMapping(value = "/shops/{id}/offshelves")
    public Object shopsIdOffshelvesPut(@PathVariable("id") long id){
        var shop = shopService.getShopByShopId(id).getData();
        if(shop == null) {
            return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST, "店铺不存在"));
        }
        if(shop.getState() == Shop.State.ONLINE.getCode().byteValue()) {
            ReturnObject ret=shopService.offShelfShop(id);
            return Common.decorateReturnObject(ret);
        }
        else return Common.decorateReturnObject(new ReturnObject<>(ResponseCode.OFFLINESHOP_ERROR));
    }
}
