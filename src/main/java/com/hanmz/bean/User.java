package com.hanmz.bean;

import com.github.mybatis.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 与mybatis配合使用
 * 一定需要一个空构造函数才能工作
 * <p>
 * Created by hanmz on 2016/8/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends IdEntity {
  private String name;
  private String password;
  private boolean isAdmin;
  private int num;
  private Instant createTime;
  private String message;
  private JsonBean json;
}
