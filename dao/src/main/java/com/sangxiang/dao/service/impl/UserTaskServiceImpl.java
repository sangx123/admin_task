package com.sangxiang.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.base.service.impl.BaseServiceImpl;
import com.sangxiang.dao.mapper.BusinessTaskMapper;
import com.sangxiang.dao.mapper.UserTaskMapper;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.model.SysUser;
import com.sangxiang.dao.model.UserTask;
import com.sangxiang.dao.service.UserTaskService;
import com.sangxiang.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
    public class UserTaskServiceImpl extends BaseServiceImpl<UserTask>  implements UserTaskService {

    @Autowired
    UserTaskMapper userTaskMapper;


    @Override
    public void createUserTask(BusinessTask task, int userId) {
        UserTask userTask=new UserTask();
        userTask.setBusinessTaskId(task.getId());
        userTask.setUserId(userId);
        userTask.setBusinessUserId(task.getUserid());
        userTask.setUserTaskStatus(0);
        userTask.setUserApplyTaskTime(new Date());
        userTask.setUserFirstSubmitTaskTimeout(DateUtils.stringToDateDefault(task.getWorkEndTime()));
        userTaskMapper.insertUseGeneratedKeys(userTask);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<BusinessTask> getUserTask(int userid,int state,int pageNum,int pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        //List<BusinessTask> list = userTaskMapper.queryUserTask(userid,state);
       return new PageInfo<>(new ArrayList<>());
    }

    @Override
    public List<BusinessTask> getUserTask(int userid, int status) {
        //return userTaskMapper.queryUserTask(userid,status);
        return new ArrayList<>();
    }

    @Override
    public UserTask hasApplyTask(int userId, int taskId) {
        return  userTaskMapper.queryUserHasApplyTask(userId,taskId);
    }

    @Override
    public List<UserTask> queryUserTaskListByBusinessTaskId(int businessTaskId) {
        return userTaskMapper.queryUserTaskListByBusinessTaskId(businessTaskId);
    }

    @Override
    public List<UserTask> queryUserTaskListByBusinessTaskIdAndStatus(int businessTaskId, int status) {
        return userTaskMapper.queryUserTaskListByBusinessTaskIdAndStatus(businessTaskId,status);
    }

    @Override
    public List<UserTask> querySuccessedUserTaskListByBusinessTaskId(int businessTaskId) {
        return userTaskMapper.querySuccessedUserTaskListByBusinessTaskId(businessTaskId);
    }

    @Override
    public List<UserTask> queryFailedUserTaskListByBusinessTaskId(int businessTaskId) {
        return userTaskMapper.queryFailedUserTaskListByBusinessTaskId(businessTaskId);
    }

    @Override
    public UserTask queryUserTaskById(int id) {
        return userTaskMapper.queryUserTaskById(id);
    }

    @Override
    public List<UserTask> getMyJieShouTaskQuanBu(int userid) {
        return userTaskMapper.getMyJieShouTaskQuanBu(userid);
    }

    @Override
    public List<UserTask> getMyJieShouTaskDaiTiJiao(int userid) {
        return userTaskMapper.getMyJieShouTaskDaiTiJiao(userid);
    }

    @Override
    public List<UserTask> getMyJieShouTaskShengHe(int userid) {
        return userTaskMapper.getMyJieShouTaskShengHe(userid);
    }

    @Override
    public List<UserTask> getMyJieShouTaskShenHeSuccess(int userid) {
        return userTaskMapper.getMyJieShouTaskShenHeSuccess(userid);
    }

    @Override
    public List<UserTask> getMyJieShouTaskShenHeFail(int userid) {
        return userTaskMapper.getMyJieShouTaskShenHeFail(userid);
    }

    @Override
    public List<UserTask> asnyUserTaskTimeOut(int userid) {
        return userTaskMapper.asnyUserTaskTimeOut(userid);
    }
}
