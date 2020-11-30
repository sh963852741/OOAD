package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpuSpecVo {

    Long id;
    String name;
    List<SpecItem> specItems;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class SpecItem{
    private Long id;
    private String name;
}
