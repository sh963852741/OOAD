package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.Application;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class BrandTests {
    @Autowired
    private MockMvc mvc;

//    @BeforeAll
//    static public void login() throws Exception {
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(BrandController.class).build();
//        String requireJson = "{\"userName\":\"13088admin\",\"password\":\"123456\"}";
//        String response = mockMvc.perform(post("/privilege/privileges/login")
//                .contentType("application/json;charset=UTF-8")
//                .content(requireJson)).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
//                .andExpect(jsonPath("$.errmsg").value("成功"))
//                .andExpect(jsonPath("$.data").isString())
//                .andReturn().getResponse().getContentAsString();
//    }

    @Test
    public void addBrand() throws Exception {
        String requestJson = "{\"name\": \"测试品牌名\", \"detail\": \"这是一个测试品牌名\"}";
        String responseString = this.mvc.perform(post("/shops/1/brands").content(requestJson).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.name").value("测试品牌名"))
                .andExpect(jsonPath("$.data.detail").value("这是一个测试品牌名"))
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
        String responseString = this.mvc.perform(put("/shops/0/brands/1").content(requestJson).contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.data.name").value("测试品牌名-改"))
                .andExpect(jsonPath("$.data.detail").value("这是一个测试品牌名-改"))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void deleteBrand() throws Exception {
        String responseString = this.mvc.perform(delete("/shops/0/brands/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }
}
