package com.sangxiang.util;

import com.xiaoleilu.hutool.json.JSONUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @author fujg
 * Created by fujg on 2017/2/21.
 */
@Slf4j
public class WebUtil {

    private WebUtil(){}

    /**
     * 判断是否为ajax请求
     * @param request
     * @return
     */
    public static boolean isAjaxRequest(HttpServletRequest request){
        boolean flag = false;
        String requestType = request.getHeader("X-Requested-With");
        if("XMLHttpRequest".equalsIgnoreCase(requestType)){
            flag = true;
        }
        return flag;
    }

    /**
     * 响应json数据
     * @param response
     * @param resultMap
     * @param status
     */
    public static void writeJson(HttpServletResponse response, Map<String, Object> resultMap, int status){
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(status);
            @Cleanup PrintWriter writer = response.getWriter();
            writer.print(JSONUtil.parseObj(resultMap));
        } catch (IOException e) {
            log.error("响应json数据失败，失败信息:{}", e);
        }

    }

    /**
     * 响应text数据
     * @param response
     * @param resultMap
     * @param status
     */
    public static void writeText(HttpServletResponse response, Map<String, Object> resultMap, int status){
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(status);
            @Cleanup PrintWriter writer = response.getWriter();
            writer.print(JSONUtil.toJsonStr(resultMap));
        } catch (IOException e) {
            log.error("响应text数据失败，失败信息:{}", e);
        }
    }
}
