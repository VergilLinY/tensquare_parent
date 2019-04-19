package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Date;

@ConfigurationProperties("jwt.config")
public class JwtUtil {

   //超时时间(分)
   //@Value("${jwt.config.ttl}")
   private long ttl;

   //密钥
   private String key;


   public long getTtl() {
      return ttl;
   }

   public void setTtl(long ttl) {
      this.ttl = ttl;
   }

   public String getKey() {
      return key;
   }

   public void setKey(String key) {
      this.key = key;
   }

   /**
    * 生成token
    * @param id  登录用户id
    * @param name 登录用户名称
    * @param roles 登陆用户角色
    * @return token
    */
   public String generateToken(String id,String name,String roles){
      //获得当前时间毫秒数
      long currentTime = System.currentTimeMillis();
      //设置失效时间
      long expirationTime = currentTime + 1000*60*ttl;

      String token = Jwts
       .builder() //获得JwtToken的构建工具
       .setId(id) //设置载荷部分的键值对
       .setSubject(name) //设置载荷部分的键值对
       .claim("role",roles)
       .setIssuedAt(new Date(currentTime)) // 设置载荷信息 => token创建时间
       .setExpiration(new Date(expirationTime))//设置失效时间
       .signWith(SignatureAlgorithm.HS256, key)//设置密钥签名方式 以及密钥的值
       .compact();//返回Token

      return token;
   }


   //解析token
   public Claims parseToken(String token){
      try {
         Claims body = Jwts.parser()  //获得token解析器
          .setSigningKey(key) //指定解析密钥
          .parseClaimsJws(token) //解析token
          .getBody();//获得载荷部分
         return body;
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

}
