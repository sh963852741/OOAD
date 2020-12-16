package cn.edu.xmu.oomall.other.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author XQChen
 * @version 创建时间：2020/12/12 下午6:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RebateDTO implements Serializable {
    private Long customerId;
    private Integer num;
}
