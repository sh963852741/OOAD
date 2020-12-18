package cn.edu.xmu.goods.model.bo;

import cn.edu.xmu.goods.model.po.CommentPo;
import cn.edu.xmu.goods.model.vo.CommentRetVo;
import cn.edu.xmu.ooad.model.VoObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comment implements VoObject {
    /**
     * 评论状态
     */
    public enum State{
        NOT_AUDIT((byte)0 ,"未审核"),
        PASS((byte)1,"评论成功"),
        FORBID((byte)2, "未通过");

        private static final Map<Byte, Comment.State> stateMap;
        static {
            stateMap = new HashMap();
            for (Comment.State enum1 : values()) {
                stateMap.put(enum1.code, enum1);
            }
        }

        private Byte code;
        private String description;

        State(Byte code, String description) {
            this.code=code;
            this.description=description;
        }


        public Byte getCode() {
            return this.code;
        }

        public String getDescription() {
            return this.description;
        }

        public Comment.State getDescriptionByCode(Byte code){
            return stateMap.get(code);
        }
    }


    private Long id;
    private Long customerId;
    private String userName;
    private String realName;
    private Long goodsSkuId;
    private Long orderitemId;
    private Byte type;
    private String content;
    private Byte state;
    private LocalDateTime gmtCreated;
    private LocalDateTime gmtModified;

    public Comment(CommentPo commentPo){
        this.id=commentPo.getId();
        this.goodsSkuId=commentPo.getGoodsSkuId();
        this.orderitemId=commentPo.getOrderitemId();
        this.customerId=commentPo.getCustomerId();
        this.content=commentPo.getContent();
        this.type=commentPo.getType();
        this.state=commentPo.getState();
        this.gmtCreated=commentPo.getGmtCreate();
        this.gmtModified=commentPo.getGmtModified();
    }


    @Override
    public CommentRetVo createVo(){
        /*CommentRetVo commentRetVo=new CommentRetVo();
        commentRetVo.setId(this.getId());
        commentRetVo.setContent(this.getContent());
        commentRetVo.setGoodsSkuId(this.getGoodsSkuId());
        commentRetVo.setOrderitemId(this.getOrderitemId());
        commentRetVo.setType(this.getType());
        commentRetVo.setContent(this.getContent());
        commentRetVo.setState(this.getState());
        commentRetVo.setCustomer(this.getCustomer());*/

        return new CommentRetVo(this);
    }

    @Override
    public Object createSimpleVo(){
        return new CommentRetVo(this);
    }

    public CommentPo createPo(){
        CommentPo po=new CommentPo();
        po.setId(this.getId());
        po.setOrderitemId(this.getOrderitemId());
        po.setCustomerId(this.getCustomerId());
        po.setCustomerId(this.getCustomerId());
        po.setContent(this.getContent());
        po.setType(this.getType());
        po.setState(this.getState());
        po.setGoodsSkuId(this.getGoodsSkuId());
        po.setGmtModified(this.getGmtModified());
        po.setGmtCreate(this.getGmtCreated());
        return po;
    }


}
