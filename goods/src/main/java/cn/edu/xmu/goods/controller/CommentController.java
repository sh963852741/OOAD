package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.CommentConclusionVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.goods.service.CommentService;
import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.LoginUser;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.ReturnObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * 评论控制器
 * @author Yifei Wang
 * Modified at 2020/11/14 22:48
 **/
@Api(value = "评论服务", tags = "comment")
@RestController /*Restful的Controller对象*/
@RequestMapping(value = "/comment", produces = "application/json;charset=UTF-8")
public class CommentController {

    private  static  final Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    private CommentService commentService;

    @Autowired
    private HttpServletResponse httpServletResponse;


    /**
     * 获得评论所有状态
     */
    @ApiOperation(value = "获得评论的所有状态")
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("comments/states")
    public Object getCommentStates(){
        ReturnObject ret=commentService.getCommentStates();
        return Common.decorateReturnObject(ret);
    }


    /**
     * 买家新增SKU的评论
     */
    @ApiOperation(value = "买家新增SKU的评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(paramType = "body", dataType = "CommentVo", name = "commentVo", value = "新增评论", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
            @ApiResponse(code = 903, message = "用户没有购买此商品")
    })
    @PostMapping("orderitems/{id}/comments")
    @ResponseBody
    @Audit
    public Object addCommentOnSku(
            @LoginUser Long uid,
            @PathVariable Long id,
            @Validated @RequestBody CommentVo commentVo,
            BindingResult bindingResult){
        logger.debug("add comment by skuId:" + id);
        //校验前端数据
        Object returnObject = Common.processFieldErrors(bindingResult, httpServletResponse);
        if (null != returnObject) {
            logger.debug("validate fail");
            return returnObject;
        }
        ReturnObject ret=commentService.newComment(id,commentVo, uid);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 查看SKU评价列表
     */
    @ApiOperation(value = "查看SKU评价列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="page", required = false, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", required = false, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("skus/{id}/comments")
    public Object selectSkuComments(@PathVariable long id, @RequestParam(required = false,defaultValue = "1") Integer page, @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        logger.debug("selectSkuComments: page = "+ page +"  pageSize ="+pageSize);
        /*Object obj=Common.processFieldErrors(bindingResult,httpServletResponse);
        if(null!=obj){
            logger.info("validate fail");
            return obj;
        }*/
        ReturnObject ret=commentService.selectAllPassCommentBySkuId(id,page,pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员核审评论
     */
    @ApiOperation(value = "管理员核审评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="did", required = true, dataType="Integer", paramType="path"),
            @ApiImplicitParam(name="id", required = true, dataType="Integer", paramType="path")
    })      @ApiImplicitParam(paramType = "body", dataType = "CommentConclusionVo", name = "commentConclusionVo", value = "新增评论", required = true)
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @PutMapping("shops/{did}/comments/{id}/confirm")
    public Object confirmComment(@PathVariable long did,@PathVariable long id,@RequestBody CommentConclusionVo commentConclusionVo){
        logger.debug("confirm Comment:" + id);
        ReturnObject ret=commentService.confirmCommnets(did,id,commentConclusionVo);
        return Common.decorateReturnObject(ret);
    }

    /**
     * 买家查看自己的评价记录
     */
    @ApiOperation(value = "买家查看自己的评价记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="authorization", value="Token", required = true, dataType="String", paramType="header"),
            @ApiImplicitParam(name="page", required = false, dataType="Integer", paramType="query"),
            @ApiImplicitParam(name="pageSize", required = false, dataType="Integer", paramType="query")
    })
    @ApiResponses({
            @ApiResponse(code = 0, message = "成功"),
    })
    @GetMapping("comments")
    @ResponseBody
    @Audit
    public Object getOwnComments(
            @LoginUser Long uid,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        logger.debug("getOwnComments: page = "+ page +"  pageSize ="+pageSize);
        ReturnObject<PageInfo<VoObject>> ret=commentService.selectAllCommentsOfUser(uid,page,pageSize);
        return Common.getPageRetObject(ret);
    }

    /**
     * 管理员查看未核审/已核审的评论列表
     *
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
    public Object getAllComments(
            @PathVariable Long id,
            @RequestParam(required = false) Integer state,
            @RequestParam(required = false,defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        logger.debug("getAllComments: page = "+ page +"  pageSize ="+pageSize);
        ReturnObject ret=commentService.selectCommentsOfState(id,state,page,pageSize);
        return Common.getPageRetObject(ret);
    }

}
