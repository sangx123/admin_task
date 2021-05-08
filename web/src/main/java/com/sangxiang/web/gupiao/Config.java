package com.sangxiang.web.gupiao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Config {
    private static Gson gson;
    public static Gson getGson(){
        if(gson==null){
           gson=new Gson();
        }
        return gson;
    }
    //设置时间
    //默认显示当天的配置
    public static Calendar getNeedDay(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());
        //calendar.add(Calendar.DAY_OF_MONTH,-3);
        return calendar;
        //calendar.add(Calendar.DAY_OF_MONTH,-2);
    }
    public static Calendar getPreNeedDay(Calendar calendar) throws Exception {
        for (int i=0;i<10;i++){
            calendar.add(Calendar.DAY_OF_MONTH,-1);
            if(!checkHoliday(calendar))break;
        }
        return calendar;
    }

    //配置文件路径
    public static String  getFilePath(String path){
        return "/Users/sangxiang/Desktop/"+path+".txt";
    }

    public static String  getToadyPath(){
        String date= Utils.getStringFromDate(getNeedDay().getTime()).substring(0,8);
        String path= "/Users/sangxiang/Desktop/" +date+"_result.txt";
        //此处只是返回一个path给另一边
        return path;
    }

    //http://14.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112402802216838088085_1615796409797&pn=2&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615796409806
    //http://21.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112406733118140687235_1615866217485&pn=2&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615866217490
    /**
     * 配置今日下载地址参数
      * @param page
     * @return
     */
   public static String getTodayPage(int page){
        return  //"cb=jQuery112409274230641138408_1615532635543&pn="+page+"&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615532635547" ;
        //"cb=jQuery112402802216838088085_1615796409797&pn="+page+"&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615796409806";
        "cb=jQuery1124028909469068014504_1616055076180&pn="+page+"&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1616055076187\n";
   }
      //http://93.push2.eastmoney.com/api/qt/clist/get?cb=jQuery112409274230641138408_1615532635543&pn=2&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615532635547
      //http://48.push2.eastmoney.com/api/qt/clist/get?cb=jQuery11240010726273175511114_1615960806581&pn=2&pz=20&po=1&np=1&ut=bd1d9ddb04089700cf9c27f6f7426281&fltt=2&invt=2&fid=f3&fs=m:0+t:6,m:0+t:13,m:0+t:80,m:1+t:2,m:1+t:23&fields=f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f12,f13,f14,f15,f16,f17,f18,f20,f21,f23,f24,f25,f22,f11,f62,f128,f136,f115,f152&_=1615960806586
    /**
     * 配置今日下载地址
     * @return
     */

    public static String getTodayUrl(){
        return "http://59.push2.eastmoney.com/api/qt/clist/get";    }



    public static int addPageDate(String url, String param, List<Diff> list){
        int total=-1;
        String str= HttpRequest.sendGet(url,param);
        str=str.substring(str.indexOf("(")+1,str.length()-2);
        //System.out.println(str);
        //通过fastjson转换数组
        JSONObject rootObject = JSON.parseObject(str);
        JSONObject dataObject=rootObject.getJSONObject("data");
        JSONArray diffArray=dataObject.getJSONArray("diff");
        for (int i=0;i<diffArray.size();i++){
            Diff model=new Diff();
            String name=diffArray.getJSONObject(i).getString("f14");
            String code=diffArray.getJSONObject(i).getString("f12");
            float todayPrice=0;
            float maxPrice= 0;
            float minPrice= 0;
            float yesterDayPrice= 0;
            float startPrice= 0;
            try {
                todayPrice = diffArray.getJSONObject(i).getFloat("f2");
            }catch (Exception e){

            }

            try {
                maxPrice= diffArray.getJSONObject(i).getFloat("f15");
            }catch (Exception e){

            }
            try {
                minPrice=diffArray.getJSONObject(i).getFloat("f16");
            }catch (Exception e){

            }
            try {
                yesterDayPrice=diffArray.getJSONObject(i).getFloat("f18");
            }catch (Exception e){

            }
            try {
                startPrice=diffArray.getJSONObject(i).getFloat("f17");
            }catch (Exception e){

            }

            model.setName(name);
            model.setCode(code);
            model.setTodayPricre(todayPrice);
            model.setTodayMaxPrice(maxPrice);
            model.setTodayMinPrice(minPrice);
            model.setYesterDayPrice(yesterDayPrice);
            model.setStartPrice(startPrice);
            list.add(model);

        }
        total=dataObject.getInteger("total");
        return total;
    }


    /**
     *
     * <p>Title: checkHoliday </P>
     * <p>Description: TODO 验证日期是否是节假日</P>
     * @param calendar  传入需要验证的日期
     * @return
     * return boolean    返回类型  返回true是节假日，返回false不是节假日
     * throws
     * date 2014-11-24 上午10:13:07
     */


    public static boolean checkHoliday(Calendar calendar) throws Exception{
        if(holidayList.size()==0) {
            initHolidayList();
        }
        //判断日期是否是周六周日
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return true;
        }
        //判断日期是否是节假日
        for (Calendar ca : holidayList) {
            if(ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)&&
                    ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){
                return true;
            }
        }

        return false;
    }


    /**
     *
     * <p>Title: initHolidayList </P>
     * <p>Description: TODO  把所有节假日放入list，验证前要先执行这个方法</P>
     * return void    返回类型
     * throws
     * date 2014-11-24 上午10:11:35
     */


    public static  void initHolidayList() {
        ArrayList<String> date=new ArrayList<>();
        date.add("2021-4-3");
        date.add("2021-4-4");
        date.add("2021-4-5");
        date.add("2021-5-1");
        date.add("2021-5-2");
        date.add("2021-5-3");
        date.add("2021-5-4");
        date.add("2021-5-5");
        date.add("2021-6-12");
        date.add("2021-6-13");
        date.add("2021-6-14");
        date.add("2021-9-19");
        date.add("2021-9-20");
        date.add("2021-9-21");
        date.add("2021-10-1");
        date.add("2021-10-2");
        date.add("2021-10-3");
        date.add("2021-10-4");
        date.add("2021-10-5");
        date.add("2021-10-6");
        date.add("2021-10-7");

        for (String string : date) {

            String[] da = string.split("-");

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
            calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);// 月份比正常小1,0代表一月
            calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
            holidayList.add(calendar);
        }

    }


    public static List<Calendar> holidayList=new ArrayList<>();

}
