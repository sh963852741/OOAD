package cn.edu.xmu.goods.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@ApiModel(value = "评论视图")
public class CommentVo {

    @Max(2)
    @Min(0)
    @ApiModelProperty(value = "评论类型")
    Long type;

    @ApiModelProperty(value = "评论内容")
    String content;

}
