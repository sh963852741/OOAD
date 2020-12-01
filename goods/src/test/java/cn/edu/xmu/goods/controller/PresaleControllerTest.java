package cn.edu.xmu.goods.controller;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PresaleControllerTest {

    private static String adminToken;
    private static String shopToken;
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    @Test
    public void addPresaleActivity1() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/284/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    public void getPresaleActivities(){

    }
}
