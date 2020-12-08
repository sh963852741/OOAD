package cn.edu.xmu.goods.model.vo;

import cn.edu.xmu.goods.model.bo.Comment;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel(value="查看评论视图")
@Data
public class CommentRetVo {
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
       // this.orderitemId=comment.getOrderitemId();
        //this.customer.set(comment.getCustomer().getId(),comment.getCustomer().getUserName(),comment.getCustomer().getName());
        this.customer.set(comment.getCustomerId(),"小沈","小小沈");
        this.type=comment.getType();
        this.state=comment.getState();
        this.gmtCreated=comment.getGmtCreated();
        this.gmtModified=comment.getGmtModified();
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