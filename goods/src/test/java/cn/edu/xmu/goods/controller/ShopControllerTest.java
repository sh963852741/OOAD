package cn.edu.xmu.goods.controller;

import cn.edu.xmu.ooad.util.JwtHelper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShopControllerTest {
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
     * 获取店铺所有状态
     * @throws Exception
     */
    @Test
    public void getAllState() throws Exception {
        String responseString = this.mvc.perform(get("/shop/shops/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 新建店铺（正常流程）
     * @throws Exception
     */
    @Test
    @Order(1)
    public void applyShop() throws Exception {
        String requestJson = "{\"name\": \"张三商铺\"}";
        String responseString = this.mvc.perform(post("/shop/shops").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 新建店铺（重复申请）
     * @throws Exception
     */
    @Test
    public void applyShop_again() throws Exception {
        String requestJson = "{\"name\": \"张三商铺\"}";
        String responseString = this.mvc.perform(post("/shop/shops").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 新建店铺（不传名称）
     * @throws Exception
     */
    @Test
    public void applyShop_null() throws Exception {
        String requestJson = "{\"name\": \"\"}";
        String responseString = this.mvc.perform(post("/shop/shops").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 新建店铺（名称是空格）
     * @throws Exception
     */
    @Test
    public void applyShop_space() throws Exception {
        String requestJson = "{\"name\": \"  \"}";
        String responseString = this.mvc.perform(post("/shop/shops").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 修改店铺
     * @throws Exception
     */
    @Test
    @Order(3)
    public void modifyShop() throws Exception {
        String requestJson = "{\"name\": \"修改后的店铺名称\"}";
        String responseString = this.mvc.perform(put("/shop/shops/1").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 修改店铺(试图改id)
     * @throws Exception
     */
    @Test
    @Order(3)
    public void modifyShop_ID() throws Exception {
        String requestJson = "{\"name\": \"修改后的店铺名称\",\"id\":\"123\"}";
        String responseString = this.mvc.perform(put("/shop/shops/1").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 修改店铺(试图改state)
     * @throws Exception
     */
    @Test
    @Order(3)
    public void modifyShop_STATE() throws Exception {
        String requestJson = "{\"name\": \"修改后的店铺名称\",\"state\":\"3\"}";
        String responseString = this.mvc.perform(put("/shop/shops/1").header("authorization", shopToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }


    /**
     * 审核店铺
     * @throws Exception
     */
    @Test
    @Order(2)
    public void auditShop() throws Exception {
        String requestJson = "{\"conclusion\": true}";
        String responseString = this.mvc.perform(put("/shop/shops/0/newshops/1/audit").header("authorization", adminToken).contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 上架店铺
     * @throws Exception
     */
    @Test
    @Order(4)
    public void onshelfShop() throws Exception {
        String responseString1 = this.mvc.perform(put("/shop/shops/1/onshelves").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();}

    /**
     * 下架店铺
     * @throws Exception
     */
    @Test
    public void offshelfShop() throws Exception {
        String responseString2 = this.mvc.perform(put("/shop/shops/1/offshelves").header("authorization", adminToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 关闭店铺
     * @throws Exception
     */
    @Test
    @Order(5)
    public void deleteShop() throws Exception {
        String responseString = this.mvc.perform(delete("/shop/shops/1").header("authorization", adminToken,shopToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }
}
