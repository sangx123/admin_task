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
import com.sangxiang.app.controller.login.LoginController;
import com.sangxiang.app.sdk.token.UserTokenManager;
import com.sangxiang.app.utils.AppResult;
import com.sangxiang.app.utils.StringUtils;
import com.sangxiang.base.rest.ApiResult;
import com.sangxiang.dao.model.*;
import com.sangxiang.dao.service.*;
import com.sangxiang.model.ApplyTaskParam;
import com.sangxiang.model.Login.HomeTaskParam;
import com.sangxiang.model.Task.TaskItem;
import com.sangxiang.model.Task.TaskParam;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import static com.alipay.api.AlipayConstants.CHARSET;

@RestController
@RequestMapping("/api/task")
public class TaskController extends AppBaseController {

    private static final Logger logger = Logger.getLogger(TaskController.class.getName());
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
    /**
     * 创建任务
     * @param files
     * @param content
     * @param title
     * @param jiangLi
     * @param peopleNum
     * @return
     */
    @PostMapping("/createTask")
    public AppResult<String> createTask(@RequestHeader("userToken") String userToken,
                                        @RequestParam(value = "imageList[]") MultipartFile files[],
                                        String content,//内容
                                        String title ,//标题
                                        String jiangLi,//奖励
                                        String peopleNum,//任务人数
                                        int type,//任务类型,
                                        String apply_end_time,//任务申请截止时间
                                        String work_end_time,//提交任务截止时间
                                        String limit

    ) {
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
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
                return  fail(AppExecStatus.FAIL,"图片上传失败，请重新提交任务");
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

        //将string转为model

        List<TaskItem> list = new Gson().fromJson(content, new TypeToken<List<TaskItem>>(){}.getType());
        //content=StringUtils.replceImgSrc(content,imageUrlList);
        int count=0;
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getAttributes()!=null&&list.get(i).getAttributes().getEmbed()!=null&&list.get(i).getAttributes().getEmbed().getType()!=null&&list.get(i).getAttributes().getEmbed().getType().equals("image")){
                list.get(i).getAttributes().getEmbed().setSource(imageUrlList.get(count++));
                //判断下一个索引是否超出如果没有超出去掉\n
                //暂时不能去掉\n否则报错
                //                if(i+1<list.size()){
                //                    TaskItem nextItem=list.get(i+1);
                //                    nextItem.setInsert(nextItem.getInsert().substring(1));
                //                }
            }
        }
        //过滤空白项会报错，暂时不全部过滤
        for (int i = 0; i <list.size() ; i++) {
            if(list.get(i).getAttributes()==null&&list.get(i).getInsert().isEmpty()){
                  list.remove(i);
            }
        }
        content=new Gson().toJson(list);
        BusinessTask businessTask=new BusinessTask();
        businessTask.setTitle(title);
        businessTask.setContent(content);
        businessTask.setCreateTime(DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        businessTask.setUserid(userId);
        businessTask.setTypeid(type);
        businessTask.setTaskLimit(limit);
        businessTask.setWorkEndTime(work_end_time);
        businessTask.setApplyEndTime(apply_end_time);

        businessTask.setWorkerPrice(new BigDecimal(jiangLi));
        businessTask.setNeedpeoplenum(Integer.valueOf(peopleNum));
        businessTask.setStatus(3);//支付完成就是任务进行中吧

        //创建支付订单
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient(alipayUrl, alipayAppID, alipayPrivateKey, "json", "UTF-8", alipayPublicKey, alipaySignType);
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要

        // 传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        String tradeNo= DateUtils.format(new Date(),"yyyyMMddHHmmssSSS")+userId;
        String name="心享-"+tradeNo;
        model.setBody("心享-用户订单");
        model.setSubject(name);
        model.setOutTradeNo(tradeNo);
        model.setTimeoutExpress(alipayTimeoutExpress);
        model.setTotalAmount(String.valueOf(businessTask.getWorkerPrice().floatValue() * businessTask.getNeedpeoplenum()));
        model.setProductCode(alipayProductCode);
        request.setBizModel(model);
        request.setNotifyUrl(alipayNotifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            //在真正生成订单的时候才创建任务
            businessTask.setOrderid(tradeNo);
            businessTask.setNeedtotalpay(new BigDecimal(businessTask.getWorkerPrice().doubleValue() * businessTask.getNeedpeoplenum().doubleValue()));
            businessTask.setPayStatus(0);
            businessTask.setAduitTime(2);
            businessTaskService.createTask(businessTask);
            return success(response.getBody());
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return fail(AppExecStatus.FAIL, "订单生成失败！");
        }
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


    @PostMapping(value = "/getHomeTaskList")
    @ApiOperation(value="任务大厅任务")
    public AppResult<PageInfo<BusinessTask>> getHomeTaskList(@RequestBody HomeTaskParam param){
        PageInfo<BusinessTask> pageInfo= businessTaskService.findPage(param.getPageNumber(),param.getPageSize(),param.getStatus());
        return success(pageInfo);
    }


    @PostMapping(value = "/getHomeBusinessTaskList")
    @ApiOperation(value="获取大厅任务列表")
    //分页，
    //任务状态，进行中的任务状态3，目前暂时手动操作为0
    //
    public AppResult<PageInfo<BusinessTask>> getHomeBusinessTaskList(@RequestHeader("userToken") String userToken,@RequestBody HomeTaskParam param){
        //int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        PageInfo<BusinessTask> pageInfo= businessTaskService.geHomeBusinessTaskPageList(param.getPageNumber(),param.getPageSize(),param.getStatus(),param.getType());
        return success(pageInfo);
    }


    @PostMapping(value = "/getMyPublishTaskList")
    @ApiOperation(value="我发布的任务列表")
    public AppResult<PageInfo<BusinessTask>> getMyPublishTaskList(@RequestHeader("userToken") String userToken,@RequestBody HomeTaskParam param){
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        PageInfo<BusinessTask> pageInfo= businessTaskService.findAllUserPublishTaskList(param.getPageNumber(),param.getPageSize(),userId,param.getStatus());
        return success(pageInfo);
    }

    @PostMapping(value = "/applyTask")
    @ApiOperation(value="申请任务")
    //先判断任务是否超过截止时间，
    //没有超过的话在判断任务是否被关闭
    //判断已申请通过人数是否超过任务名额，
    //申请任务总人数，不能超过任务名额30人
    //不能申请自己发布的任务
    //不能申请已经申请的任务
    //参数为BusinessTaskId
    public synchronized AppResult applyTask(@RequestHeader("userToken") String userToken,@RequestBody ApplyTaskParam param){
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        BusinessTask task=  businessTaskService.queryById(param.getTaskid());
        List<UserTask> userTaskList=userTaskService.queryUserTaskListByBusinessTaskId(task.getId());


        int applyedSum=0;
        boolean userHasApplyed=false;
        if(userTaskList!=null) {
            for (int i = 0; i < userTaskList.size(); i++) {
                if(userTaskList.get(i).getUserTaskStatus()>0){
                    applyedSum++;
                }
                if(userTaskList.get(i).getUserId()==userId){
                    userHasApplyed=true;
                    break;
                }
            }
        }
        if(userHasApplyed){
            return fail(AppExecStatus.FAIL,"该任务已申请，不能重复申请");
        }
        if(task.getUserid()==userId){
            return fail(AppExecStatus.FAIL,"自己不能申请自己发布的任务!");
        }
        if(task.getStatus()!=3){
            return fail(AppExecStatus.FAIL,"任务未开始，或者已结束");
        }
        if(applyedSum>task.getNeedpeoplenum()){
            return fail(AppExecStatus.FAIL,"任务名额已满");
        }
        if(userTaskList.size()>task.getNeedpeoplenum()+30){
            return fail(AppExecStatus.FAIL,"单个任务申请人数已超过任务名额30人，不能再申请");
        }
        userTaskService.createUserTask(task,userId);
        return success("任务申请成功，请等待审核！");
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

//    /**
//     * 获取当前时间 年月日时分秒毫秒
//     **/
//    public static String currentDate() {
//        String result = "";
//        try {
//            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//            Date data = new Date();
//            result = dfs.format(data);
//        } catch (Exception e) {
//        }
//        return result;
//    }


    @RequestMapping(value = "/getNotifyUrlInfo",method = RequestMethod.POST)
    public void alipayNotifyUrlInfo(HttpServletRequest request, HttpServletResponse response){
        logger.info("收到支付回调！");
        //接收参数进行校验
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //赋值
        AliNotify model=new AliNotify();
        if(params.containsKey("app_id")) {
            model.setAppId(params.get("app_id"));
        }
        if(params.containsKey("notify_id")){
            model.setNotifyId(params.get("notify_id"));
        }
        if(params.containsKey("notify_time")){
            model.setNotifyTime(params.get("notify_time"));
        }
        if(params.containsKey("notify_type")){
            model.setNotifyType(params.get("notify_type"));
        }
        if(params.containsKey("charset")){
            model.setCharset(params.get("charset"));
        }
        if(params.containsKey("version")){
            model.setVersion(params.get("version"));
        }

        if(params.containsKey("sign_type")){
            model.setSignType(params.get("sign_type"));
        }

        if(params.containsKey("sign")){
            model.setSign(params.get("sign"));
        }
        if(params.containsKey("trade_no")){
            model.setTradeNo(params.get("trade_no"));
        }
        if(params.containsKey("out_trade_no")){
            model.setOutTradeNo(params.get("out_trade_no"));
        }

        if(params.containsKey("out_biz_no")){
            model.setOutBizNo(params.get("out_biz_no"));
        }

        if(params.containsKey("buyer_id")){
            model.setBuyerId(params.get("buyer_id"));
        }

        if(params.containsKey("buyer_logon_id")){
            model.setBuyerLogonId(params.get("buyer_logon_id"));
        }

        if(params.containsKey("seller_id")){
            model.setSellerId(params.get("seller_id"));
        }

        if(params.containsKey("seller_email")){
            model.setSellerEmail(params.get("seller_email"));
        }

        if(params.containsKey("trade_status")){
            model.setTradeStatus(params.get("trade_status"));
        }

        if(params.containsKey("total_amount")){
            model.setTotalAmount(params.get("total_amount"));
        }
        if(params.containsKey("receipt_amount")){
            model.setReceiptAmount(params.get("receipt_amount"));
        }

        if(params.containsKey("invoice_amount")){
            model.setInvoiceAmount(params.get("invoice_amount"));
        }

        if(params.containsKey("buyer_pay_amount")){
            model.setBuyerPayAmount(params.get("buyer_pay_amount"));
        }

        if(params.containsKey("point_amount")){
            model.setPointAmount(params.get("point_amount"));
        }

        if(params.containsKey("refund_fee")){
            model.setRefundFee(params.get("refund_fee"));
        }

        if(params.containsKey("subject")){
            model.setSubject(params.get("subject"));
        }

        if(params.containsKey("body")){
            model.setBody(params.get("body"));
        }

        if(params.containsKey("gmt_create")){
            model.setGmtCreate(params.get("gmt_create"));
        }
        if(params.containsKey("gmt_payment")){
            model.setGmtPayment(params.get("gmt_payment"));
        }

        if(params.containsKey("gmt_refund")){
            model.setGmtRefund(params.get("gmt_refund"));
        }

        if(params.containsKey("fund_bill_list")){
            model.setFundBillList(params.get("fund_bill_list"));
        }

        if(params.containsKey("passback_params")){
            model.setPassbackParams(params.get("passback_params"));
        }

        if(params.containsKey("voucher_detail_list")){
            model.setVoucherDetailList(params.get("voucher_detail_list"));
        }
        //先插入数据
        aliNotifyService.insertAliNotify(model);
        logger.info("parameters is [parameters=]"+params);
        String appId = request.getParameter("app_id");//appid
        String merchantOrderNo = request.getParameter("out_trade_no");//商户订单号
//        String orderId = cashMapper.getCashByMerchantOrderNo(merchantOrderNo).getOrderId();
//        if (orderId == null) {
//             log.error("orderId is null");
//             ReturnData.fail(ReturnCode.SERVER_EXCEPTION);
//         }
//         log.info("orderId: {}", orderId);
         String payState = request.getParameter("trade_status");//交易状态
         String encodeOrderNum = null;
         //cashLogMapper.add(request.getParameter("out_trade_no"), "NOTIFY", JSON.toJSONString(parameters), new Date());
         try {
            encodeOrderNum = URLDecoder.decode(request.getParameter("passback_params"), "UTF-8");
            log.info("encodeOrderNum is [encodeOrderNum={}]", encodeOrderNum);
            boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8",alipaySignType);
            log.info("signVerified is [signVerified={}]", signVerified);
            if (signVerified) { //通过验证
                log.info("payState: {}", payState);
                 if (payState.equals("TRADE_SUCCESS")) {
                            //判断订单号与插入的订单号是否一样
//                            if (merchantOrderNo.equals(encodeOrderNum) == false || appid.equals(appId) == false) {
//                                    log.info("vali failure");
//                                  //cashMapper.update(merchantOrderNo, 4);
//
//                                 response.getOutputStream().print("failure");
//                                 return;
//                             }
                         //cashMapper.update(merchantOrderNo, 3);
                         //orderMapper.afterPay(orderId);
                         response.getOutputStream().print("success");
                         return;
                 } else if (payState.equals("TRADE_CLOSED")) { //交易关闭
                     //cashMapper.update(merchantOrderNo, 7);
                 } else {
                     //cashMapper.update(merchantOrderNo, 4);
                     response.getOutputStream().print("failure");
                     return;
                }
            } else {
             //签名校验失败更状态
             //cashMapper.update(merchantOrderNo, 4);
                response.getOutputStream().print("failure");
                return;
             }
             log.info("encodeOrderNum is  [encodeOrderNum={}]", encodeOrderNum);
             //cashMapper.update(merchantOrderNo, 4);
             response.getOutputStream().print("failure");
            return;
        } catch (AlipayApiException e) {
            log.error(e.getErrMsg());
            throw new RuntimeException("调用支付宝接口发生异常");
         } catch (UnsupportedEncodingException e) {
         log.error(e.getMessage());
         throw new RuntimeException("URLDecoderf发生异常");
        } catch (IOException e) {
             log.error(e.getMessage());
             throw new RuntimeException("IO发生异常");
        }
    }


    @PostMapping(value = "/getTaskMainType")
    @ApiOperation(value="获取任务一级分类")
    public AppResult<List<TaskMainTyle>> getTaskMainType(@RequestHeader("userToken") String userToken){
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        List<TaskMainTyle> list= taskMainTypeService.getAllTaskMainType();
        return success(list);
    }


    @PostMapping(value = "/getTaskById")
    @ApiOperation(value="获取任务详情")
    //任务详情返回，任务标题，任务创建人，创建时间，所需任务人数，任务价格，申请截止日期，第一次任务提交截止日期
    //任务查看人数，正在申请中的人数，【已进行中的任务人数】，已完成任务的人数，申诉中的任务人数，已完成的任务
    public AppResult<BusinessTask> getTaskById(@RequestHeader("userToken") String userToken, @RequestBody TaskParam taskParam){
        int userId = UserTokenManager.getInstance().getUserIdFromToken(userToken).intValue();
        BusinessTask model= businessTaskService.queryById(taskParam.getId());
        List<UserTask> list=userTaskService.queryUserTaskListByBusinessTaskId(model.getId());
        model.setUserTaskList(list);
        return success(model);
    }

}
