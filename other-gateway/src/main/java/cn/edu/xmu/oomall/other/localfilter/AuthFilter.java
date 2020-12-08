package cn.edu.xmu.oomall.other.localfilter;

import cn.edu.xmu.ooad.util.JacksonUtil;
import cn.edu.xmu.ooad.util.JwtHelper;
import cn.edu.xmu.ooad.util.ResponseCode;
import com.auth0.jwt.JWT;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.function.Consumer;

/**
 * 2 * @author: LiangJi3229
 * 3 * @date: 2020/12/5 下午2:10
 * 4
 */
public class AuthFilter implements GatewayFilter, Ordered {
    private static final Logger logger= LoggerFactory.getLogger(AuthFilter.class);
    private String tokenName;
    private Integer jwtExpireTime = 3600;
    public AuthFilter(Config config){this.tokenName=config.getTokenName();}

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        // 获取请求参数
        String token = request.getHeaders().getFirst(tokenName);
        // 判断token是否为空，无需token的url在配置文件中设置
        logger.debug("filter: token = " + token);
        if (StringUtil.isNullOrEmpty(token)){
            return getErrorResponse(HttpStatus.UNAUTHORIZED,ResponseCode.AUTH_INVALID_JWT,response,"token为空");
        }
        // 判断token是否合法
        JwtHelper.UserAndDepart userAndDepart = new JwtHelper().verifyTokenAndGetClaims(token);
        if (userAndDepart == null) {
            // 若token解析不合法
            return getErrorResponse(HttpStatus.UNAUTHORIZED,ResponseCode.AUTH_INVALID_JWT,response,"token解析不合法");
        }
        Long userId=userAndDepart.getUserId();
        Long departId=userAndDepart.getDepartId();
        if(departId!=-2){
            return getErrorResponse(HttpStatus.UNAUTHORIZED,ResponseCode.AUTH_INVALID_JWT,response,"管理员无法访问普通用户URL");
        }
        Date expireTime=JWT.decode(token).getExpiresAt();
        Date nowTime=new Date();
        Long gapTime=expireTime.getTime()-nowTime.getTime();
        logger.debug("当前token过期时间-现在时间= "+gapTime+"millis");

        //此处为增加userid到header
        Consumer<HttpHeaders> httpHeadersConsumer=httpHeaders -> {
            httpHeaders.add("userid",userId.toString());
        };
        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate().headers(httpHeadersConsumer).build();
        logger.debug(serverHttpRequest.getHeaders().toString());
        exchange.mutate().request(serverHttpRequest).build();
        //request.getHeaders().add("userid",userId.toString());
        //*******************


        if(gapTime<0){
            return getErrorResponse(HttpStatus.UNAUTHORIZED,ResponseCode.AUTH_INVALID_JWT,response,"token过期");
        }
        else if(gapTime<=jwtExpireTime*1000/2){
            String newToken=new JwtHelper().createToken(userId,-2L,jwtExpireTime);
            response.getHeaders().set(tokenName,newToken);
        }
        else{
            response.getHeaders().set(tokenName,token);
        }
        return chain.filter(exchange);

    }
    public Mono<Void> getErrorResponse(HttpStatus status, ResponseCode code, ServerHttpResponse response){
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Object returnObj= cn.edu.xmu.ooad.util.ResponseUtil.fail(code,code.getMessage());
        DataBuffer db=response.bufferFactory().wrap(JacksonUtil.toJson(returnObj).getBytes());
        return response.writeWith(Mono.just(db));
    }
    public Mono<Void> getErrorResponse(HttpStatus status, ResponseCode code, ServerHttpResponse response,String message){
        response.setStatusCode(status);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        Object returnObj= cn.edu.xmu.ooad.util.ResponseUtil.fail(code,message);
        DataBuffer db=response.bufferFactory().wrap(JacksonUtil.toJson(returnObj).getBytes());
        return response.writeWith(Mono.just(db));
    }

    @Override
    public int getOrder() {
        return 0;
    }


    public static class Config {
        private String tokenName;

        public Config(){

        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }
    }
}
