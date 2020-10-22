package com.sangxiang.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.impl.BaseServiceImpl;
import com.sangxiang.dao.mapper.TaskMainTyleMapper;

import com.sangxiang.dao.model.TaskMainTyle;
import com.sangxiang.dao.service.TaskMainTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskMainTypeServiceImpl extends BaseServiceImpl<TaskMainTyle> implements TaskMainTypeService {
    @Autowired
    TaskMainTyleMapper taskMainTyleMapper;



    @Override
    public List<TaskMainTyle> getAllTaskMainType() {
        return taskMainTyleMapper.selectAll();
    }
}
