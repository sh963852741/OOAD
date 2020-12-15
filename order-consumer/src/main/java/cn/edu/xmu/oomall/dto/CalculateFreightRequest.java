package cn.edu.xmu.oomall.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhibin lan
 * @date 2020-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateFreightRequest implements Serializable {
    Long skuId;

    Integer count;
}
