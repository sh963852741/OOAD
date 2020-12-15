package cn.edu.xmu.activity.service.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class MockUserService implements IUserService{
    @Override
    public UserDTO getUserById(Long id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(5L);
        userDTO.setUserName("suw");
        userDTO.setRealName("34s");
        return userDTO;
    }
}
