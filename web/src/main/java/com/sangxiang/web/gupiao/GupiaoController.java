package com.sangxiang.web.gupiao;

import com.github.pagehelper.PageInfo;
import com.google.gson.reflect.TypeToken;
import com.sangxiang.base.rest.ApiExecStatus;
import com.sangxiang.base.rest.ApiResult;
import com.sangxiang.base.rest.BaseResource;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.service.BusinessTaskService;
import com.sangxiang.dao.service.UserTaskService;
import com.sangxiang.model.ApplyTaskParam;
import com.sangxiang.model.Login.HomeTaskParam;
import com.sangxiang.web.oauth2.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Api(description="股票" )
public class GupiaoController extends BaseResource {

    @GetMapping(value = "/getxingtaitwo")
    @ApiOperation(value="获取股票形态学数据")
    public ApiResult<List<Diff>> getXingTai2(){
        Date now=new Date();

        File file=new File(Config.getToadyPath());
        if(!file.exists()){
           return fail(ApiExecStatus.NO_FILE,"9：10分后才会运行今日程序");
        }else {
            String content= Utils.readFile(Config.getToadyPath());
            Type type = new TypeToken<ArrayList<Diff>>() {
            }.getType();
            List<Diff> diffList= Config.getGson().fromJson(content,type);
            return success(diffList);
        }



    }



}
