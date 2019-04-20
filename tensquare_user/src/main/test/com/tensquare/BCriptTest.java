package com.tensquare;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCriptTest {

   @Test
   //加密
   public void fun1() {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      System.out.println(encoder.encode("123456"));

      //$2a$10$AKrhVGqcZxRTAUaS63iIWOdjM3/9JtG0oClTst1EbLuT0F2s1D0Cq
      //$2a$10$hF/gXY5yH4mEjYLLJDRru.hnO13zIBVv8grjjYrZSQGuQRW1OOz7W
      //$2a$10$RQ5kRGmFOTWa9SjMi2gINOG17zeDrX9cugTIumpRsZlDZBIARhCM.

   }

   @Test
   //校验
   public void fun2() {
      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

      System.out.println(encoder.matches("123456","$2a$10$AKrhVGqcZxRTAUaS63iIWOdjM3/9JtG0oClTst1EbLuT0F2s1D0Cq"));
      System.out.println(encoder.matches("123456","$2a$10$hF/gXY5yH4mEjYLLJDRru.hnO13zIBVv8grjjYrZSQGuQRW1OOz7W"));
      System.out.println(encoder.matches("123456","$2a$10$RQ5kRGmFOTWa9SjMi2gINOG17zeDrX9cugTIumpRsZlDZBIARhCM."));
      //$2a$10$AKrhVGqcZxRTAUaS63iIWOdjM3/9JtG0oClTst1EbLuT0F2s1D0Cq
      //$2a$10$hF/gXY5yH4mEjYLLJDRru.hnO13zIBVv8grjjYrZSQGuQRW1OOz7W
      //$2a$10$RQ5kRGmFOTWa9SjMi2gINOG17zeDrX9cugTIumpRsZlDZBIARhCM.

   }
}
