package com.tensquare;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {

   @Test
   //生成token
   public void fun1() {
      String token = Jwts
       .builder() //获得JwtToken的构建工具
       .setId("888") //设置载荷部分的键值对
       .setSubject("tom") //设置载荷部分的键值对
       .setIssuedAt(new Date()) // 设置载荷信息 => token创建时间
       .signWith(SignatureAlgorithm.HS256, "itcast")//设置密钥签名方式 以及密钥的值
       .compact();//返回Token

      System.out.println(token);
   }

   @Test
   //解析token
   public void fun2() {
     String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiJ0b20iLCJpYXQiOjE1NTE3NzQwMjl9.hzti7SztHEQ0z3dTXQlxIz_T5jXHAm2KSuiBf7b1B14";

      Claims body = Jwts.parser()  //获得token解析器
       .setSigningKey("itcast") //指定解析密钥
       .parseClaimsJws(token) //解析token
       .getBody();//获得载荷部分

   //从载荷中提取信息
      String id = body.getId();
      String name = body.getSubject();
      Date date = body.getIssuedAt();

      System.out.println(id+"=>"+name+"=>"+date);

   }

   @Test
   //生成token =>自定义载荷
   public void fun3() {
      //将自定义放入map中
      Map<String,Object> map = new HashMap<>();
      map.put("abc","bcd");
      map.put("hehe","heihei");

      String token = Jwts
       .builder() //获得JwtToken的构建工具
       .setId("888") //设置载荷部分的键值对
       .setSubject("tom") //设置载荷部分的键值对
       .setIssuedAt(new Date()) // 设置载荷信息 => token创建时间
       .setClaims(map) //设置自定义载荷
       .signWith(SignatureAlgorithm.HS256, "itcast")//设置密钥签名方式 以及密钥的值
       .compact();//返回Token

      System.out.println(token);
   }

   @Test
   //解析token => 解析自定义载荷
   public void fun4() {
      String token = "eyJhbGciOiJIUzI1NiJ9.eyJhYmMiOiJiY2QiLCJoZWhlIjoiaGVpaGVpIn0.auc1I0OcejT5C51Pe2T1QepUnsZuVQoXL44n4SWw9UA";

      Claims body = Jwts.parser()  //获得token解析器
       .setSigningKey("itcast") //指定解析密钥
       .parseClaimsJws(token) //解析token
       .getBody();//获得载荷部分

      //从载荷中提取信息
      String id = body.getId();
      String name = body.getSubject();
      Date date = body.getIssuedAt();

      System.out.println(body.get("abc"));
      System.out.println(body.get("hehe"));


   }

   @Test
   //生成token => 设置有效时间
   public void fun5() {
      //将自定义放入map中
      Map<String,Object> map = new HashMap<>();
      map.put("abc","bcd");
      map.put("hehe","heihei");

      //获得当前时间毫秒数
      long currentTime = System.currentTimeMillis();

      long expirationTime = currentTime + 1000*30;

      String token = Jwts
       .builder() //获得JwtToken的构建工具
       .setId("888") //设置载荷部分的键值对
       .setSubject("tom") //设置载荷部分的键值对
       .setClaims(map) //设置自定义载荷 ,自定义载荷需要放置到有效时间之前
       .setIssuedAt(new Date(currentTime)) // 设置载荷信息 => token创建时间
       .setExpiration(new Date(expirationTime))//设置失效时间
       .signWith(SignatureAlgorithm.HS256, "itcast")//设置密钥签名方式 以及密钥的值
       .compact();//返回Token

      System.out.println(token);
   }

   @Test
   //解析token => 测试有效时间
   public void fun6() {
      String token = "eyJhbGciOiJIUzI1NiJ9.eyJhYmMiOiJiY2QiLCJoZWhlIjoiaGVpaGVpIiwiZXhwIjoxNTUxNzc2Njk4LCJpYXQiOjE1NTE3NzY2Njh9.pjGINYe7qHCKNLYai_hd52CPSZdFHwcuigBIxQzzOjo";

      Claims body = Jwts.parser()  //获得token解析器
       .setSigningKey("itcast") //指定解析密钥
       .parseClaimsJws(token) //解析token
       .getBody();//获得载荷部分

      //从载荷中提取信息
      String id = body.getId();
      String name = body.getSubject();
      Date date = body.getIssuedAt();

      System.out.println(body.getIssuedAt());
      System.out.println(body.getExpiration());
      System.out.println(body.get("abc"));
      System.out.println(body.get("hehe"));


   }
}
