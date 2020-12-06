package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.PresaleActivity;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = false)
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
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name").value("预售活动"))
                .andExpect(jsonPath("$.data.advancePayPrice").value(20))
                .andExpect(jsonPath("$.data.restPayPrice").value(3000))
                .andExpect(jsonPath("$.data.quantity").value(10))
                .andExpect(jsonPath("$.data.payTime").isString())
                .andExpect(jsonPath("$.data.beginTime").isString())
                .andExpect(jsonPath("$.data.endTime").isString())
                .andExpect(jsonPath("$.data.gmtModified").isEmpty())
                .andExpect(jsonPath("$.data.gmtCreate").isString())
                .andExpect(jsonPath("$.data.state").isNumber())
                .andExpect(jsonPath("$.data.goodsSpu.shopId").doesNotExist())
                .andExpect(jsonPath("$.data.goodsSpu.id").value(290))
                .andExpect(jsonPath("$.data.shop.id").value(1))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity2() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity3() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity4() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.minusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity5() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.minusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity6() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": -3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //测试错误
    public void addPresaleActivity7() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/spus/3000/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //测试错误
    public void addPresaleActivity8() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/2/spus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getPresaleState()throws Exception{
        ResultActions response = mvc.perform(get("/presale/presales/states")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.size()").value(PresaleActivity.PresaleStatus.values().length))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").isNumber())
                .andExpect(jsonPath("$.data[0].name").isString())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void modifyPresaleActivity()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void delPresaleActivity(){

    }
}
