package com.tensquare;


import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MongoTest {

    @Test  // 查询所有文档
    public void baseTest() {
        /*
            db.spit.insert({_id:"1",content:"我还是没有想明白到底为啥出错",userid:"1012",nickname:"小明",visits:NumberInt(2020)});
            db.spit.insert({_id:"2",content:"加班到半夜",userid:"1013",nickname:"凯撒",visits:NumberInt(1023)});
            db.spit.insert({_id:"3",content:"手机流量超了咋办？",userid:"1013",nickname:"凯撒",visits:NumberInt(111)});
            db.spit.insert({_id:"4",content:"坚持就是胜利",userid:"1014",nickname:"诺诺",visits:NumberInt(1223)});
         */
        //建立连接
        MongoClient client = new MongoClient("47.102.156.57");
        //打开数据库
        MongoDatabase spitdb = client.getDatabase("spitdb");
        //获取集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //查询数据获取文档集合
        FindIterable<Document> documents = spit.find();

        for (Document document : documents) {
            System.out.println(document.getString("content"));
            System.out.println(document.getString("userid"));
            System.out.println(document.getInteger("visits"));
        }
        client.close();
    }

    @Test  // 查询所有文档
    public void findByConditionTest() {
        //建立连接
        MongoClient client = new MongoClient("47.102.156.57");
        //打开数据库
        MongoDatabase spitdb = client.getDatabase("spitdb");
        //获取集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //构建查询条件
        BasicDBObject bson = new BasicDBObject("userid", "1013");
        //查询数据获取文档集合
        FindIterable<Document> documents = spit.find(bson);

        for (Document document : documents) {
            System.out.println(document.getString("content"));
            System.out.println(document.getString("userid"));
            System.out.println(document.getInteger("visits"));
        }
        client.close();
    }

    @Test  // 查询visits大于1000
    public void findByCondition2Test() {
        //建立连接
        MongoClient client = new MongoClient("47.102.156.57");
        //打开数据库
        MongoDatabase spitdb = client.getDatabase("spitdb");
        //获取集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //构建查询条件
        BasicDBObject bson = new BasicDBObject("visits", new BasicDBObject("$gt", 1000));
        //查询数据获取文档集合
        FindIterable<Document> documents = spit.find(bson);

        for (Document document : documents) {
            System.out.print(document.getString("content") + "    ");
            System.out.print(document.getString("userid") + "    ");
            System.out.println(document.getInteger("visits"));
        }
        client.close();
    }

    @Test  // 查询visits大于1000
    public void insertTest() {
        //建立连接
        MongoClient client = new MongoClient("47.102.156.57");
        //打开数据库
        MongoDatabase spitdb = client.getDatabase("spitdb");
        //获取集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");
        //构建集合数据
        Map<String, Object> map = new HashMap<>();
        map.put("content", "我要吐槽");
        map.put("userid", "9999");
        map.put("visits", 123);
        map.put("publishtime", new Date());
        Document document = new Document(map);

        //插入数据
        spit.insertOne(document);

        client.close();
    }
}
