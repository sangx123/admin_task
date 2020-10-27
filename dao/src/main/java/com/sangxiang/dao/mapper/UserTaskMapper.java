package com.sangxiang.dao.mapper;

import com.sangxiang.base.mapper.MyMapper;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.model.UserTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTaskMapper extends MyMapper<UserTask> {
    List<BusinessTask> queryUserTask(@Param("userid")int userid, @Param("state")int state);

    UserTask queryUserHasApplyTask(@Param("userid")int userid,@Param("taskid")int taskid);

    List<UserTask> queryUserTaskListByBusinessTaskId(@Param("businessTaskId")int businessTaskId);
    //查询所有的完成的结果
    List<UserTask> querySuccessedUserTaskListByBusinessTaskId(@Param("businessTaskId")int businessTaskId);

    //查询所有的未完成的结果
    List<UserTask> queryFailedUserTaskListByBusinessTaskId(@Param("businessTaskId")int businessTaskId);

    List<UserTask> queryUserTaskListByBusinessTaskIdAndStatus(@Param("businessTaskId")int businessTaskId,@Param("user_task_status")int user_task_status);
}