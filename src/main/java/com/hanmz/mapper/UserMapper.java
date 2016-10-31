package com.hanmz.mapper;

import com.github.mybatis.mapper.ICrudMapper;
import com.hanmz.bean.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.type.MappedTypes;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
public interface UserMapper extends ICrudMapper<User> {
  @Select("SELECT num FROM user WHERE id=#{id}")
  int selectById(@Param("id") Long id);

  @Select("SELECT * FROM user WHERE message=#{message}")
  User findByMessage(@Param("message") String message);

}
