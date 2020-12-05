package cn.edu.xmu.goods.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
    @Autowired
    private MockMvc mvc;

    @Test
    public void getAllState() throws Exception {
        String responseString = this.mvc.perform(get("/shop/shops/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 新建店铺
     * @throws Exception
     */
    @Test
    @Order(1)
    public void applyShop() throws Exception {
        String requestJson = "{\"name\": \"张三商铺\"}";
        String responseString = this.mvc.perform(post("/shop/shops").contentType("application/json;charset=UTF-8").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(3)
    public void modifyShop() throws Exception {
        String requestJson = "{\"name\": \"修改后的店铺名称\"}";
        String responseString = this.mvc.perform(put("/shops/1").header("authorization", null).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(2)
    public void auditShop() throws Exception {
        String requestJson = "{\"conclusion\": true}";
        String responseString = this.mvc.perform(put("/shops/0/newshops/1/audit").header("authorization", null).content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(4)
    public void shelfShop() throws Exception {
        String responseString1 = this.mvc.perform(put("/shops/1/onshelves").header("authorization", null))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

        String responseString2 = this.mvc.perform(put("/shops/1/offshelves").header("authorization", null))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @Order(5)
    public void deleteShop() throws Exception {
        String responseString = this.mvc.perform(delete("/shops/12").header("authorization", null))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }


}
