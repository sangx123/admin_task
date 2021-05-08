package com.sangxiang.web.gupiao;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    //帮助类
    public static String default_data_format="yyyyMMddHHmmss";
    public static String getStringFromDate(Date date){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(default_data_format);
            return sdf.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 日期格式字符串转换成时间戳
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static Date getDate(String date_str){
        String format="yyyyMMdd HH:mm:ss";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date_str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 功能描述:
     * <创建文件>
     *
     * @param filePath 1
     * @return void
     * @author zhoulipu
     * @date 2019/8/2 16:23
     */
    public static void createFile(String filePath) {
        try {
            File file = new File(filePath);
            // 判断文件是否存在
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能描述:
     * <覆盖写入>
     *
     * @param filePath 1
     * @param Content  2
     * @return void
     * @author zhoulipu
     * @date 2019/8/2 16:23
     */
    public static void writeFileOverlay(String filePath, String Content) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(filePath);
            pw.write(Content);
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pw != null) {
                pw.flush();
                pw.close();
            }
        }
    }



    /**
     * 功能描述:
     * <读取文件>
     *
     * @param filePath 1
     * @return void
     * @author zhoulipu
     * @date 2019/8/2 16:24
     */
    public static String readFile(String filePath) {
        InputStreamReader in = null;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            File file = new File(filePath);
            in = new InputStreamReader(new FileInputStream(file), "utf8");
            br = new BufferedReader(in);
            String s;
            // 逐行读取
            while ((s = br.readLine()) != null) {
                //System.out.println(s);
                sb.append(s);
            }

            br.close();
            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }

    }
}




