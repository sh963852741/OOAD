package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.ooad.model.VoObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel(value="查看评论视图")
@Data
public class CommentRetVo implements VoObject {
    @ApiModelProperty(value = "评论id")
    private Long id;

    private Long goodsSkuId;

    private Customer customer=new Customer();

    //private Long orderitemId;

    private Byte type;

    private String content;

    private Byte state;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    public CommentRetVo(Comment comment){
        this.id=comment.getId();
        this.goodsSkuId=comment.getGoodsSkuId();
        this.customer.set(comment.getCustomerId(),comment.getUserName(),comment.getRealName());
        this.type=comment.getType();
        this.state=comment.getState();
        this.gmtCreated=comment.getGmtCreated();
        this.gmtModified=comment.getGmtModified();
    }

    public CommentRetVo(CommentPo commentPo,String userName,String realName,Long goodsSkuId){
        this.setId(commentPo.getId());
        this.customer.set(commentPo.getId(),userName,realName);
        this.setState(commentPo.getState());
        this.setContent(commentPo.getContent());
        this.setType(commentPo.getType());
        this.setGoodsSkuId(goodsSkuId);
        this.setGmtCreated(commentPo.getGmtCreate());
        this.setGmtModified(commentPo.getGmtModified());
    }

    public CommentRetVo(CommentPo commentPo,String userName,String realName){
        this.setId(commentPo.getId());
        this.customer.set(commentPo.getId(),userName,realName);
        this.setState(commentPo.getState());
        this.setContent(commentPo.getContent());
        this.setType(commentPo.getType());
        this.setGoodsSkuId(commentPo.getGoodsSkuId());
        this.setGmtCreated(commentPo.getGmtCreate());
        this.setGmtModified(commentPo.getGmtModified());
    }

    @Override
    public Object createVo() {
        return this;
    }

    @Override
    public Object createSimpleVo() {
        return this;
    }
    /*
    public CommentPo createPo(){
        CommentPo po=new CommentPo();
        po.setId(this.getId());
        po.setGoodsSkuId(this.getGoodsSkuId());
       // po.setOrderitemId(this.getOrderitemId());
        po.setContent(this.getContent());
        po.setType(this.getType());
        po.setState(this.getState());
        po.setCustomerId(this.getCustomer().getId());
        po.setGmtCreate(this.getGmtCreate());
        po.setGmtModified(this.getGmtModified());
        return po;
    }*/

}

@Data
class Customer{
    private Long id;
    private String userName;
    private String realName;

    public void set(Long id,String userName,String realName){
        this.id=id;
        this.userName=userName;
        this.realName=realName;
    }
}