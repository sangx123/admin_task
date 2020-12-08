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
        //先获取用户领取的任务并同步
        //sql查询数据的好处，不用查询已结束的
        //查询用户所有未完成的任务
        userAsycTask(userId);

        //作为商家一个个任务去结束
        List<BusinessTask> list= businessTaskService.getShanhuTask(userId,3);

        for (int i = 0; i <list.size() ; i++) {
            businessAsycTask(list.get(i));
        }


        return success("接收任务数据同步完成");

    }



    //超时任务，增加用户的金币，并添加用户金币变化表
    public void finishTask(UserTask userTask){
        BusinessTask businessTask=businessTaskService.queryById(userTask.getBusinessTaskId());
        finishTask(userTask,businessTask);
    }
    //超时任务，增加用户的金币，并添加用户金币变化表
    public void finishTask(UserTask userTask,BusinessTask businessTask){
        //第一次，或者第二次审核超时就算失败
        //此处只是用来判断用户的获取金额是否已经完成-重新获取下最新的数据
        UserTask userTaskMoney=userTaskService.queryUserTaskById(userTask.getId().intValue());
        if(userTask.getUserTaskStatus()==4||userTask.getUserTaskStatus()==8&&!userTaskMoney.getFinished()){
            //修改商户任务已支付金额
            businessTask.setHaspayedmoney(businessTask.getWorkerPrice().add(businessTask.getHaspayedmoney()));
            businessTaskService.updateSelective(businessTask);
            //修改用户任务金额并更新,并设置用户任务已完成
            userTask.setUserGetMoney(businessTask.getWorkerPrice());
            userTask.setFinished(true);
            userTaskService.updateSelective(userTask);
            //修改用户金额并更新
            SysUser user=sysUserService.queryUser(userTask.getUserId());
            user.setMoney(user.getMoney()+userTask.getUserGetMoney().floatValue());
            sysUserService.updateSelective(user);
        }
    }
    //商人发布的任务列表的同步
    public void businessAsycTask(BusinessTask businessTask){

        //      1.1 待提交任务超时：用户待提交超过了任务截止时间(任务提交超时，用户可进行任务提交超时申诉)（任务完成超时，已结束）
        //          查找数据库中我的任务状态是1，且超过任务提交时间的将状态改成2
        //      1.2 商户审核超时完成(任务状态为已完成，用户总金额+任务奖励)(用户查询审核状态的时候，超时就设置任务状态为商户审核超时，或者商家查询任务状态的时候，超时就设置为申请超时)
        //          查找数据库中我的任务状态是3，且超过任务审核时间3天 将任务状态改成4，将用户任务状态改成已完成，并修改用户余额
        //      1.3 商户超时未审核申诉 (任务已完成，用户总金额+任务奖励)
        //          查找数据库中我的任务状态是7，且超过任务审核时间3天 将任务状态改成8，将用户任务状态改成已完成，并修改用户余额
        //      1.4 用户审核失败-可申诉状态

        List<UserTask> userTaskList=userTaskService.queryUserTaskListByBusinessTaskId(businessTask.getId());
        if(userTaskList==null||userTaskList.size()==0)return;

        for (int i = 0; i <userTaskList.size() ; i++) {
            UserTask item=userTaskList.get(i);
            //用户提交超时
            if(item.getUserTaskStatus()==1){
                //如果任务截止时间在当前时间之前的话
                //提交超时，任务结束，状态任务状态2，只更改状态
                if(item.getUserFirstSubmitTaskTimeout().before(new Date())){
                    item.setUserTaskStatus(2);
                    item.setFinished(true);
                    userTaskService.updateSelective(item);
                }

            }

            //商家审核超时,更改卖家和商家信息
            //审核超时，任务状态4，任务结束，更改用户金币_扣除任务余额
            if (item.getUserTaskStatus()==3){

                //如果用户第一次提交的时间时间加上3天，在今天之前表示审核超时
                if(DateUtils.addDateDays(item.getUserFirstSubmitTaskTime(),3).before(new Date())){
                    item.setUserTaskStatus(4);
                    userTaskService.updateSelective(item);
                    finishTask(item, businessTask);
                }
            }
            //此处给审核失败的用户3天的时间去申诉
            //用户申诉超时-任务状态11，任务结束，只更改状态
            if (item.getUserTaskStatus()==6){
                //如果用户审核时间加上3天在当前3天的时间的话标示已完成
                if(DateUtils.addDateDays(item.getBusinessAuditFirstTime(),3).before(new Date())){
                    item.setUserTaskStatus(11);
                    item.setFinished(true);
                    userTaskService.updateSelective(item);
                }

            }

            //商户第二次审核超时
            //商户二次审核超时-任务结束，更改用户金币_扣除任务余额
            if (userTaskList.get(i).getUserTaskStatus()==7){
                if(DateUtils.addDateDays(item.getUserSecondSubmitTaskTime(),3).before(new Date())){
                    item.setUserTaskStatus(8);
                    userTaskService.updateSelective(item);
                    finishTask(item,businessTask);
                }
            }
        }
    }

    public void userAsycTask(int  userId){
        List<UserTask> userTaskList=userTaskService.asnyUserTaskTimeOut(userId);
        //      1.1 待提交任务超时：用户待提交超过了任务截止时间(任务提交超时，用户可进行任务提交超时申诉)（任务完成超时，已结束）
        //          查找数据库中我的任务状态是1，且超过任务提交时间的将状态改成2
        //      1.2 商户审核超时完成(任务状态为已完成，用户总金额+任务奖励)(用户查询审核状态的时候，超时就设置任务状态为商户审核超时，或者商家查询任务状态的时候，超时就设置为申请超时)
        //          查找数据库中我的任务状态是3，且超过任务审核时间3天 将任务状态改成4，将用户任务状态改成已完成，并修改用户余额
        //      1.3 商户超时未审核申诉 (任务已完成，用户总金额+任务奖励)
        //          查找数据库中我的任务状态是7，且超过任务审核时间3天 将任务状态改成8，将用户任务状态改成已完成，并修改用户余额
        //      1.4 用户审核失败-可申诉状态

        if(userTaskList==null||userTaskList.size()==0)return;

        for (int i = 0; i <userTaskList.size() ; i++) {
            UserTask item=userTaskList.get(i);
            //用户提交超时
            if(item.getUserTaskStatus()==1){
                //如果任务截止时间在当前时间之前的话
                //提交超时，任务结束，状态任务状态2，只更改状态
                if(item.getUserFirstSubmitTaskTimeout().before(new Date())){
                    item.setUserTaskStatus(2);
                    item.setFinished(true);
                    userTaskService.updateSelective(item);
                }

            }

            //商家审核超时,更改卖家和商家信息
            //审核超时，任务状态4，任务结束，更改用户金币_扣除任务余额
            if (item.getUserTaskStatus()==3){

                //如果用户第一次提交的时间时间加上3天，在今天之前表示审核超时
                if(DateUtils.addDateDays(item.getUserFirstSubmitTaskTime(),3).before(new Date())){
                    item.setUserTaskStatus(4);
                    userTaskService.updateSelective(item);
                    finishTask(item);
                }
            }
            //此处给审核失败的用户3天的时间去申诉
            //用户申诉超时-任务状态11，任务结束，只更改状态
            if (item.getUserTaskStatus()==6){
                //如果用户审核时间加上3天在当前3天的时间的话标示已完成
                if(DateUtils.addDateDays(item.getBusinessAuditFirstTime(),3).before(new Date())){
                    item.setUserTaskStatus(11);
                    item.setFinished(true);
                    userTaskService.updateSelective(item);
                }

            }

            //商户第二次审核超时
            //商户二次审核超时-任务结束，更改用户金币_扣除任务余额
            if (userTaskList.get(i).getUserTaskStatus()==7){
                if(DateUtils.addDateDays(item.getUserSecondSubmitTaskTime(),3).before(new Date())){
                    item.setUserTaskStatus(8);
                    userTaskService.updateSelective(item);
                    finishTask(item);
                }
            }
        }
    }
}
