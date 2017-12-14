package com.hanmz.bean;

import com.github.mybatis.entity.IdEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 与mybatis配合使用
 * 一定需要一个空构造函数才能工作
 * <p>
 * Created by hanmz on 2016/8/4.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BatchUser extends IdEntity {
  private String name;
  private String password;
  private boolean isAdmin;
  private int num;
  private Date time;
  private String message;

}
