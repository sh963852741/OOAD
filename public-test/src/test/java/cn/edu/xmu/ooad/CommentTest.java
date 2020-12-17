package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.annotation.Audit;
import cn.edu.xmu.ooad.annotation.Depart;
import cn.edu.xmu.ooad.util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.lang.*;

@SpringBootTest
public class CommentTest {
    private String managementGate = "http://localhost:8080";

    @Value("${public-test.mallgate}")
    private String mallGate;

    private WebTestClient manageClient;

    private WebTestClient mallClient;

    private static String adminToken;
    private static String shopToken;
    private static String userToken;

    public CommentTest(){
        this.manageClient = WebTestClient.bindToServer()
                .baseUrl(managementGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();

        this.mallClient = WebTestClient.bindToServer()
                .baseUrl(mallGate)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }

    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
        //userToken=jwtHelper.createToken(59L,1L, 3600);
    }

    /**
     * 获取评论状态
     */
    public void getAllState() {
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = manageClient.get().uri("/comments/states");
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isArray()
                .jsonPath("$.data[0].code").isNotEmpty()
                .jsonPath("$.data[0].name").isNotEmpty()
                .returnResult()
                .getResponseBodyContent();
    }

    /**
     * 买家新增SKU的评论
     */
    @Test
    public void addGoodCommentGoodType(){
        byte[] responseBuffer = null;
        String requestJson="{\"type\":0 ,\"content\":\"这个真不错\"}";
        WebTestClient.RequestHeadersSpec res = manageClient.post().uri("/orderitems/1/comments")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.customer.id").isNotEmpty()
                .jsonPath("$.data.customer.userName").isNotEmpty()
                .jsonPath("$.data.customer.name").isNotEmpty()
                .jsonPath("$.data.goodsSkuId").isNumber()
                .jsonPath("$.data.type").isEqualTo(0)
                .jsonPath("$.data.content").isEqualTo("这个真不错")
                .jsonPath("$.data.state").isEqualTo(0)
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.gmtModified").isNotEmpty()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *管理员通过评论
     */
    @Test
    public void allowComment(){
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": true}";
        WebTestClient.RequestHeadersSpec res = manageClient.put().uri("/shops/0/comments/1/confirm")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.customer.id").isNotEmpty()
                .jsonPath("$.data.customer.userName").isNotEmpty()
                .jsonPath("$.data.customer.name").isNotEmpty()
                .jsonPath("$.data.goodsSkuId").isNumber()
                .jsonPath("$.data.type").isEqualTo(0)
                .jsonPath("$.data.content").isNotEmpty()
                .jsonPath("$.data.state").isEqualTo(1)
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.gmtModified").isNotEmpty()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     *管理员不通过评论
     */
    @Test
    public void passComment(){
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": false}";
        WebTestClient.RequestHeadersSpec res = manageClient.put().uri("/shops/0/comments/1/confirm")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.customer.id").isNotEmpty()
                .jsonPath("$.data.customer.userName").isNotEmpty()
                .jsonPath("$.data.customer.name").isNotEmpty()
                .jsonPath("$.data.goodsSkuId").isNumber()
                .jsonPath("$.data.type").isEqualTo(0)
                .jsonPath("$.data.content").isNotEmpty()
                .jsonPath("$.data.state").isEqualTo(2)
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.gmtModified").isNotEmpty()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 审核参数不合法
     */
    @Test
    public void auditComment_abnormal() {
        byte[] responseBuffer = null;
        String requestJson = "{\"conclusion\": 123}";
        WebTestClient.RequestHeadersSpec res = manageClient.put().uri("/comment/shops/0/comments/1/confirm")
                .header("authorization", adminToken)
                .bodyValue(requestJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("字段不合法")
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();
        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看SKU评价列表(第一页)
     */
    @Test
    public void getAllCommnetOfSku1(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/skus/1/comments?page=1&pageSize=3").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看SKU评价列表(第二页)
     */
    @Test
    public void getAllCommnetOfSku2(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/skus/1/comments?page=2&pageSize=3").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第一页)
     */
    @Test
    public void getAllCommnetOfUser1(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/skus/1/comments?page=1&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 买家查看自己的评价记录，包括评论状态(第二页)
     */
    @Test
    public void getAllCommnetOfUser2(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/skus/1/comments?page=2&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看未核审评论列表(第一页)
     */
    @Test
    public void getAllUnauditedComment1(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=0&page=1&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看未核审评论列表(第二页)
     */
    @Test
    public void getAllUnauditedComment2(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=0&page=2&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看审核通过评论列表(第一页)
     */
    @Test
    public void getAllAuditedComment1(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=1&page=1&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看审核通过评论列表(第二页)
     */
    @Test
    public void getAllAuditedComment2(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=1&page=2&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看审核不通过评论列表(第一页)
     */
    @Test
    public void getAllUnAuditedComment1(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=2&page=1&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 管理员查看审核不通过评论列表(第二页)
     */
    @Test
    public void getAllUnAuditedComment2(){
        byte[] responseBuffer = null;
        responseBuffer=manageClient.get().uri("/shops/0/comments/all?state=2&page=2&pageSize=3")
                .header("authorization", shopToken).exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("application/json;charset=UTF-8")
                .expectBody()
                .returnResult().getResponseBody();

        try {
            String responseString = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
