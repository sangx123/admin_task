package com.sangxiang.app.controller.task;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangxiang.app.AppBaseController;
import com.sangxiang.app.AppExecStatus;
import com.sangxiang.app.sdk.token.UserTokenManager;
import com.sangxiang.app.utils.AppResult;
import com.sangxiang.dao.model.*;
import com.sangxiang.dao.service.*;
import com.sangxiang.model.ApplyTaskParam;
import com.sangxiang.model.Login.HomeTaskParam;
import com.sangxiang.model.Task.TaskItem;
import com.sangxiang.model.Task.TaskParam;
import com.sangxiang.model.UserCenter.MyJieShouTaskParam;
import com.sangxiang.model.UserCenter.UserTaskParam;
import com.sangxiang.util.DateUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/asnytask")
public class AsnyTaskController extends AppBaseController {

    private static final Logger logger = Logger.getLogger(AsnyTaskController.class.getName());
    // token 过期时间， 默认2小时后过期
    @Value("${upload.imagePath}")
    private String imagePath;

    // token 过期时间， 默认2小时后过期
    @Value("${upload.imageURL}")
    private String imageURL;


    @Value("${ali_appid}")
    private String alipayAppID;

    @Value("${ali_private_key}")
    private String alipayPrivateKey;

    @Value("${ali_public_key}")
    private String alipayPublicKey;

    @Value("${ali_sign_type}")
    private String alipaySignType;

    @Value("${ali_url}")
    private String alipayUrl;

    @Value("${ali_timeout_express}")
    private String alipayTimeoutExpress;

    @Value("${ali_product_code}")
    private String alipayProductCode;

    @Value("${ali_notify_url}")
    private String alipayNotifyUrl;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    BusinessTaskService businessTaskService;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    AliNotifyService aliNotifyService;

    @Autowired
    TaskMainTypeService taskMainTypeService;
    @PostMapping("/asnyBusinessTaskTimeOut")
    public AppResult<String> asnyBusinessTaskTimeOut(@RequestHeader("userToken") String userToken) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        return success("");
//        content=convertUpLoadFileAndContent(files,content);
//        if(content.equals("0")){
//            return  fail(AppExecStatus.FAIL,"图片上传失败，请重新提交任务");
//        }
//        BusinessTask businessTask=new BusinessTask();
//        businessTask.setTitle(title);
//        businessTask.setContent(content);
//        businessTask.setCreateTime(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
//        businessTask.setUserid(userId);
//        businessTask.setTypeid(type);
//        businessTask.setTaskLimit(limit);
//        businessTask.setWorkEndTime(work_end_time);
//        businessTask.setApplyEndTime(apply_end_time);
//
//        businessTask.setWorkerPrice(new BigDecimal(jiangLi));
//        businessTask.setNeedpeoplenum(Integer.valueOf(peopleNum));
//        businessTask.setStatus(3);//支付完成就是任务进行中吧
//
//        //创建支付订单
//        //实例化客户端
//        AlipayClient alipayClient = new DefaultAlipayClient(alipayUrl, alipayAppID, alipayPrivateKey, "json", "UTF-8", alipayPublicKey, alipaySignType);
//        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
//        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
//        //SDK已经封装掉了公共参数，这里只需要
//
//        // 传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
//        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
//        String tradeNo= DateUtils.format(new Date(),"yyyyMMddHHmmssSSS")+userId;
//        String name="心享-"+tradeNo;
//        model.setBody("心享-用户订单");
//        model.setSubject(name);
//        model.setOutTradeNo(tradeNo);
//        model.setTimeoutExpress(alipayTimeoutExpress);
//        model.setTotalAmount(String.valueOf(businessTask.getWorkerPrice().floatValue() * businessTask.getNeedpeoplenum()));
//        model.setProductCode(alipayProductCode);
//        request.setBizModel(model);
//        request.setNotifyUrl(alipayNotifyUrl);
//        try {
//            //这里和普通的接口调用不同，使用的是sdkExecute
//            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
//            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
//            //在真正生成订单的时候才创建任务
//            businessTask.setOrderid(tradeNo);
//            businessTask.setNeedtotalpay(new BigDecimal(businessTask.getWorkerPrice().doubleValue() * businessTask.getNeedpeoplenum().doubleValue()));
//            businessTask.setPayStatus(0);
//            businessTask.setAduitTime(2);
//            businessTaskService.createTask(businessTask);
//            return success(response.getBody());
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//            return fail(AppExecStatus.FAIL, "订单生成失败！");
//        }
    }

    @PostMapping("/asnyUserTaskTimeOut")
    //    1，我领取的任务超时
    //      1.1 待提交任务超时：用户待提交超过了任务截止时间(任务提交超时，用户可进行任务提交超时申诉)（任务完成超时，已结束）
    //          查找数据库中我的任务状态是1，且超过任务提交时间的将状态改成2
    //      1.2 商户审核超时完成(任务状态为已完成，用户总金额+任务奖励)(用户查询审核状态的时候，超时就设置任务状态为商户审核超时，或者商家查询任务状态的时候，超时就设置为申请超时)
    //          查找数据库中我的任务状态是3，且超过任务审核时间3天 将任务状态改成4，将用户任务状态改成已完成，并修改用户余额
    //      1.3 商户超时未审核申诉 (任务已完成，用户总金额+任务奖励)
    //          查找数据库中我的任务状态是7，且超过任务审核时间3天 将任务状态改成8，将用户任务状态改成已完成，并修改用户余额
    //      1.4 用户审核失败-可申诉状态
    public AppResult<String> asnyUserTaskTimeOut(@RequestHeader("userToken") String userToken) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        List<UserTask> timeoutList=userTaskService.asnyUserTaskTimeOut(userId);
        if(timeoutList!=null&&timeoutList.size()>0) {
            for (int i = 0; i < timeoutList.size(); i++) {
                //用户提交超时
                if (timeoutList.get(i).getUserTaskStatus()==1){
                    timeoutList.get(i).setUserTaskStatus(2);
                    userTaskService.updateSelective(timeoutList.get(i));

                }

                //商户审核超时--算作用户完成任务
                if (timeoutList.get(i).getUserTaskStatus()==3){
                    timeoutList.get(i).setUserTaskStatus(4);
                    userTaskService.updateSelective(timeoutList.get(i));
                    finishTask(timeoutList.get(i));

                }

                //用户申诉超时-我的任务结束
                if (timeoutList.get(i).getUserTaskStatus()==6){
                    timeoutList.get(i).setUserTaskStatus(11);
                    userTaskService.updateSelective(timeoutList.get(i));

                }
                //商户审核申诉超时--算作用户完成任务
                if (timeoutList.get(i).getUserTaskStatus()==7){
                    timeoutList.get(i).setUserTaskStatus(8);
                    userTaskService.updateSelective(timeoutList.get(i));
                    finishTask(timeoutList.get(i));
                }

            }
        }
        return success("数据同步完成");

    }


    public void finishTask(UserTask userTask){
        //第一次，或者第二次审核超时就算失败
        if(userTask.getUserTaskStatus()==4||userTask.getUserTaskStatus()==8){
            //减去商户的冻结金额
            //增加用户的冻结金额
            //添加历史记录表
            SysUser business=sysUserService.queryUser(userTask.getBusinessUserId());
            SysUser user=sysUserService.queryUser(userTask.getUserId());

            business.setLockMoney();

        }
    }
}
