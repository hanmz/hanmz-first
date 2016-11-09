package com.hanmz.bean;


import lombok.Data;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * 测试 mongo
 * Created by hanmz on 2016/11/7.
 */
@Data
@Entity(value = "test_entity", noClassnameStored = true)
public class TestEntity {
  @Id
  private ObjectId hanmz;
  private String host;
  private String other;
  private long time;
  private String blank;
  private int num;
}
