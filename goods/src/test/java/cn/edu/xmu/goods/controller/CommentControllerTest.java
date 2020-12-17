package cn.edu.xmu.goods.controller;

import cn.edu.xmu.goods.model.vo.CommentConclusionVo;
import cn.edu.xmu.goods.model.vo.CommentVo;
import cn.edu.xmu.ooad.util.Common;
import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    private static String adminToken;
    private static String shopToken;
    private static String userToken;
    @Autowired
    private MockMvc mvc;

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
        userToken =jwtHelper.createToken(1234L,2L,3600);
    }

    /**
     * 获取评论状态
     */
    @Test
    public void getCommentState() throws Exception{
        String responseString=this.mvc.perform(get("/comment/comments/states"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 买家新增SKU的评论
     */
    @Test
    public void addGoodCommentGoodType() throws Exception{
        /*CommentVo commentVo=new CommentVo();
        commentVo.setType(0L);
        commentVo.setContent("这个真不错");
        String requestJSON= JacksonUtil.toJson(commentVo);*/
        String requestJSON="{\"type\":0 ,\"content\":\"这个真不错\"}";


        mvc.perform(post("/comment/orderitems/1/comments").contentType("application/json;charset=UTF-8")
        .header("authorization",adminToken).content(requestJSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     *管理员通过评论
     */
    @Test
    public void allowComment() throws Exception{
        CommentConclusionVo conclusion=new CommentConclusionVo();
        conclusion.setConclusion(true);
        String requestJSON=JacksonUtil.toJson(conclusion);

        this.mvc.perform(put("/comment/shops/0/comments/2/confirm").contentType("application/json;charset=UTF-8").header("authorization",adminToken).content(requestJSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());

    }

    /**
     *管理员不通过评论
     */
    @Test
    public void passComment() throws Exception{
        CommentConclusionVo conclusion=new CommentConclusionVo();
        conclusion.setConclusion(false);
        String requestJSON=JacksonUtil.toJson(conclusion);

        this.mvc.perform(put("/comment/shops/0/comments/1/confirm").contentType("application/json;charset=UTF-8").header("authorization",adminToken).content(requestJSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 查看SKU评价列表(第二页)
     */
    @Test
    public void getAllCommnetOfSku2() throws Exception{
        this.mvc.perform(get("/comment/skus/273/comments").contentType("application/json;charset=UTF-8").queryParam("page","2").queryParam("pageSize","3"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 查看SKU评价列表(第一页)
     */
    @Test
    public void getAllCommnetOfSku1() throws Exception{
        this.mvc.perform(get("/comment/skus/273/comments").contentType("application/json;charset=UTF-8").queryParam("page","1").queryParam("pageSize","3"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());

    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第一页)
     */
    @Test
    public void getAllCommnetOfUser1() throws Exception{
         this.mvc.perform(get("/comment/comments?page=1&pageSize=3").contentType("application/json;charset=UTF-8").header("authorization",adminToken))
         .andExpect(status().isOk())
         .andExpect(content().contentType("application/json;charset=UTF-8"))
         .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
         .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第二页)
     */
    @Test
    public void getAllCommnetOfUser2() throws Exception{
        this.mvc.perform(get("/comment/comments").contentType("application/json;charset=UTF-8").header("authorization",adminToken).queryParam("page","2").queryParam("pageSize","10"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 管理员查看未核审评论列表(第二页)
     */
    @Test
    public void getAllUnauditedComment1() throws Exception{
         this.mvc.perform(get("/comment/shops/0/comments/all").contentType("application/json;charset=UTF-8").header("authorization",adminToken).queryParam("state","0").queryParam("page","2").queryParam("pageSize","3"))
         .andExpect(status().isOk())
         .andExpect(content().contentType("application/json;charset=UTF-8"))
         .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
         .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 管理员查看已核审评论列表(第二页)
     */
    @Test
    public void getAllAuditedComment1() throws Exception{
         this.mvc.perform(get("/comment/shops/0/comments/all").contentType("application/json;charset=UTF-8").header("authorization",adminToken).queryParam("state","1").queryParam("page","2").queryParam("pageSize","10"))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
        .andExpect(jsonPath("$.errno").value(ResponseCode.OK.getCode()))
        .andDo(MockMvcResultHandlers.print());
    }
    
}
