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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
    public class UserTaskServiceImpl extends BaseServiceImpl<UserTask>  implements UserTaskService {

    @Autowired
    UserTaskMapper userTaskMapper;


    @Transactional
    @Override
    public void createUserTask(BusinessTask task, SysUser user) {
        //task.setWorkerNum(task.getWorkerNum()-1);
        //taskMapper.updateByPrimaryKey(task);
        UserTask userTask=new UserTask();
        userTask.setBusinessTaskId(task.getId());
        userTask.setUserId(user.getId());
        userTask.setUserTaskStatus(0);
        userTask.setUserApplyTaskTime(new Date());
        userTaskMapper.insertUseGeneratedKeys(userTask);
    }

    @Override
    public void createUserTask(BusinessTask task, int userId) {
        UserTask userTask=new UserTask();
        userTask.setBusinessTaskId(task.getId());
        userTask.setUserId(userId);
        userTask.setUserTaskStatus(0);
        userTaskMapper.insertUseGeneratedKeys(userTask);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<BusinessTask> getUserTask(int userid,int state,int pageNum,int pageSize) {
        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessTask> list = userTaskMapper.queryUserTask(userid,state);
       return new PageInfo<>(list);
    }

    @Override
    public List<BusinessTask> getUserTask(int userid, int status) {
        return userTaskMapper.queryUserTask(userid,status);
    }

    @Override
    public UserTask hasApplyTask(int userId, int taskId) {
        return  userTaskMapper.queryUserHasApplyTask(userId,taskId);
    }

    @Override
    public List<UserTask> queryUserTaskListByBusinessTaskId(int businessTaskId) {
        return userTaskMapper.queryUserTaskListByBusinessTaskId(businessTaskId);
    }
}
