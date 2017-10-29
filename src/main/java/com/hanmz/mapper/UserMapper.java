package com.hanmz.mapper;

import com.github.mybatis.annotation.AutoResultMap;
import com.github.mybatis.mapper.ICrudMapper;
import com.hanmz.bean.CountRelation;
import com.hanmz.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
public interface UserMapper extends ICrudMapper<User> {
  @Select("SELECT * FROM user WHERE name IN ${condition}")
  List<User> findEntityByList(@Param("condition") String condition);

  @Select("SELECT num FROM user WHERE id=#{id}")
  int selectById(@Param("id") Long id);

  @Select("SELECT name,count(*) as count FROM user GROUP BY name")
  List<CountRelation> count();

  @Select("SELECT count(*) FROM user WHERE name IN ${condition}")
  int countName(@Param("condition") String condition);

  @Select("SELECT count(*) FROM user WHERE name = #{name}")
  int userObj(User user);

  @Insert("INSERT INTO user (name,password,is_admin) VALUES (#{name},#{password},#{isAdmin})")
  void insertObject(User user);

  @Select("SELECT count(*) FROM person")
  int countPerson();

  @Select("SELECT sum(num) FROM user WHERE num=33")
  int sum();

  @Delete("DELETE FROM user WHERE name=#{name}")
  void deletePerson(@Param("name") String name);

  @Update("UPDATE user SET password=1234 WHERE name=#{name}")
  long updatePerson(@Param("name") String name);

  @Select("SELECT * FROM user WHERE name LIKE #{name_like} AND name NOT LIKE #{name_not_like}")
  List<User> test(@Param("name_like") String name_like, @Param("name_not_like") String name_not_like);

  @Select("SELECT * FROM user WHERE ${query}")
  List<User> test1(@Param("query") String query);

  List<User> findByNums(@Param("nums") List<Long> num);

  @UpdateProvider(type = MyProvider.class, method = "update")
  void updateByFields(User user, String statement, String... fields);

  @SelectProvider(type = MyProvider.class, method = "select")
  @AutoResultMap
  List<User> selectByFields(User user, String... fields);

  @DeleteProvider(type = MyProvider.class, method = "delete")
  void deleteByFields(User user, String... fields);


}
