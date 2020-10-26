package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.BusinessTask;

import java.util.List;

public interface BusinessTaskService extends BaseService<BusinessTask> {
    /**
     * 创建任务
     * @param
     */
    int createTask(BusinessTask task);


    /**
     *
     * @param id
     * @return
     */
    BusinessTask queryById(int id);

    /**
     * 分页查询任务列表
     * @param pageNum
     * @param pageSize
     * @param state
     * @return
     */
    PageInfo<BusinessTask> geHomeBusinessTaskPageList(Integer pageNum, Integer pageSize, int state,int type);


    PageInfo<BusinessTask> findPage(Integer pageNum, Integer pageSize, int state);

    List<BusinessTask> getShanhuTask(int userid, int status);

    PageInfo<BusinessTask> findUserPublishTaskList(Integer pageNumber, Integer pageSize, Integer state, int userId);

    PageInfo<BusinessTask> findAllUserPublishTaskList(Integer pageNumber, Integer pageSize, int userId, int state);
}