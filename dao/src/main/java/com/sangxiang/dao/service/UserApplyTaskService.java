package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.UserApplyTask;

import java.util.List;

public interface UserApplyTaskService extends BaseService<UserApplyTask> {
//    /**
//     * 创建任务
//     * @param
//     */
//    int createTask(Task task);
//
//
//    /**
//     *
//     * @param id
//     * @return
//     */
//    Task queryById(int id);
//    /**
//     * 分页查询任务列表
//     * @param pageNum
//     * @param pageSize
//     * @param state
//     * @return
//     */
//    PageInfo<Task> findPage(Integer pageNum, Integer pageSize, int state);
//    List<Task> getShanhuTask(int userid, int status);
//
//    PageInfo<Task> findUserPublishTaskList(Integer pageNumber, Integer pageSize, Integer state, int userId);
//
//    PageInfo<Task> findAllUserPublishTaskList(Integer pageNumber, Integer pageSize, int userId, int state);

      List<UserApplyTask> getUserApplyTask(int taskId);
}