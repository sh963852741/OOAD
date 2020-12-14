package cn.edu.xmu.goods.service;

import cn.edu.xmu.goods.dao.CommentDao;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.vo.CommentConclusionVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;
import cn.edu.xmu.other.impl.ICustomerService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.edu.xmu.oomall.impl.*;

@Service
public class CommentService {
    private static final Logger logger= LoggerFactory.getLogger(CommentService.class);
    @Autowired
    private CommentDao commentDao;

    @DubboReference
    private ICustomerService customerService;
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
     * @return
     */
    public ReturnObject newComment(Long orderItemId, CommentVo commentVo){
        CommentPo po=new CommentPo();
        Long customerId=123456L;

        po.setOrderitemId(123456L);
        po.setContent(commentVo.getContent());
        po.setType(commentVo.getType().byteValue());
        po.setState(Comment.State.NOT_AUDIT.getCode());
        po.setCustomerId(customerId);
        ReturnObject ret=commentDao.insertComment(po);
        return ret;
    }

    /**
     * 分页查询sku下所有已通过审核的评论
     * @param skuId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject selectAllPassCommentBySkuId(Long skuId,Integer pageNum,Integer pageSize){
        return commentDao.selectAllPassCommentBySkuId(skuId,pageNum,pageSize);
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
    public ReturnObject selectAllCommentsOfUser(Integer pageNum, Integer pageSize){
        return commentDao.selectAllCommentsOfUser(pageNum,pageSize);
    }

    /**
     * 管理员查看未审核/已审核评论列表(有疑问)
     * @param state
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ReturnObject selelctCommentsOfState(Long did,Integer state,Integer pageNum, Integer pageSize){
        if(did!=0){
            return new ReturnObject(ResponseCode.RESOURCE_ID_OUTSCOPE);
        }
        return commentDao.selelctCommentsOfState(state.byteValue(),pageNum,pageSize);
    }




}
