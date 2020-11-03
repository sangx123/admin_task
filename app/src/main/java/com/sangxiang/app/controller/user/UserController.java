package com.sangxiang.app.controller.user;

import com.sangxiang.app.AppBaseController;
import com.sangxiang.app.AppExecStatus;
import com.sangxiang.app.controller.login.LoginController;
import com.sangxiang.app.controller.login.UserLoginInfo;
import com.sangxiang.app.controller.login.UserLoginParam;
import com.sangxiang.app.sdk.token.UserTokenManager;
import com.sangxiang.app.utils.AppResult;
import com.sangxiang.dao.mapper.SysUserMapper;
import com.sangxiang.dao.model.SysUser;
import com.sangxiang.dao.service.SysUserService;
import com.sangxiang.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/user")
public class UserController extends AppBaseController {
    @Autowired
    private SysUserService sysUserService;
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());
    @Autowired
    private SysUserMapper sysUserMapper;
    /**
     * 获取用户自己的信息
     */
    @GetMapping("/getUserInfo")
    public AppResult<SysUser> getUserInfo(@RequestHeader("userToken") String userToken) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        SysUser userInfo= sysUserMapper.getUserInfo(userId);;
        if(userInfo==null){
            return  fail(AppExecStatus.FAIL,"用户不存在!");
        }
        return success(userInfo);
    }
}
