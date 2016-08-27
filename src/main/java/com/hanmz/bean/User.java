package com.hanmz.bean;

import com.github.mybatis.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

/**
 * 与mybatis配合使用
 * 一定需要一个空构造函数才能工作
 * <p>
 * Created by hanmz on 2016/8/4.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User extends IdEntity {
  private String name;
  private String password;
  private boolean isAdmin;
  private int num;


}
