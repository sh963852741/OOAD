package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.CommentVo;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "评论服务", tags = "comment")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
public class CommentController {
    /**
     * 获得评论所有状态
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "获得评论的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("comments/states")
    public Object getCommentStates(){
        return null;
    }

    /**
     * 买家新增SKU的评论
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "买家新增SKU的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 903, message = "用户没有购买此商品")
    })
    @PostMapping("orderitems/{id}/comments")
    public Object addCommentOnSku(@PathVariable long id, @RequestBody CommentVo commentVo){
        return null;
    }

    /**
     * 查看SKU评价列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "查看SKU评价列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="page", required = false, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="pageSize", required = false, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("skus/{id}/comments")
    public Object selectSkuComments(@PathVariable long id, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize){
        return null;
    }

    /**
     * 管理员核审评论
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "管理员核审评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("comments/{id}/confirm")
    public Object confirmComment(@PathVariable long id,@RequestBody boolean conclusion){
        return null;
    }

    /**
     * 买家查看自己的评价记录
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "买家查看自己的评价记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("comments")
    public Object getOwnComments(@PathVariable long id,@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize){
        return null;
    }

    /**
     * 管理员查看未核审/已核审的评论列表
     * @param
     * @return Object
     * createdBy Yifei Wang 2020/11/15 21:37
     */
    @ApiOperation(value = "管理员查看未核审/已核审的评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("shops/{id}/comments/all")
    public Object getAllComments(@PathVariable long id,@RequestParam(required = false) Integer state,@RequestParam(required = false) Integer page,@RequestParam(required = false) Integer pageSize){
        return null;
    }















}
