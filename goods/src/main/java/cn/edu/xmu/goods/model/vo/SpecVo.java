package cn.edu.xmu.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecVo {

    private Long id;
    private String name;
    private List<SpecItem> specItems;
}
