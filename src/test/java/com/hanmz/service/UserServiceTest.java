package com.hanmz.service;

import com.google.common.collect.Lists;
import com.hanmz.bean.CountRelation;
import com.hanmz.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceTest {
  @Autowired
  private UserService userService;

  @Test
  public void findById() throws Exception {
    userService.set("han1", "hh");
    Date date = new Date();
    System.out.println(date.getTime());
    System.out.println(System.currentTimeMillis());
  }

  @Test
  public void count() throws Exception {
    List<CountRelation> countRelation = userService.count();
    System.out.println(countRelation.size());
  }

  @Test
  public void countAll() throws Exception {
    System.out.println(userService.countAll("success", Lists.newArrayList("123", "hanmz"), 123, new int[] {1, 2, 3}, null, new Object()));
  }

  @Test
  public void countAll1() throws Exception {
    System.out.println(userService.countAll());
  }

  @Test
  public void countName() throws Exception {
    System.out.println(userService.countName());
  }

  @Test
  public void findEntityByList() throws Exception {
    List<User> list = userService.findEntityByList();
    System.out.println(list.size());
  }

  @Test
  public void userObj() throws Exception {
    System.out.println(userService.userObj());
  }

  @Test
  public void sum() throws Exception {
    System.out.println(userService.sum());
  }

  @Test
  public void insertObject() throws Exception {
    userService.insertObject();

  }

  @Test
  public void jsonHandlerTest() throws Exception {
    long id = userService.insertObject();
    User user = userService.findById(id);
    System.out.println(user);
  }

  @Test
  public void insertTest() throws Exception {
    User user = User.builder().name("hanmz").build();
    userService.insert(user);
  }

  /**
   * 重要
   */
  @Test
  public void updateByFieldsTest() throws Exception {
    User user = User.builder().name("sss").build();
    userService.updateByFields(user, "num='1',message='test'", "name");
  }

  /**
   * 重要
   */
  @Test
  public void selectByFieldsTest() throws Exception {
    User user = User.builder().name("sss").build();
    List<User> users = userService.selectByFields(user, "name");
    System.out.println(users);
  }

  /**
   * 重要
   */
  @Test
  public void deleteByFieldsTest() throws Exception {
    //    User user = User.builder().name("sss' or `name`='hanmz").build();
    User user = User.builder().name("hanmz").build();
    userService.deleteByFields(user, "name");
  }

  @Test
  public void countPerson() throws Exception {
    userService.deletePerson();
  }

  @Test
  public void test() throws Exception {
    userService.test("han", null);
  }

  @Test
  public void test1() throws Exception {
    userService.test1("name LIKE 'han%'");
  }

  @Test
  public void list() throws Exception {
    userService.list(Lists.newArrayList(1L, 2L, 3L));
  }
}
