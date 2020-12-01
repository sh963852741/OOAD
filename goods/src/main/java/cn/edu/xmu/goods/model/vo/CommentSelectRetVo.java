package cn.edu.xmu.goods.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentSelectRetVo {
    private Long page;
    private Long pageSize;
    private Long total;
    private Long pages;
    private List<CommentRetVo> list=new ArrayList<>();
}
