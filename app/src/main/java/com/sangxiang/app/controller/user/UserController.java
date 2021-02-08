package com.sangxiang.app.controller.user;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangxiang.app.AppBaseController;
import com.sangxiang.app.AppExecStatus;
import com.sangxiang.app.controller.login.LoginController;
import com.sangxiang.app.controller.login.UserLoginInfo;
import com.sangxiang.app.controller.login.UserLoginParam;
import com.sangxiang.app.sdk.token.UserTokenManager;
import com.sangxiang.app.utils.AppResult;
import com.sangxiang.dao.mapper.SysUserMapper;
import com.sangxiang.dao.model.BusinessTask;
import com.sangxiang.dao.model.SysUser;
import com.sangxiang.dao.service.SysUserService;
import com.sangxiang.model.Task.TaskItem;
import com.sangxiang.util.DateUtils;
import com.sangxiang.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Value("${upload.imagePath}")
    private String imagePath;

    @Value("${upload.imageURL}")
    private String imageURL;


    @GetMapping("/getUserInfo")
    public AppResult<SysUser> getUserInfo(@RequestHeader("userToken") String userToken) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        SysUser userInfo= sysUserMapper.getUserInfo(userId);
        if(userInfo==null){
            return  fail(AppExecStatus.FAIL,"用户不存在!");
        }
        return success(userInfo);
    }


    @PostMapping("/uploadHeadImage")
    public AppResult<String> uploadHeadImage(@RequestHeader("userToken") String userToken,
                                        @RequestParam(value = "imageList[]") MultipartFile files[],
                                        String nickName

    ) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        if(files.length==0){
            return  fail(AppExecStatus.FAIL,"请选择图片");
        }
        String avater=convertUpLoadFileAndContent(files);
        SysUser userInfo= sysUserMapper.getUserInfo(userId);
        userInfo.setAvatar(avater);
        sysUserService.updateSelective(userInfo);
        return success(avater);

    }


    private String convertUpLoadFileAndContent(MultipartFile[] files){
        List<String> imageUrlList = new ArrayList<String>();
        File uploadDirectory = new File(imagePath);
        if (uploadDirectory.exists()) {
            if (!uploadDirectory.isDirectory()) {
                uploadDirectory.delete();
            }
        } else {
            uploadDirectory.mkdir();
        }
        //这里可以支持多文件上传
        if (files != null && files.length >= 1) {
            BufferedOutputStream bw = null;
            try {
                for (MultipartFile file : files) {
                    String fileName = file.getOriginalFilename();
                    //判断是否有文件且是否为图片文件
                    if (fileName != null && !"".equalsIgnoreCase(fileName.trim()) && isImageFile(fileName)) {
                        String imageName=DateUtils.format(new Date(),"yyyyMMddHHmmssSSS") + getFileType(fileName);
                        String filePath=imagePath + "/" + imageName;
                        //创建输出文件对象
                        File outFile = new File(filePath);
                        //拷贝文件到输出文件对象
                        FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
                        imageUrlList.add(imageURL+imageName);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            } finally {
                try {
                    if (bw != null) {
                        bw.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageUrlList.get(0);
    }

    /**
     * 获取文件后缀名     *     * @param fileName     * @return
     */
    private String getFileType(String fileName) {
        if (fileName != null && fileName.indexOf(".") >= 0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }


    private Boolean isImageFile(String fileName) {
        String[] img_type = new String[]{".jpg", ".jpeg", ".png", ".gif", ".bmp"};
        if (fileName == null) {
            return false;
        }
        fileName = fileName.toLowerCase();
        for (String type : img_type) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
