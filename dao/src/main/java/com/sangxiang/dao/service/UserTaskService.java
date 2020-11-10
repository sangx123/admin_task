package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.mapper.MyMapper;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.model.SysUser;
import com.sangxiang.dao.model.UserTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTaskService extends BaseService<UserTask> {
    //void createUserTask(BusinessTask task, SysUser user);]
    //申请任务
    void createUserTask(BusinessTask task,int userId);
    PageInfo<BusinessTask> getUserTask(int userid,int state,int pageNum, int pageSize);
    List<BusinessTask> getUserTask(int userid, int status);
    UserTask hasApplyTask(int userId, int id);
    //获取任务详情中用的到
    //通过businessId获取该任务下所有任务的消息
    List<UserTask> queryUserTaskListByBusinessTaskId(int businessTaskId);
    //查询任务
    List<UserTask> queryUserTaskListByBusinessTaskIdAndStatus(int businessTaskId,int status);
    //查询所有的完成的结果(审核超时完成4，审核成功5，审核申诉超时成功8，申诉成功9，)
    List<UserTask> querySuccessedUserTaskListByBusinessTaskId(@Param("businessTaskId")int businessTaskId);

    //查询所有的失败的结果(审核失败6，申诉失败10)
    List<UserTask> queryFailedUserTaskListByBusinessTaskId(@Param("businessTaskId")int businessTaskId);

    /***
     * 根据主键查询信息
     * @param id
     * @return
     */
    UserTask queryUserTaskById(int id);
}