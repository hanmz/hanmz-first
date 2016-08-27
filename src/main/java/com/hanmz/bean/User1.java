package com.hanmz.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by hanmz on 2016/8/9.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User1 {
  private String name;
  private String password;
  private boolean isAdmin;
}
