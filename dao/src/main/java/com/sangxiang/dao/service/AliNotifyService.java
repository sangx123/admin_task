package com.sangxiang.dao.service;

import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.BaseService;
import com.sangxiang.dao.model.AliNotify;
import com.sangxiang.dao.model.Task;

import java.util.List;

public interface AliNotifyService extends BaseService<AliNotify> {
    /**
     * 新增消息
     * @param
     */
    int insertAliNotify(AliNotify model);
}