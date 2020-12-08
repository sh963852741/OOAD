package cn.edu.xmu.goods.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

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
     * 买家新增SKU的评论(内容好评，类型好评),内容好评，类型差评？
     */
    @Test
    public void addGoodCommentGoodType() throws Exception{
        /*CommentVo commentVo=new CommentVo();
        commentVo.setType(0L);
        commentVo.setContent("这个真不错");
        String requestJSON= JacksonUtil.toJson(commentVo);*/
        String requestJSON="{\"type\":0 ,\"content\":\"这个真不错\"}";
        String responseString=this.mvc.perform(post("/comment/orderitems/2/comments").contentType("application/json;charset=UTF-8").content(requestJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     *管理员通过评论
     */
    @Test
    public void allowComment() throws Exception{
        String requestJSON="{\"conclusion\":true }";
        String responseString=this.mvc.perform(put("/comment/comments/2/confirm").contentType("application/json;charset=UTF-8").content(requestJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     *管理员不通过评论
     */
    @Test
    public void passComment() throws Exception{
        String requestJSON="{\"conclusion\":false}";
        String responseString=this.mvc.perform(put("/comment/comments/1/confirm").contentType("application/json;charset=UTF-8").content(requestJSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 查看SKU评价列表(第二页)
     */
    @Test
    public void getAllCommnetOfSku2() throws Exception{
        String responseString = this.mvc.perform(get("/comment/skus/2/comments").contentType("application/json;charset=UTF-8").queryParam("page","2").queryParam("pageSize","10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 查看SKU评价列表(第一页)
     */
    @Test
    public void getAllCommnetOfSku1() throws Exception{
        String responseString = this.mvc.perform(get("/comment/skus/2/comments").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第一页)
     */
    @Test
    public void getAllCommnetOfUser1() throws Exception{
        String responseString = this.mvc.perform(get("/comment/comments").contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第二页)
     */
    @Test
    public void getAllCommnetOfUser2() throws Exception{
        String responseString = this.mvc.perform(get("/comment/comments").contentType("application/json;charset=UTF-8").queryParam("page","2").queryParam("pageSize","10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 管理员查看未核审评论列表(第二页)
     */
    @Test
    public void getAllUnauditedComment1() throws Exception{
        String responseString = this.mvc.perform(get("/comment/shops/0/comments/all").queryParam("state","0").queryParam("page","2").queryParam("pageSize","10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * 管理员查看已核审评论列表(第二页)
     */
    @Test
    public void getAllAuditedComment1() throws Exception{
        String responseString = this.mvc.perform(get("/comment/shops/0/comments/all").queryParam("state","1").queryParam("page","2").queryParam("pageSize","10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
    }
}

