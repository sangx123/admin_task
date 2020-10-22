package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.TaskMainTyle;

import java.util.List;

public interface TaskMainTypeService extends BaseService<TaskMainTyle> {
    List<TaskMainTyle> getAllTaskMainType();
}