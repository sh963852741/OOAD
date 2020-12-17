package cn.edu.xmu.flashsale.controller;

import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.vo.FlashSaleItemVo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleRetVo;
import cn.edu.xmu.flashsale.model.vo.FlashSaleVo;
import cn.edu.xmu.flashsale.service.FlashSaleService;
import cn.edu.xmu.ooad.util.Common;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 秒杀控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "秒杀服务", tags = "flashsale")
@RestController /*Restful的Controller对象*/
@Slf4j
@RequestMapping(value = "/flashsale", produces = "application/json;charset=UTF-8")
public class FlashSaleController {

    @Autowired
    FlashSaleService flashSaleService;

    /**
     * 查询某一时段秒杀活动详情
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "查询某一时段秒杀活动详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping(value= "/timesegments/{id}/flashsales")
    public Mono<Object> getFlashSales(@PathVariable long id){
            return flashSaleService.getFlashSale(id).map(x -> x);
    }

    /**
     * 平台管理员在某个时间段下新建秒杀
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员在某个时间段下新建秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", value="秒杀时间段id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{did}/timesegments/{id}/flashsales")
    public Object createFlashSale(@PathVariable long id, @RequestBody FlashSaleVo flashDate){
        var ret = flashSaleService.addFlashSale(id, flashDate.getFlashDate().atStartOfDay());

        return Common.decorateReturnObject(ret);
    }

    /**
     * 获取当前时段秒杀列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "获取当前时段秒杀列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="page", value="页码", required = true, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSiez",value = "每页数目",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("/flashsales/current")
    public Mono<Object> getCurrentFlashsales(@RequestParam Integer page, @RequestParam Integer pageSize){
        log.debug("/flashsales/current");
        return flashSaleService.getFlashSale(1L).map(x->{
            log.debug(x.toString());
            return x;
        });
    }

    /**
     * 平台管理员删除某个时段秒杀
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员删除某个时段秒杀")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id",value = "秒杀id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{did}/flashsales/{id}")
    public Object deleteFlashsale(@PathVariable Integer id){
        var ret = flashSaleService.delFlashSale(id);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 管理员修改秒杀活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "管理员修改秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id",value = "秒杀id",required = true,dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="flashDate",value = "秒杀时间段",required = true,dataType = "String",paramType = "body")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{did}/flashsales/{id}")
    public Object changeFlashsale(@PathVariable Long id,@RequestBody FlashSaleVo flashDate){
        var ret = flashSaleService.modifyFlashSale(id, flashDate.getFlashDate().atStartOfDay());
        return Common.decorateReturnObject(ret);
    }

    /**
     * 平台管理员向秒杀活动添加商品SKU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员向秒杀活动添加SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id",value = "秒杀id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PostMapping("/shops/{did}/flashsales/{id}/flashitems")
    public Object addFlashitems(@PathVariable Long id, @RequestBody FlashSaleItemVo flashsaleVo){
        var ret = flashSaleService.addSkuToFlashSale(id, flashsaleVo);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 获取秒杀活动商品
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "获取秒杀活动商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id",value = "秒杀活动id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("flashsales/{id}/flashitems")
    public Object getFlashitems(@PathVariable Integer id, @RequestParam Integer page,@RequestParam Integer pageSize){
        return null;
    }

    /**
     * 平台管理员在秒杀活动删除商品SKU
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员在秒杀活动删除商品SKU")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="fid",value = "秒杀活动id",required = true,dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="id",value = "秒杀活动项id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @DeleteMapping("/shops/{did}/flashsales/{fid}/flashitems/{id}")
    public Object deleteFlashitems(@PathVariable("fid") Long flashSaleId, @PathVariable("id") Long itemId){
        var ret = flashSaleService.removeSkuFromFlashSale(flashSaleId, itemId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 平台管理员上线秒杀活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员上线秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="fid",value = "秒杀活动id",required = true,dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="id",value = "秒杀活动项id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{did}/flashsales/{id}/onshelves")
    public Object onlineFlashSale(@PathVariable("id") Long flashSaleId){
        var ret = flashSaleService.onlineFlashSale(flashSaleId);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 平台管理员下线秒杀活动
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "平台管理员上线秒杀活动")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="fid",value = "秒杀活动id",required = true,dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name="id",value = "秒杀活动项id",required = true,dataType = "Integer", paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("/shops/{did}/flashsales/{id}/offshelves")
    public Object offlineFlashSale(@PathVariable("id") Long flashSaleId){
        var ret = flashSaleService.offlineFlashSale(flashSaleId);
        return Common.decorateReturnObject(ret);
    }
}
