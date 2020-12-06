package cn.edu.xmu.flashsale.controller;

import cn.edu.xmu.flashsale.model.bo.FlashSaleItem;
import cn.edu.xmu.flashsale.model.vo.FlashSaleItemRetVo;
import cn.edu.xmu.flashsale.model.vo.FlashsaleVo;
import cn.edu.xmu.flashsale.service.FlashSaleService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 秒杀控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "秒杀服务", tags = "flashsale")
@RestController /*Restful的Controller对象*/
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
    public Object getFlashSales(@PathVariable long id){
            return flashSaleService.getFlashSale(id).map(FlashSaleItem::createVo);
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
    @PostMapping("timesegments/{id}/flashsales")
    public Object creatFlashsale(@PathVariable long id, @RequestBody String flashDate){
        return null;
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
    @GetMapping("flashsales/current")
    public Object getCurrentFlashsales(@RequestParam Integer page,@RequestParam Integer pageSize){
        return null;
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
    @DeleteMapping("flashsales/{id}")
    public Object deleteFlashsale(@PathVariable Integer id){
        return null;
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
    @PutMapping("flashsales/{id}")
    public Object changeFlashsale(@PathVariable Integer id,@RequestBody String flashDate){
        return null;
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
    @PostMapping("flashsales/{id}/flashitems")
    public Object addFlashitems(@PathVariable Integer id, @RequestBody FlashsaleVo flashsaleVo){
        return null;
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
    @DeleteMapping("flashsales/{fid}/flashitems/{id}")
    public Object deleteFlashitems(@PathVariable Integer fid, @PathVariable Integer id){
        return null;
    }



}
