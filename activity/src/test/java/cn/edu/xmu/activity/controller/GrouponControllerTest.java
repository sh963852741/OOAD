package cn.edu.xmu.activity.controller;

import cn.edu.xmu.activity.model.vo.CouponActivityVo;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GrouponControllerTest {
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

    /**
     * 获取团购活动状态
     * @throws Exception
     */
    @Test
    public void getGrouponActivityState1() throws Exception{
        mvc.perform(get("/groupon/groupons/states").contentType("application/json;charset=UTF-8").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    /**
     * 按照店铺获取团购活动
     * @throws Exception
     */
    public void getGrouponActivityList2() throws Exception {
        mvc.perform(get("/groupon/groupons?shopId=2").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(1))
                .andDo(MockMvcResultHandlers.print());
        mvc.perform(get("/groupon/groupons?shopId=0").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(0))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 按照时间线获取优惠活动
     * @throws Exception
     */
    @Test
    public void getGrouponActivityList3() throws Exception {
        for(int i = 1;i<=3;++i){
            mvc.perform(get("/coupon/couponactivities?timeline=" + i).contentType("application/json;charset=UTF-8"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("application/json;charset=UTF-8"))
                    .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                    .andDo(MockMvcResultHandlers.print());
        }
    }
    @Test
    /**
     * 按照店铺获取团购活动(all)
     * @throws Exception
     */
    public void getGrouponActivityList4() throws Exception {
        mvc.perform(get("/groupon/shops/1/groupons").contentType("application/json;charset=UTF-8")
                .header("authorization",shopToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andExpect(jsonPath("$.data.list.length()").value(3))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    /**
     * 按照店铺获取团购活动(all) 无权限
     * @throws Exception
     * 返回的错误码是503应该是505
     * ！
     * ！
     * ！
     */
    public void getGrouponActivityList5() throws Exception {
        mvc.perform(get("/groupon/shops/2/groupons").contentType("application/json;charset=UTF-8")
                .header("authorization",shopToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 新建优惠活动
     */
    @Test
    public void addGrouponActivity1() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime endTime = time.plusHours(3);
        String request="{ \"name\":\"团购活动\",\"strategy\": \"\", \"beginTime\": \"" + beginTime.toString()
                + "\",\"endTime\": \""+ endTime.toString() +"\"}";

        mvc.perform(post("/groupon/shops/1/spus/273/groupons").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken).content(request))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 新建优惠活动 失败
     */
    @Test
    public void addGrouponActivity2() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(4);
        LocalDateTime endTime = time.plusHours(3);
        String request="{ \"name\":\"团购活动\",\"strategy\": \"\", \"beginTime\": \"" + beginTime.toString()
                + "\",\"endTime\": \""+ endTime.toString() +"\"}";

        mvc.perform(post("/groupon/shops/1/spus/273/groupons").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 修改优惠活动  错误
     */
    @Test
    public void modifyGrouponActivity1() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(4);
        LocalDateTime endTime = time.plusHours(3);
        String request="{ \"name\":\"团购活动\",\"strategy\": \"\", \"beginTime\": \"" + beginTime.toString()
                + "\",\"endTime\": \""+ endTime.toString() +"\"}";

        mvc.perform(put("/groupon/shops/{shopId}/groupons/{id}").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken).content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.FIELD_NOTVALID.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 修改优惠活动
     */
    @Test
    public void modifyGrouponActivity2() throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime endTime = time.plusHours(3);
        String request="{ \"name\":\"团购活动\",\"strategy\": \"\", \"beginTime\": \"" + beginTime.toString()
                + "\",\"endTime\": \""+ endTime.toString() +"\"}";

        mvc.perform(put("/groupon/shops/{shopId}/groupons/{id}").contentType("application/json;charset=UTF-8")
                .header("authorization", shopToken).content(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 删除优惠活动
     * @throws Exception
     */
    @Test
    public void delCouponActivity()throws Exception {
        mvc.perform(delete("/groupon/shops/1/groupons/1").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());
        mvc.perform(delete("/groupon/shops/1/groupons/100").contentType("application/json;charset=UTF-8")
                .header("authorization", adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.RESOURCE_ID_NOTEXIST.getCode()))
                .andDo(MockMvcResultHandlers.print());
    }
    /**
     * 上下线优惠活动
     * @throws Exception
     */
    @Test
    public void lineActivity() throws Exception {
        mvc.perform(put("/groupon/shops/1/groupons/3/offshelves").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());

        mvc.perform(put("/groupon/shops/1/groupons/3/onshelves").contentType("application/json;charset=UTF-8")
                .header("authorization",adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
                .andDo(MockMvcResultHandlers.print());


    }
        /**
         *
         */
}
