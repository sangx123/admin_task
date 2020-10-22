package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.mapper.MyMapper;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.model.SysUser;
import com.sangxiang.dao.model.UserTask;

import java.util.List;

public interface UserTaskService extends BaseService<UserTask> {
    void createUserTask(BusinessTask task, SysUser user);
    void createUserTask(BusinessTask task,int userId);
    PageInfo<BusinessTask> getUserTask(int userid,int state,int pageNum, int pageSize);
    List<BusinessTask> getUserTask(int userid, int status);
    UserTask hasApplyTask(int userId, int id);
}