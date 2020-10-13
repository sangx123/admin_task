package com.sangxiang.dao.mapper;

import com.sangxiang.base.mapper.MyMapper;
import com.sangxiang.dao.model.UserApplyTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserApplyTaskMapper extends MyMapper<UserApplyTask> {

   List<UserApplyTask> getUserApplyTask(@Param("taskId")int taskId);
}