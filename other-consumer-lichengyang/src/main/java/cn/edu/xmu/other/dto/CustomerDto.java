package cn.edu.xmu.other.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ChengYang Li
 */
@Data
public class CustomerDto implements Serializable
{
    Long id;
    String userName;
    String realName;
}
