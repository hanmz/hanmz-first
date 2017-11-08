package com.hanmz.bean;

import com.alibaba.fastjson.JSONObject;
import com.github.mybatis.entity.IdEntity;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

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
public class User extends IdEntity {
  private String name;
  private String password;
  private boolean isAdmin;
  private int num;
  private Instant createTime;
  private String message;
  private List<JsonBean> json;

  public void setJson(List list) {
    if (list == null) {
      json = null;
    } else if (list.isEmpty()) {
      json = Lists.newArrayList();
    } else if (JsonBean.class.equals(list.get(0).getClass())) {
      json = (List<JsonBean>) list;
    } else {
      json = JSONObject.parseArray(list.toString(), JsonBean.class);
    }
  }

}
