package com.hanmz.web;

import com.hanmz.service.JsonService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

/**
 * home controller
 * Created by hanmz on 2016/7/31.
 */
@Controller
public class HomeController {
  @Resource
  private JsonService jsonService;

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public String home() {
    return "home";
  }

  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public String test() {
    jsonService.show();
    return "test";
  }

}
