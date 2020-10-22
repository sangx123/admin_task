package com.sangxiang.dao.mapper;


import com.sangxiang.base.mapper.MyMapper;
import com.sangxiang.dao.model.BusinessTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BusinessTaskMapper extends MyMapper<BusinessTask> {
    BusinessTask queryById(int id);
    List<BusinessTask> getShanhuTask(@Param("userid")int userid, @Param("status")int status);

    List<BusinessTask> getEnableTask();
    List<BusinessTask> getUserPublishTask(@Param("userid")int userid, @Param("status")int status);
    List<BusinessTask> getAllUserPublishTask(@Param("userid")int userid, @Param("status")int status);
}