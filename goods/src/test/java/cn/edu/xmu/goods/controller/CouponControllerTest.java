package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.bo.Coupon;
import cn.edu.xmu.goods.model.bo.CouponActivity;
import cn.edu.xmu.goods.model.bo.PresaleActivity;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerTest {
    @Autowired
    private MockMvc mvc;

    private static String adminToken;
    private static String shopToken;

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    /**
     * 获取优惠券所有状态
     * @throws Exception
     */
    @Test
    public void getCouponActivityStatus() throws Exception {
        ResultActions response = mvc.perform(get("/coupon/coupons/states")
                .contentType("application/json;charset=UTF-8"));
        String responseString = response.andExpect((status().isOk()))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data.size()").value(Coupon.CouponStatus.values().length))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].code").isNumber())
                .andExpect(jsonPath("$.data[0].name").isString())
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 新建优惠活动
     * @throws Exception
     */
    @Test
    public void addCouponActivity1() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime couponTime = time.minusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        String request="{ \"name\": \"优惠活动\", \"quantity\": 20, \"quantityType\": 0, \"validTerm\": 6, " +
                "\"beginTime\": \"" + beginTime.toString() + "\", " +
                "\"endTime\": \"" + endTime.toString() + "\", " +
                "\"couponTime\": \"" + couponTime.toString() +"\"," +
                "\"strategy\": {\"limitation\": 20}}";
        ResultActions response = mvc.perform(post("/coupon/shops/0/spus/20/couponactivities")
                .contentType("application/json;charset=UTF-8").content(request)
                .header("authorization", adminToken));

        String responseString = response.andExpect((status().isOk()))
                // .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.errmsg").value("成功"))
                .andExpect(jsonPath("$.data").isMap())
                .andExpect(jsonPath("$.data.id").isNumber())
                .andExpect(jsonPath("$.data.state").value(CouponActivity.CouponStatus.PENDING))
                .andExpect(jsonPath("$.data.quantity").value(20))
                .andExpect(jsonPath("$.data.quantityType").value(0))
                .andExpect(jsonPath("$.data.validTerm").value(6))
                .andExpect(jsonPath("$.data.imageUrl").doesNotExist())
                .andExpect(jsonPath("$.data.beginTime").isString())
                .andExpect(jsonPath("$.data.endTime").isString())
                .andExpect(jsonPath("$.data.gmtModified").isEmpty())
                .andExpect(jsonPath("$.data.gmtCreate").isString())
                .andExpect(jsonPath("$.data.couponTime").isString())
                .andExpect(jsonPath("$.data.shop.id").value(0))
                .andExpect(jsonPath("$.data.shop.name").isString())
                .andReturn().getResponse().getContentAsString();
    }
}