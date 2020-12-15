package cn.edu.xmu.ooad;

import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class PresaleTest {

    private WebTestClient webClient;

    private static String adminToken;
    private static String shopToken;

    public PresaleTest(){
        this.webClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:8080")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8")
                .build();
    }
//this is a test
    @BeforeAll
    private static void login(){
        JwtHelper jwtHelper = new JwtHelper();
        adminToken =jwtHelper.createToken(1L,0L, 3600);
        shopToken =jwtHelper.createToken(59L,1L, 3600);
    }

    /**
     * 正常加入一个预售活动
     */
    @Test
    public void addPresaleActivity1(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.name").isEqualTo("预售活动")
                .jsonPath("$.data.advancePayPrice").isEqualTo(20)
                .jsonPath("$.data.restPayPrice").isEqualTo(3000)
                .jsonPath("$.data.quantity").isEqualTo(10)
                .jsonPath("$.data.payTime").isEqualTo(payTime.toString())
                .jsonPath("$.data.beginTime").isEqualTo(beginTime.toString())
                .jsonPath("$.data.endTime").isEqualTo(endTime.toString())
                .jsonPath("$.data.gmtModified").isEmpty()
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.state").isNumber()
                .jsonPath("$.data.goodsSpu.shopId").doesNotExist()
                .jsonPath("$.data.goodsSpu.id").isEqualTo(290)
                .jsonPath("$.data.shop.id").isEqualTo(1)
                .jsonPath("$.data.shop.name").isEqualTo("张三商铺")
                .returnResult()
                .getResponseBodyContent();

        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 加入一个预售活动  无名称
     */
    public void addPresaleActivity2() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();

    }
    @Test
    /**
     * 加入一个预售活动  开始时间早于当前时间√
     */
    public void addPresaleActivity3() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 加入一个预售活动  结束时间早于当前时间
     */
    public void addPresaleActivity4() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.minusHours(3);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 加入一个预售活动  支付时间早于当前时间
     */
    public void addPresaleActivity5() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.minusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 加入一个预售活动  尾款是负数
     */
    public void addPresaleActivity6() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\":\"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": -3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 加入一个预售活动  指定的sku不存在
     */
    public void addPresaleActivity7() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/3000/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isNotFound()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_NOTEXIST.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();

    }
    @Test
    /**
     * 加入一个预售活动  指定的sku不是本商铺的sku（与操作其他商铺分开？）
     */
    public void addPresaleActivity8() throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/2/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isForbidden()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.RESOURCE_ID_OUTSCOPE.getCode())
                .jsonPath("$.data").doesNotExist()
                .returnResult()
                .getResponseBodyContent();

    }
    /**
     * 正常加入一个预售活动 同一时段两个预售活动（允许） ，此时只要再跑一遍add1可以add就证明可以（只改了名称）
     */
    @Test
    public void addPresaleActivity9(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动测\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.name").isEqualTo("预售活动测")
                .jsonPath("$.data.advancePayPrice").isEqualTo(20)
                .jsonPath("$.data.restPayPrice").isEqualTo(3000)
                .jsonPath("$.data.quantity").isEqualTo(10)
                .jsonPath("$.data.payTime").isEqualTo(payTime.toString())
                .jsonPath("$.data.beginTime").isEqualTo(beginTime.toString())
                .jsonPath("$.data.endTime").isEqualTo(endTime.toString())
                .jsonPath("$.data.gmtModified").isEmpty()
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.state").isNumber()
                .jsonPath("$.data.goodsSpu.shopId").doesNotExist()
                .jsonPath("$.data.goodsSpu.id").isEqualTo(290)
                .jsonPath("$.data.shop.id").isEqualTo(1)
                .jsonPath("$.data.shop.name").isEqualTo("张三商铺")
                .returnResult()
                .getResponseBodyContent();

        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 加入一个预售活动（尝试设置状态但无效所以不影响），此时只要改变测试语句但正常返回即可
     */
    @Test
    public void addPresaleActivity10(){
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(1);
        LocalDateTime payTime = time.plusHours(2);
        LocalDateTime endTime = time.plusHours(3);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动\", \"advancePayPrice\": 20, \"restPayPrice\": 3000, \"quantity\": 10, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\",\"state\":\"0\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/skus/290/presales")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data").isMap()
                .jsonPath("$.data.id").isNumber()
                .jsonPath("$.data.name").isEqualTo("预售活动")
                .jsonPath("$.data.advancePayPrice").isEqualTo(20)
                .jsonPath("$.data.restPayPrice").isEqualTo(3000)
                .jsonPath("$.data.quantity").isEqualTo(10)
                .jsonPath("$.data.payTime").isEqualTo(payTime.toString())
                .jsonPath("$.data.beginTime").isEqualTo(beginTime.toString())
                .jsonPath("$.data.endTime").isEqualTo(endTime.toString())
                .jsonPath("$.data.gmtModified").isEmpty()
                .jsonPath("$.data.gmtCreate").isNotEmpty()
                .jsonPath("$.data.state").isNumber()
                .jsonPath("$.data.goodsSpu.shopId").doesNotExist()
                .jsonPath("$.data.goodsSpu.id").isEqualTo(290)
                .jsonPath("$.data.shop.id").isEqualTo(1)
                .jsonPath("$.data.shop.name").isEqualTo("张三商铺")
                .returnResult()
                .getResponseBodyContent();

        try {
            String response = new String(responseBuffer, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    @Test
    /**
     * 正常修改一个预售活动
     */
    public void modifyPresaleActivity1()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  无名称
     */
    public void modifyPresaleActivity2()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  开始时间早于当前时间
     */
    public void modifyPresaleActivity3()throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.minusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                + "\",\"endTime\": \"" + endTime.toString() + "\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  结束时间早于当前时间
     */
    public void modifyPresaleActivity4()throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.minusHours(4);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                + "\",\"endTime\": \"" + endTime.toString() + "\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  尾款支付时间早于当前时间
     */
    public void modifyPresaleActivity5()throws Exception {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.minusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson = "{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                + "\",\"endTime\": \"" + endTime.toString() + "\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  尾款是负数
     */
    public void modifyPresaleActivity6()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": -300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.FIELD_NOTVALID.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个已经开始的预售活动 （设2已经开始，不行换别的预售活动）（测试之后改错误码）
     */
    public void modifyPresaleActivity7()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/2")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改预售活动   尝试修改sku但无效 (sku加在requireJson上)
     */
    public void modifyPresaleActivity8()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\",\"sku\":\"289\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个已经取消的预售活动 （设3已经取消，不行换别的预售活动）（测试之后改错误码）
     */
    public void modifyPresaleActivity9()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/1/presales/3")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 修改一个预售活动  不属于自己店铺（设3， 测试之后改错误码）
     */
    public void modifyPresaleActivity10()throws Exception{
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime beginTime = time.plusHours(2);
        LocalDateTime payTime = time.plusHours(3);
        LocalDateTime endTime = time.plusHours(4);

        byte[] responseBuffer = null;
        String requireJson="{ \"name\": \"预售活动-改\", \"advancePayPrice\": 200, \"restPayPrice\": 300, \"quantity\": 110, \"beginTime\": \"" + beginTime.toString()
                + "\", \"payTime\": \"" + payTime.toString()
                +"\",\"endTime\": \""+ endTime.toString() +"\"}";
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/shops/3/presales/1")
                .header("authorization", shopToken)
                .bodyValue(requireJson);

        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .returnResult()
                .getResponseBodyContent();
    }
    @Test
    /**
     * 获取预售活动的所有状态
     * .andExpect(jsonPath("$.data.size()").value(PresaleActivity.PresaleStatus.values().length)
     * 这句不会改
     */
    public void getPresaleState()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales/states")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.data[0].code").isNumber()
                .jsonPath("$.data[0].name").isNotEmpty()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  用户根据timeline
     */
    public void getPresaleActivity1()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?timeline=1&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  用户根据sku id
     */
    public void getPresaleActivity2()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?sku id=290&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  用户根据shop id
     */
    public void getPresaleActivity3()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?shop id=0&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  用户根据shop id sku id 应该返回sku id 的结果
     */
    public void getPresaleActivity4()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?shop id=0&sku id=290&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  店铺管理员查看其他店铺的活动 应返回错误码
     */
    public void getPresaleActivity5()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?shop id=2&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  店铺管理员查看各状态sku
     */
    public void getPresaleActivity6()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?state=1&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  店铺管理员查看各状态sku state 不正确
     */
    public void getPresaleActivity7()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?state=10&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
    @Test
    /**
     * 获取预售活动
     *  平台管理员查看其他店铺的预售活动
     */
    public void getPresaleActivity8()throws Exception{
        byte[] responseBuffer = null;
        WebTestClient.RequestHeadersSpec res = webClient.post().uri("/presale/presales?shop id=2&page=1&pageSize=3")
                .header("authorization", shopToken);
        responseBuffer = res.exchange().expectHeader().contentType("application/json;charset=UTF-8")
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.errno").isEqualTo(ResponseCode.OK.getCode())
                .jsonPath("$.errmsg").isEqualTo("成功")
                .jsonPath("$.data.page").isEqualTo(1)
                .jsonPath("$.data.pageSize").isEqualTo(3)
                .jsonPath("$.data.list[0].name").isNotEmpty()
                .jsonPath("$.data.list[0].BeginTime").isNotEmpty()
                .jsonPath("$.data.list[0].payTime").isNotEmpty()
                .jsonPath("$.data.list[0].endTime").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.id").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.name").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.skuSn").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.imageUrl").isNotEmpty()
                .jsonPath("$.data.list[0].goodsSpu.inventory").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.originalPrice").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.price").isNumber()
                .jsonPath("$.data.list[0].goodsSpu.disable").isNumber()
                .jsonPath("$.data").isArray()
                .returnResult()
                .getResponseBodyContent();


    }
}
