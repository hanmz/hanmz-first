package com.hanmz.web;

import com.google.common.collect.Lists;
import com.hanmz.bean.BatchUser;
import com.hanmz.bean.User;
import com.hanmz.service.BatchUserService;
import com.hanmz.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * home controller
 *
 * @author hanmz 2016/7/31
 */
@Slf4j
@RestController
public class UserController {
  @Resource
  private UserService userService;
  @Resource
  private BatchUserService batchUserService;

  @RequestMapping(value = "/user/insert", method = RequestMethod.GET)
  public String insertTest() throws Exception {
    User user = User.builder().name("hanmz").build();
    userService.insert(user);
    return "OK";
  }

  @RequestMapping(value = "/user/find", method = RequestMethod.GET)
  public String findTest() throws Exception {
    userService.findById(50);
    userService.findAll();
    return "OK";
  }

  @RequestMapping(value = "/user/update", method = RequestMethod.GET)
  public String updateTest() throws Exception {
    User user = User.builder().name("韩明泽").time(new Date()).build();
    user.setId(50L);
    userService.update(user);
    return "OK";
  }

  @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
  public String deleteTest() throws Exception {
    userService.delete(User.builder().name("hanmz").build());
    userService.deleteById(0L);
    return "OK";
  }

  @RequestMapping(value = "/user/json", method = RequestMethod.GET)
  public String jsonHandlerTest() throws Exception {
    long id = userService.insertObject();
    User user = userService.findById(id);

    log.info(user.toString());
    return "OK";
  }

  @RequestMapping(value = "/user/findEntityByList", method = RequestMethod.GET)
  public String findEntityByList() throws Exception {
    userService.findEntityByList();
    return "OK";
  }

  /**
   * 批量处理
   */
  @RequestMapping(value = "/user/batchUpdate", method = RequestMethod.GET)
  public String batchUpdate() throws Exception {
    BatchUser user1 = BatchUser.builder().build();
    user1.setId(100L);
    BatchUser user2 = BatchUser.builder().build();
    user2.setId(200L);
    List<BatchUser> users = Lists.newArrayList(user1, user2);
    batchUserService.batchUpdate(users);
    return "OK";
  }

  @RequestMapping(value = "/user/batchInsert", method = RequestMethod.GET)
  public String batchInsert() throws Exception {
    List<BatchUser> users = Lists.newArrayList(BatchUser.builder().name("hanmz").build(), BatchUser.builder().name("hanmz").build());
    batchUserService.batchInsert(users);
    return "OK";
  }


  @RequestMapping(value = "/user/test", method = RequestMethod.GET)
  public String test() throws Exception {
    return "OK";
  }
}
