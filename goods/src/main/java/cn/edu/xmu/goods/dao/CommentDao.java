package cn.edu.xmu.goods.dao;

import cn.edu.xmu.goods.mapper.CommentPoMapper;
import cn.edu.xmu.goods.mapper.SKUPoMapper;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.po.CommentPoExample;
import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.SKUPo;
import cn.edu.xmu.goods.model.vo.CommentRetVo;
import cn.edu.xmu.goods.model.vo.CommentSelectRetVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cn.edu.xmu.ooad.util.ResponseCode;
import cn.edu.xmu.ooad.util.ReturnObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CommentDao {
    @Autowired
    CommentPoMapper commentPoMapper;

    @Autowired
    SKUPoMapper skuPoMapper;


    private static final Logger logger = LoggerFactory.getLogger(CommentDao.class);

    /**
     * 获取评论所有状态
     * @param[]
     * @return CommentPo
     */
    public ReturnObject getCommentStates(){
        List<Map<String,Object>> stateList=new ArrayList<>();
        for(Comment.State state:Comment.State.values()){
            Map<String,Object> temp=new HashMap<>();
            temp.put("code",state.getCode());
            temp.put("name",state.getDescription());
            stateList.add(temp);
        }
        return new ReturnObject<>(stateList);
    }


    /**
     * 买家新增一条评论
     * @param commentPo
     * @return
     */
    public ReturnObject insertComment(CommentPo commentPo){
        try{
            commentPo.setGmtCreate(LocalDateTime.now());
            commentPo.setGmtModified(commentPo.getGmtModified());
            int ret=commentPoMapper.insertSelective(commentPo);
            if(ret==0){
                logger.debug("insertComment:insert fail"+commentPo.toString());
                return new ReturnObject(ResponseCode.FIELD_NOTVALID);
            }
            return new ReturnObject(commentPo);
        }catch (Exception e){
            return new ReturnObject(ResponseCode.INTERNAL_SERVER_ERR);
        }
    }

    /**
     * 分页查询sku下所有已通过审核的评论
     *
     * @param skuId
     * @param pageNum
     * @param pageSize
     * @return ReturnObject<>
     */
    public ReturnObject selectAllPassCommentBySkuId(Long skuId,Integer pageNum,Integer pageSize){
        //查看是否有此sku
        SKUPo skuPo=skuPoMapper.selectByPrimaryKey(skuId);
        if(skuPo==null){
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }

        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        List<CommentPo> commentPos=new ArrayList<>();
        //List<CommentRetVo> commentRetVos=new ArrayList<>();

        //分页查询
        PageHelper.startPage(pageNum,pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        try{
            //按照skuId进行查询
            criteria.andGoodsSkuIdEqualTo(skuId);
            criteria.andStateEqualTo(Comment.State.PASS.getCode());
            commentPos=commentPoMapper.selectByExample(example);
        }catch (DataAccessException e){
            logger.error("selectAllCommentBySkuId:DataAccessException:"+e.getMessage());
            return new ReturnObject<>(ResponseCode.RESOURCE_ID_NOTEXIST);
        }
        return new ReturnObject<>(commentPos);

        /*for(CommentPo po:commentPos){
            Comment comment=new Comment(po);
            commentRetVos.add(comment.createVo());
        }

        PageInfo<CommentRetVo> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentSelectRetVo);*/
    }
    

    /**
     * 修改评论状态
     */
    public ReturnObject updateCommentState(Comment comment){
        CommentPo commentPo=comment.createPo();
        int ret;
        try{
            ret=commentPoMapper.updateByPrimaryKeySelective(commentPo);
        }catch (Exception e){
            logger.error("updateCommentState: DataAccessException:" + e.getMessage());
            return new ReturnObject<>(ResponseCode.INTERNAL_SERVER_ERR);
        }
        if (ret == 0) {
            logger.debug("updateSku: update fail ! " + commentPo.toString());
            return new ReturnObject(ResponseCode.FIELD_NOTVALID);
        } else {
            logger.debug("updateSku: update sku success ! " + commentPo.toString());
            return new ReturnObject();
        }
    }


    /**
     * 买家查看自己的评价记录，包括评论状态
     */
    public List<CommentPo> selectAllCommentsOfUser(Integer pageNum, Integer pageSize){
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        List<CommentPo> commentPos=new ArrayList<>();
        List<CommentRetVo> commentRetVos=new ArrayList<>();

        PageHelper.startPage(pageNum,pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        commentPos=commentPoMapper.selectByExample(example);

        return commentPos;

        /*PageInfo<CommentRetVo> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentSelectRetVo);*/
    }

    /**
     *管理员查看未审核/已审核评论列表
     */
    public List<CommentPo> selelctCommentsOfState(Byte state,Integer pageNum, Integer pageSize){
        CommentPoExample example=new CommentPoExample();
        CommentPoExample.Criteria criteria=example.createCriteria();
        List<CommentPo> commentPos=new ArrayList<>();
        List<CommentRetVo> commentRetVos=new ArrayList<>();
        List<Comment> comments=new ArrayList<>();

        PageHelper.startPage(pageNum,pageSize);
        logger.debug("page = " + pageNum + "pageSize = " + pageSize);
        criteria.andStateEqualTo(state);
        commentPos=commentPoMapper.selectByExample(example);
        return commentPos;

        /*PageInfo<CommentRetVo> commentRetVoPageInfo=PageInfo.of(commentRetVos);
        CommentSelectRetVo commentSelectRetVo=new CommentSelectRetVo();
        commentSelectRetVo.setPage(pageNum.longValue());
        commentSelectRetVo.setPageSize(pageSize.longValue());
        commentSelectRetVo.setPages((long)commentRetVoPageInfo.getPages());
        commentSelectRetVo.setTotal(commentSelectRetVo.getTotal());
        commentSelectRetVo.setList(commentRetVos);

        return new ReturnObject<>(commentSelectRetVo);*/
    }
}


