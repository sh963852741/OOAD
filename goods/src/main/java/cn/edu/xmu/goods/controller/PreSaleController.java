package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.PresaleActivityVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 预售控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "预售服务", tags = "presale")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/presale", produces = "application/json;charset=UTF-8")
public class PreSaleController {

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
    public Object getpresaleState(){
        return null;
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
    public Object queryPresale(@ApiParam(value = "根据商铺id查询") @Valid @RequestParam(value = "shopId", required = false) Integer shopId, @ApiParam(value = "时间：0 还未开始的， 1 明天开始的，2 正在进行中的，3 已经结束的") @Valid @RequestParam(value = "timeline", required = false) Integer timeline, @ApiParam(value = "根据SPUid查询") @Valid @RequestParam(value = "spuId", required = false) Integer spuId, @ApiParam(value = "页码") @Valid @RequestParam(value = "page", required = false) Integer page, @ApiParam(value = "每页数目") @Valid @RequestParam(value = "pageSize", required = false) Integer pageSize){
        return null;
    }


    /**
     * 管理员查询SPU所有预售活动(包括下线的)
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员查询SPU所有预售活动(包括下线的)", nickname = "queryPresaleofSPU", notes = "",  tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @GetMapping(value = "/shops/{shopId}/spus/{id}/presales")
    public Object queryPresaleofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization, @ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId, @ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Integer id, @ApiParam(value = "") @Valid @RequestParam(value = "state", required = false) Integer state){
        return null;
    }

    /**
     * 管理员新增SPU预售活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/17 21:37
     */
    @ApiOperation(value = "管理员新增SPU预售活动", nickname = "createPresaleofSPU", notes = "", tags={ "presale", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "成功") })
    @PostMapping(value = "/shops/{shopId}/spus/{id}/presales")
    public Object createPresaleofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "商品SPUid",required=true) @PathVariable("id") Integer id,@ApiParam(value = "可修改的预售活动信息" ,required=true )  @Valid @RequestBody PresaleActivityVo presaleActivityVo){
        return null;
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
    public Object changePresaleofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "预售活动id",required=true) @PathVariable("id") Integer id,@ApiParam(value = "可修改的预售活动信息" ,required=true )  @Valid @RequestBody PresaleActivityVo presaleActivityVo){
        return null;
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
    public Object offlinePresaleofSPU(@ApiParam(value = "用户token" ,required=true) @RequestHeader(value="authorization", required=true) String authorization,@ApiParam(value = "商铺id",required=true) @PathVariable("shopId") Integer shopId,@ApiParam(value = "预售活动id",required=true) @PathVariable("id") Integer id){
        return null;
    }
}
