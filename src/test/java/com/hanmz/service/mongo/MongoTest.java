package com.hanmz.service.mongo;

import com.github.autoconf.ConfigFactory;
import com.github.mongo.support.DatastoreExt;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hanmz.bean.TestEntity;
import com.sun.tools.doclets.internal.toolkit.util.DocFinder;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.Entity;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hanmz on 2016/11/7.
 */
public class MongoTest {
  private static DatastoreExt datastore;

  public static void main(String[] args) throws Exception {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:mongo.xml");
    context.start();
    datastore = (DatastoreExt) context.getBean("mongoDatastore");

//    insert();
        query();
//        find();
  }

  private static void insert() {
    TestEntity testEntity = new TestEntity();
    testEntity.setHost("172.31.101.10");
    testEntity.setOther("other");
    testEntity.setTime(System.currentTimeMillis());

    System.out.println(datastore.save(testEntity).getId());
  }

  private static void find() {
    Query<TestEntity> query = datastore.find(TestEntity.class).filter("other =", "other");

    List<TestEntity> list = query.asList();
    list.forEach(entity -> System.out.println(entity.getBlank()));
  }

  private static void query() {
    Query<TestEntity> query =
      datastore.find(TestEntity.class).filter("time <=", System.currentTimeMillis() + 1000 * 60 * 60);

    UpdateOperations<TestEntity> updateOperations =
      datastore.createUpdateOperations(TestEntity.class).inc("time", 1000 * 60 * 60).set("blank", "null");

    List<TestEntity> list = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      list.add(datastore.findAndModify(query, updateOperations));
    }
    System.out.println(System.currentTimeMillis() + 1000 * 5);
    list.forEach(System.out::println);
  }

}
