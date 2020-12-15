package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.bo.PresaleActivity;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PresaleControllerTest {

    private static String adminToken;
    private static String shopToken;
    @Autowired
    private MockMvc mvc;
    //test

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    @Test
    //正确////
    public void addPresaleActivity1() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
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
                .andExpect(jsonPath("$.data.goodsSku.shopId").doesNotExist())
                .andExpect(jsonPath("$.data.goodsSku.id").value(290))
                .andExpect(jsonPath("$.data.shop.id").value(1))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //无名称√
    public void addPresaleActivity2() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //开始时间早于当前时间√
    public void addPresaleActivity3() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //结束时间早于当前时间√
    public void addPresaleActivity4() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.minusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //尾款支付时间早于当前时间√
    public void addPresaleActivity5() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.minusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //尾款是负数√
    public void addPresaleActivity6() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": -3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //指定的sku不存在√
    public void addPresaleActivity7() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/3000/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isNotFound()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //测试错误！ 指定的sku不是本商铺的sku（错误码应为对自己店铺外资源操作）
    public void addPresaleActivity8() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/299/presales")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void addPresaleActivity9() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
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
                .andExpect(jsonPath("$.data.goodsSku.shopId").doesNotExist())
                .andExpect(jsonPath("$.data.goodsSku.id").value(290))
                .andExpect(jsonPath("$.data.shop.id").value(1))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
        String request1="{ \"name\": \"预售活动1\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response1 = mvc.perform(post("/presale/shops/1/skus/290/presales")
                .contentType("application/json;charset=UTF-8").content(request1)
                .header("authorization", shopToken));
        String responseString1 = response1.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.name").value("预售活动1"))
                .andExpect(jsonPath("$.data.advancePayPrice").value(20))
                .andExpect(jsonPath("$.data.restPayPrice").value(3000))
                .andExpect(jsonPath("$.data.quantity").value(10))
                .andExpect(jsonPath("$.data.payTime").isString())
                .andExpect(jsonPath("$.data.beginTime").isString())
                .andExpect(jsonPath("$.data.endTime").isString())
                .andExpect(jsonPath("$.data.gmtModified").isEmpty())
                .andExpect(jsonPath("$.data.gmtCreate").isString())
                .andExpect(jsonPath("$.data.state").isNumber())
                .andExpect(jsonPath("$.data.goodsSku.shopId").doesNotExist())
                .andExpect(jsonPath("$.data.goodsSku.id").value(290))
                .andExpect(jsonPath("$.data.shop.id").value(1))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //尝试设置活动状态
    public void addPresaleActivity10() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\",\"state\":\"1\"}";
        ResultActions response = mvc.perform(post("/presale/shops/1/skus/290/presales")
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
                .andExpect(jsonPath("$.data.goodsSku.shopId").doesNotExist())
                .andExpect(jsonPath("$.data.goodsSku.id").value(290))
                .andExpect(jsonPath("$.data.shop.id").value(1))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andDo(MockMvcResultHandlers.print())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //正常
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
    //正常 用户根据timeline
    public void getPresaleActivity1()throws Exception{
        ResultActions response = mvc.perform(get("/presale/presales")
                .queryParam("timeline", "1")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0].name").isString())
                .andExpect(jsonPath("$.data.list[0].BeginTime").isString())
                .andExpect(jsonPath("$.data.list[0].payTime").isString())
                .andExpect(jsonPath("$.data.list[0].endTime").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.id").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.name").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.skuSn").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.imageUrl").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.inventory").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.originalPrice").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.price").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.disable").isNumber())
                .andReturn().getResponse().getContentAsString();

    }
    @Test
    //正常 用户根据sku id
    public void getPresaleActivity2()throws Exception{
        ResultActions response = mvc.perform(get("/presale/presales")
                .queryParam("skuId", "1")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0].name").isString())
                .andExpect(jsonPath("$.data.list[0].BeginTime").isString())
                .andExpect(jsonPath("$.data.list[0].payTime").isString())
                .andExpect(jsonPath("$.data.list[0].endTime").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.id").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.name").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.skuSn").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.imageUrl").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.inventory").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.originalPrice").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.price").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.disable").isNumber())
                .andReturn().getResponse().getContentAsString();

    }
    @Test
    //正常 用户根据shop id
    public void getPresaleActivity3()throws Exception{
        ResultActions response = mvc.perform(get("/presale/presales")
                .queryParam("shopId", "1")
                .queryParam("page", "1")
                .queryParam("pageSize", "3")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.page").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(3))
                .andExpect(jsonPath("$.data.list[0].name").isString())
                .andExpect(jsonPath("$.data.list[0].BeginTime").isString())
                .andExpect(jsonPath("$.data.list[0].payTime").isString())
                .andExpect(jsonPath("$.data.list[0].endTime").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.id").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.name").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.skuSn").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.imageUrl").isString())
                .andExpect(jsonPath("$.data.list[0].goodsSku.inventory").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.originalPrice").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.price").isNumber())
                .andExpect(jsonPath("$.data.list[0].goodsSku.disable").isNumber())
                .andReturn().getResponse().getContentAsString();

    }
    @Test
    //正常
    public void modifyPresaleActivity1()throws Exception{
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
    //无名称
    public void modifyPresaleActivity2()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //开始时间早于当前时间
    public void modifyPresaleActivity3()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().is4xxClientError()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //结束时间早于当前时间
    public void modifyPresaleActivity4()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.minusHours(4);

        String request="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().is4xxClientError()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //支付尾款时间早于当前时间
    public void modifyPresaleActivity5()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.minusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().is4xxClientError()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //尾款是负数
    public void modifyPresaleActivity6()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"\", \"advancePayPrice\": 200, \"restPayPrice\": -300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().isBadRequest()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    //开始时间早于当前时间
    public void modifyPresaleActivity7()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        String request="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        ResultActions response = mvc.perform(put("/presale/shops/0/presales/1")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));
        String responseString = response.andExpect((status().is4xxClientError()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void delPresaleActivity1()throws Exception {
        ResultActions response = mvc.perform(delete("/shops/1/presales/1")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
    @Test
    public void delPresaleActivity2()throws Exception {
        ResultActions response = mvc.perform(delete("/shops/1/presales/290")
                .contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken));
        String responseString = response.andExpect((status().isNotFound()))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andExpect(jsonPath("$.data").doesNotExist())
                .andReturn().getResponse().getContentAsString();
    }
}
