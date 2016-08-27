package com.hanmz.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.hanmz.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
public interface UserMapper extends ICrudMapper<User> {
  @Select("SELECT num FROM user WHERE id=#{id}")
  int selectById(@Param("id") Long id);

  @Select("SELECT * FROM user WHERE is_admin=false")
  List<User> selectByIsAdmin();

}
