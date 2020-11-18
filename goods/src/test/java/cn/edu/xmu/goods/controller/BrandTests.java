package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BrandTests {
    @Autowired
    private MockMvc mvc;

    @Test
    public void addBrand() throws Exception {
        String requestJson = "{\"name\": \"测试品牌名\",\"detail\": \"这是一个测试品牌名\"}";
        String responseString = this.mvc.perform(post("/shops/1/brands").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void getAllBrands() throws Exception {
        String responseString = this.mvc.perform(get("/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void modifyBrand() throws Exception {
        String requestJson = "{\"name\": \"测试品牌名-改\",\"detail\": \"这是一个测试品牌名-改\"}";
        String responseString = this.mvc.perform(put("/shops/1/brands/1").content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteBrand() throws Exception {
        String responseString = this.mvc.perform(delete("/shops/1/brands/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }
}
