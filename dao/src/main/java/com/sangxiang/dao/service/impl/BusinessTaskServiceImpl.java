package com.sangxiang.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.impl.BaseServiceImpl;
import com.sangxiang.dao.mapper.BusinessTaskMapper;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.service.BusinessTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessTaskServiceImpl extends BaseServiceImpl<BusinessTask> implements BusinessTaskService {
    @Autowired
    BusinessTaskMapper businessTaskMapper;

    @Override
    public int createTask(BusinessTask task) {
        businessTaskMapper.insertUseGeneratedKeys(task);
         return task.getId();
    }

    @Override
    public BusinessTask queryById(int id) {
        return businessTaskMapper.queryById(id);
    }

    @Override
    public PageInfo<BusinessTask> geHomeBusinessTaskPageList(Integer pageNum, Integer pageSize, int state, int type) {
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessTask> list = businessTaskMapper.getHomeBusinessTaskPageList(state,type);
        return new PageInfo<>(list);
    }

    @Transactional(readOnly = true)
    @Override
    public PageInfo<BusinessTask> findPage(Integer pageNum, Integer pageSize, int state) {
//        Example example = new Example(Task.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("state", state);
//
//        //倒序
//        example.orderBy("createTime").desc();

        //分页
        PageHelper.startPage(pageNum,pageSize);
        List<BusinessTask> list = businessTaskMapper.getEnableTask();
        return new PageInfo<>(list);
    }

    @Override
    public List<BusinessTask> getShanhuTask(int userid, int status) {
        return businessTaskMapper.getShanhuTask(userid,status);
    }

    @Override
    public PageInfo<BusinessTask> findUserPublishTaskList(Integer pageNumber, Integer pageSize, Integer state, int userId) {
        PageHelper.startPage(pageNumber,pageSize);
        List<BusinessTask> list = businessTaskMapper.getUserPublishTask(userId,state);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<BusinessTask> findAllUserPublishTaskList(Integer pageNumber, Integer pageSize, int userId,int state) {
        PageHelper.startPage(pageNumber,pageSize);
        List<BusinessTask> list = businessTaskMapper.getAllUserPublishTask(userId,state);
        return new PageInfo<>(list);
    }

    @Override
    public BusinessTask getTaskByUUID(String mUUID) {
        return businessTaskMapper.getTaskByUUID(mUUID);
    }


}
