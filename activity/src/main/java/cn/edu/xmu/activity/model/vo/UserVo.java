package cn.edu.xmu.activity.model.vo;

import cn.edu.xmu.activity.service.dubbo.UserDTO;
import lombok.Data;

@Data
public class UserVo {
    private Long id;
    private String userName;

    public UserVo(UserDTO dto){
        id = dto.getId();
        userName =dto.getUserName();
    }
}
