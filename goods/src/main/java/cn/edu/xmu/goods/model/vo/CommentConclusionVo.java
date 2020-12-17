package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "评论审核结果视图")
public class CommentConclusionVo {
    @ApiModelProperty(value = "评论审核结果")
    @NotNull
    private boolean conclusion;

    public boolean getConclusion(){
        return this.conclusion;
    }
}
