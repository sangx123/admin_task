package com.sangxiang.dao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sangxiang.base.service.impl.BaseServiceImpl;
import com.sangxiang.dao.mapper.AliNotifyMapper;
import com.sangxiang.dao.model.AliNotify;

import com.sangxiang.dao.service.AliNotifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AliNotifyServiceImpl extends BaseServiceImpl<AliNotify> implements AliNotifyService {
    @Autowired
    AliNotifyMapper aliNotifyMapper;

    @Override
    public int insertAliNotify(AliNotify model) {
        aliNotifyMapper.insertUseGeneratedKeys(model);
         return model.getId();
    }
}
