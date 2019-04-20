package com.tensquare.user;

import com.tensquare.user.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//使用该类配置SpringMVC(拦截器)
@Configuration
public class WebConfiguration extends WebMvcConfigurationSupport {

   @Autowired
   private JwtInterceptor inter;

   //添加(注册)拦截器
   @Override
   protected void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(inter)//将Jwt拦截器添加
              .addPathPatterns("/**"); //指定拦截路径

   }
}
