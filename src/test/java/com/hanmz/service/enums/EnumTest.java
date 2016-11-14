package com.hanmz.service.enums;

/**
 * Created by hanmz on 2016/11/10.
 */
public enum EnumTest {
  RED("1"), YELLOW("2");

  private final String colour;

  EnumTest(String colour) {
    this.colour = colour;
  }

  public String getColour() {
    return colour;
  }
}
