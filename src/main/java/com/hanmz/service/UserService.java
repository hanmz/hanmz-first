package com.hanmz.service;

import com.github.jedis.support.JedisCmd;
import com.google.common.collect.Lists;
import com.hanmz.aop.ParamAnno;
import com.hanmz.bean.CountRelation;
import com.hanmz.bean.JsonBean;
import com.hanmz.bean.User;
import com.hanmz.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
@Service
public class UserService {
  @Resource
  private UserMapper userMapper;
  @Resource
  private JedisCmd jedis;

  public void set(String key, String value) {
    jedis.set(key, value);
  }

  List<CountRelation> count() {
    return userMapper.count();
  }

  @ParamAnno
  int countAll(String test, List<String> list, int value, int[] ints, Object o1, Object o2) {
    return userMapper.countAll();
  }

  @ParamAnno
  int countAll() {
    return userMapper.countAll();
  }

  int countName() {
    String condition = "('1','han')";
    return userMapper.countName(condition);
  }

  List<User> findEntityByList() {
    List<String> conditions = Lists.newArrayList("");
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    conditions.forEach(c -> sb.append("'").append(c).append("'").append(','));
    if (sb.charAt(sb.length() - 1) == ',') {
      sb.replace(sb.length() - 1, sb.length(), ")");
    } else {
      sb.append(")");
    }
    return userMapper.findEntityByList(sb.toString());
  }

  int userObj() {
    User user = new User();
    user.setName("2");
    return userMapper.userObj(user);
  }

  int countPerson() {
    return userMapper.countPerson();
  }

  void deletePerson() {
    userMapper.deletePerson("dshjkfshdjkf");
  }

  int sum() {
    return userMapper.sum();
  }

  /**
   * 插入对象
   */
  long insertObject() {
    User user = new User();
    user.setName("hanmz");
    user.setPassword("hanmz");
    user.setAdmin(true);

    user.setJson(Lists.newArrayList(JsonBean.builder().valStr("韩明泽").valInt(123).build()));
    userMapper.insertAndSetObjectId(user);
    return user.getId();
  }

  /**
   * 插入对象
   */
  User findById(long id) {
    return userMapper.findById(id);
  }

  void insert(User user) {
    userMapper.insert(user);
  }

  void updateByFields(User user, String statement, String... fields) {
    userMapper.updateByFields(user, statement, fields);
  }

  List<User> selectByFields(User user, String... fields) {
    return userMapper.selectByFields(user, fields);
  }

  void deleteByFields(User user, String... fields) {
    userMapper.deleteByFields(user, fields);
  }

  void test(String like, String notLike) {
    System.out.println(userMapper.test(like, notLike).size());
  }

  void test1(String query) {
    System.out.println(userMapper.test1(query).size());
  }

  void list(List<Long> query) {
    System.out.println(userMapper.findByNums(query).size());
  }
}
