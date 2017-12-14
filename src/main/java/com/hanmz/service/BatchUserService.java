package com.hanmz.service;

import com.hanmz.bean.BatchUser;
import com.hanmz.mapper.BatchUserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hanmz on 2016/8/4.
 */
@Service
public class BatchUserService {
  @Resource
  private BatchUserMapper batchUserMapper;

  public void batchUpdate(List<BatchUser> users) {
    batchUserMapper.batchUpdate(users);
  }

  public void batchInsert(List<BatchUser> users) {
    batchUserMapper.batchInsert(users);
  }
}
