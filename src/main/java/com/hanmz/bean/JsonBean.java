package com.hanmz.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * *
 * Created by hanmz on 2017/10/13.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBean {
  private String valStr;
  private int valInt;
  private String[] valArr;
}
