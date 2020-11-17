package cn.edu.xmu.ooad.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "评论视图")
public class CommentVo {

    @ApiModelProperty(value = "评论类型")
    Long type;

    @ApiModelProperty(value = "评论内容")
    String content;

}
