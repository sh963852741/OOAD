package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CommentDao;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.vo.CommentConclusionVo;
import cn.edu.xmu.goods.model.vo.CommentRetVo;
import cn.edu.xmu.goods.model.vo.CommentSelectRetVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.goods.service.dubbo.CustomerServiceMock;
import cn.edu.xmu.goods.service.dubbo.OrderServiceMock;
import cn.edu.xmu.ooad.model.VoObject;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.oomall.order.service.IDubboOrderService;
import cn.edu.xmu.other.impl.ICustomerService;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.apache.dubbo.config.annotation.DubboReference;
import org.bouncycastle.voms.VOMSAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private static final Logger logger= LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentDao commentDao;

//    @DubboReference
    private ICustomerService customerService;

//    @DubboReference
    private IDubboOrderService orderService;

    @Autowired
    private CustomerServiceMock customerServiceMock;

    @Autowired
    private OrderServiceMock orderServiceMock;
    /**
     * 获取评论所有状态
     * @return
     */
    public ReturnObject getCommentStates(){
        return commentDao.getCommentStates();
    }

    /**
     * 买家新增评论
     * @param orderItemId
     * @param commentVo
     * @param userId
     * @return
     */
    public ReturnObject newComment(Long orderItemId, CommentVo commentVo, Long userId){
        /*if(!orderService.isCustomerOwnOrderItem(userId, orderItemId)){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "用户没有购买此商品");
        }*/

        if(!orderServiceMock.isCustomerOwnOrderItem(userId,orderItemId)){
            return new ReturnObject(ResponseCode.FIELD_NOTVALID, "用户没有购买此商品");
        }

        //var customer = customerService.getCustomerById(userId);
        //var orderItem=orderService.getOrderItem(orderItemId);

        var orderItem=orderServiceMock.getOrderItem(orderItemId);
        var customer=customerServiceMock.getCustomerById(userId);

        CommentPo commentPo=new CommentPo();
        commentPo.setOrderitemId(orderItemId);
        commentPo.setContent(commentVo.getContent());
        commentPo.setType(commentVo.getType().byteValue());
        commentPo.setState(Comment.State.NOT_AUDIT.getCode());
        commentPo.setCustomerId(userId);

        ReturnObject ret=commentDao.insertComment(commentPo);
        return new ReturnObject(new CommentRetVo(commentPo,customer.getRealName(),customer.getRealName(),orderItem.getSkuId()));
    }

    /**
     * 分页查询sku下所有已通过审核的评论
     * @param skuId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> selectAllPassCommentBySkuId(Long skuId,Integer pageNum,Integer pageSize){
        ReturnObject<List<CommentPo>> ret=commentDao.selectAllPassCommentBySkuId(skuId,pageNum,pageSize);
        if(ret.getCode()!=ResponseCode.OK){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST,"该商品不存在");
        }
        List<VoObject> commentRetVos=new ArrayList<>();
        for(CommentPo po:ret.getData()){
            //var customer = customerService.getCustomerById(po.getCustomerId());

            var customer=customerServiceMock.getCustomerById(po.getCustomerId());
            CommentRetVo vo=new CommentRetVo(po,customer.getUserName(),customer.getRealName());
            commentRetVos.add(vo);
        }

        PageInfo<VoObject> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentRetVoPageInfo);
    }

    /**
     * 管理员审核评论
     * @param id
     * @param conclusion
     * @return
     */
    public ReturnObject confirmCommnets(Long did,Long id, CommentConclusionVo conclusion){
        if(did!=0){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        Comment comment=new Comment();
        comment.setId(id);
        comment.setState(conclusion.getConclusion()==true?Comment.State.PASS.getCode() : Comment.State.FORBID.getCode());
        ReturnObject ret=commentDao.updateCommentState(comment);
        return ret;
    }

    /**
     * 买家查看自己的评价记录，包括评论状态
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> selectAllCommentsOfUser(Long userId,Integer pageNum, Integer pageSize){
        List<CommentPo> commentPos=commentDao.selectAllCommentsOfUser(userId,pageNum,pageSize);
        List<VoObject> commentRetVos=new ArrayList<>();
        for(CommentPo po:commentPos){
            //var customer = customerService.getCustomerById(po.getCustomerId());
            var customer=customerServiceMock.getCustomerById(po.getCustomerId());
            CommentRetVo vo=new CommentRetVo(po,customer.getUserName(),customer.getRealName());
            commentRetVos.add(vo);
        }

        //分页查询
        PageInfo<VoObject> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentRetVoPageInfo);
    }

    /**
     * 管理员查看未审核/已审核评论列表(有疑问)
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject<PageInfo<VoObject>> selectCommentsOfState(Long did, Integer state, Integer pageNum, Integer pageSize){
        if(did!=0){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        List<CommentPo> commentPos=commentDao.selelctCommentsOfState(state.byteValue(),pageNum,pageSize);
        List<VoObject> commentRetVos=new ArrayList<>();
        for(CommentPo po:commentPos){
            //var customer = customerService.getCustomerById(po.getCustomerId());
            var customer=customerServiceMock.getCustomerById(po.getCustomerId());
            CommentRetVo vo=new CommentRetVo(po,customer.getUserName(),customer.getRealName());
            commentRetVos.add(vo);
        }

        PageInfo<VoObject> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentRetVoPageInfo);
    }



}

