package com.tensquare.user;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import utils.IdWorker;
import utils.JwtUtil;

@SpringBootApplication
@EnableEurekaClient
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public IdWorker idWorkker(){
		return new IdWorker(1, 1);
	}

	@Bean
	//将加密工具类交给Spring容器
	public BCryptPasswordEncoder getBCryptPasswordEncoder(){
		return new 	BCryptPasswordEncoder();
	}

	@Bean
	//将Jwt工具类交给Spring管理
	public JwtUtil getJwtUtil(){
		return new JwtUtil();
	}
}
