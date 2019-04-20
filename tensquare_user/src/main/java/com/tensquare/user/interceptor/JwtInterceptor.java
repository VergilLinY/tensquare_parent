package com.tensquare.user.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import utils.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//拦截器 => 抽取从request中获得token的代码
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {
   @Autowired
   private JwtUtil jwtUtil;
   @Override
   //在controller方法执行前,先执行
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

      //1 . 获得 Authorization 头内容(token)
      String header =	request.getHeader("Authorization");
      //2 判断是否存在该请求头
      if(!StringUtils.isEmpty(header) && header.startsWith("Bearer ")){ //存在,并且复合约定
         //3 截掉"Bearer "部分,并解析token
         String token =  header.substring(7);
         Claims claims = jwtUtil.parseToken(token);
         if(claims != null){ //解析出用户信息
            //将用户信息放入request域
            String role = (String) claims.get("role");
            //claims_admin 管理员
            //claims_user 普通游客
            request.setAttribute("claims_"+role,claims);
            System.out.println("将Jwt的载荷信息放入request域:claims_"+role);
         }
      }
      //放行
      return true;
   }
}
