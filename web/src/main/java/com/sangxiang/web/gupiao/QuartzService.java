package com.sangxiang.web.gupiao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sangxiang.web.gupiao.Config;
import com.sangxiang.web.gupiao.Utils;
import com.sangxiang.web.gupiao.Diff;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.lang.reflect.Type;
import java.util.*;
@Slf4j
@Component
public class QuartzService {
    //    每分钟启动
    //@Scheduled(cron = "0 0/1 * * * ?")
    //目前设置1分钟实现一次
    @Scheduled(fixedDelay =1000*5)
    public void timerToNow(){
        //超过3点过1分之后就不执行
        Calendar endDay= Config.getNeedDay();;
        String todayName= Utils.getStringFromDate(endDay.getTime());
        Date now=new Date();
//        try {
//            run();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if(!now.after(Utils.getDate(todayName.substring(0,8)+" 15:20:00"))&&!now.before(Utils.getDate(todayName.substring(0,8)+" 09:10:00"))){
            log.info("时间在9点过10分和15：20");
            try {
                run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            log.info("时间不在9点过10分和15：20");
        }

    }


    /***
     * 形态学2天的算法
     * @throws Exception
     */
    void run() throws Exception {
        log.info("开始执行");
        Calendar endDay= Config.getNeedDay();;
        String todayName= Utils.getStringFromDate(endDay.getTime());
        if(new Date().after(Utils.getDate(todayName.substring(0,8)+" 15:00:00"))){
            todayName=todayName.substring(0,8);
        }
        String todayPath=Config.getFilePath(todayName);
        String yesterdayPath=getPredayPath(endDay);
        String yesterdayPath2=getPredayPath(endDay);
        String yesterdayPath3=getPredayPath(endDay);
        String yesterdayPath4=getPredayPath(endDay);
        String yesterdayPath5=getPredayPath(endDay);
        String yesterdayPath6=getPredayPath(endDay);
        String yesterdayPath7=getPredayPath(endDay);
        File file = new File(todayPath);

        HashMap<String, Diff> yesterdayHashMap1=new HashMap<>();
        HashMap<String,Diff> yesterdayHashMap2=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap3=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap4=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap5=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap6=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap7=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap8=new HashMap<String,Diff>();
        HashMap<String,Diff> yesterdayHashMap9=new HashMap<String,Diff>();
        HashMap<String,Diff> starMap=new HashMap<String,Diff>();
        HashMap<String,Diff> endMap=new HashMap<String,Diff>();
        List<Diff> todayList=new ArrayList();
        Gson gson=new Gson();
        int todayPage=1;
        String todayUrl=Config.getTodayUrl();
        String todayParam=Config.getTodayPage(todayPage);
        //今天的处理和其他几天的处理不一样
        // 判断文件是否存在
        if (!file.exists()) {
            //写入数据到今天的数据列表
            int todayTotalPage=Config.addPageDate(todayUrl,todayParam,todayList);
            Utils.createFile(todayPath);

            for(int i=1;20*i<todayTotalPage;i++){
                todayPage++;
                todayParam=Config.getTodayPage(todayPage);
                Config.addPageDate(todayUrl,todayParam,todayList);
                Utils.writeFileOverlay(todayPath,gson.toJson(todayList));
            }
        }else{
            System.out.println("读取今日数据"+todayPath);
            //读取今日数据
            String toadyStr= Utils.readFile(todayPath);
            Type type = new TypeToken<ArrayList<Diff>>() {
            }.getType();
            todayList=gson.fromJson(toadyStr,type);
        }


        yesterdayHashMap1=getDayDate(yesterdayPath);
        //yesterdayHashMap2=getDayDate(yesterdayPath2);
//            yesterdayHashMap3=getDayDate(yesterdayPath3);
//            yesterdayHashMap4=getDayDate(yesterdayPath4);
//            yesterdayHashMap5=getDayDate(yesterdayPath5);
//            yesterdayHashMap6=getDayDate(yesterdayPath6);
//            yesterdayHashMap7=getDayDate(yesterdayPath7);
        calculateXingtai2(todayList,yesterdayHashMap1);
//            starMap=yesterdayHashMap1;
//            endMap=yesterdayHashMap7;
        //caculatexingtai2Test(todayList,yesterdayHashMap1,yesterdayHashMap2);
        // calculateXingtai5(todayList,yesterdayHashMap1,yesterdayHashMap2,yesterdayHashMap3,yesterdayHashMap4,yesterdayHashMap5);
        //caculatexingtai3(todayList,yesterdayHashMap1,yesterdayHashMap2,yesterdayHashMap3);
        //calculateMACD(todayList,endMap,starMap);
        log.info("结束执行");


    }

    private void calculateMACD2(List<Diff> list, HashMap<String, Diff> starMap, HashMap<String, Diff> endMap) {

        int countUp6Down=0;
        int countUp6Up=0;
        int countDown6Down=0;
        int countDown6Up=0;
        for(int i=0;i<list.size();i++){
            if(starMap.containsKey(list.get(i).getCode())&&endMap.containsKey(list.get(i).getCode())){
                if(Math.abs(endMap.get(list.get(i).getCode()).getYesterDayPrice()-starMap.get(list.get(i).getCode()).getTodayPricre())<starMap.get(list.get(i).getCode()).getTodayPricre()*0.05f){
                    //5日线向上
                    if(list.get(i).getTodayPricre()>list.get(i).getYesterDayPrice()){
                        countUp6Up++;
                    }else {
                        countUp6Down++;
                    }
                }

//                else {
//                    if(list.get(i).getTodayPricre()>list.get(i).getYesterDayPrice()){
//                        countDown6Up++;
//                    }else {
//                        countDown6Down++;
//                    }
//                }

            }
        }
        System.out.println("Up-up"+countUp6Up);
        System.out.println("Up-down"+countUp6Down);
        System.out.println("Up-baifenbi"+countUp6Down/((countUp6Up+countUp6Down)*1.0f));

//
//        System.out.println("down-up"+countDown6Up);
//        System.out.println("down-down"+countDown6Down);
//        System.out.println("down-baifenbi"+countDown6Down/((countDown6Up+countDown6Down)*1.0f));
    }

    public  HashMap<String,Diff> getDayDate(String path){

        //读取前天数据
        HashMap<String,Diff> map=new HashMap<>();
        System.out.println("读取的数据"+path);
        String content=Utils.readFile(path);
        Type type = new TypeToken<ArrayList<Diff>>() {
        }.getType();
        ArrayList<Diff> list=Config.getGson().fromJson(content,type);
        for (int i=0;i<list.size();i++){
            map.put(list.get(i).getCode(),list.get(i));
        }
        return  map;
    }

    /***
     * 第一天高开，第二天低开
     */
    public void calculateXingtai2(List<Diff> todayList, HashMap<String,Diff> yesterdayHashMap1){
        ArrayList<Diff> result=new ArrayList<>();
        for(int i=0;i<todayList.size();i++){
            if(!todayList.get(i).getName().contains("ST")) {
                Diff todayModel = todayList.get(i);
                //今天是上涨的
                if (yesterdayHashMap1.containsKey(todayModel.getCode())) {
                    Diff yesterdayModel1 = yesterdayHashMap1.get(todayModel.getCode());
                    //昨天是上涨的行情
                    //昨天高开
                    if (yesterdayModel1.getTodayPricre() > yesterdayModel1.getStartPrice() && yesterdayModel1.getYesterDayPrice() < yesterdayModel1.getStartPrice()) {
                        //今天低开(低于昨天的起始价),且上涨
                        if (todayModel.getStartPrice() < yesterdayModel1.getStartPrice()&&todayModel.getTodayPricre() > todayModel.getStartPrice()) {
                            //System.out.println(todayModel.getName() + todayModel.getCode());
                            //昨日上涨
                            //if (yesterdayModel1.getTodayPricre() > todayModel.getStartPrice()) {
                            // System.out.println(todayModel.getName() + todayModel.getCode());
                            //今天高点不超过前天低点
                            if(todayModel.getTodayPricre()<yesterdayModel1.getStartPrice()) {
                                //今日分上涨的股票
                                result.add(todayModel);
                                //System.out.println("================》：" + todayModel.getName() + todayModel.getCode());
                            }
//                            else {
//                                    System.out.println(todayModel.getName() + todayModel.getCode());
//                            }

                            //}
                        }

                    }
                }
            }

        }
        //将对象保存在文件中
       String str= Config.getGson().toJson(result);

        File file = new File(Config.getToadyPath());
        if (!file.exists()){
            Utils.createFile(Config.getToadyPath());
        }
        //将结果数据覆盖写入
        Utils.writeFileOverlay(Config.getToadyPath(),str);
    }

    public void caculatexingtai2Test(List<Diff> todayList, HashMap<String,Diff> yesterdayHashMap1,HashMap<String,Diff> yesterdayHashMap2){
        // ============================================================================
        int  count3=0;
        int count2=0;

        int  count5=0;
        int count6=0;
        for(int i=0;i<todayList.size();i++){
            if(!todayList.get(i).getName().contains("ST")) {
                Diff todayModel = todayList.get(i);
                //今天是上涨的
                if (yesterdayHashMap2.containsKey(todayModel.getCode()) && yesterdayHashMap1.containsKey(todayModel.getCode())) {
                    Diff yesterdayModel2 = yesterdayHashMap2.get(todayModel.getCode());
                    Diff yesterdayModel1 = yesterdayHashMap1.get(todayModel.getCode());
                    //前天是上涨的行情
                    //前天高开
                    if (yesterdayModel2.getTodayPricre() > yesterdayModel2.getStartPrice() && yesterdayModel2.getYesterDayPrice() < yesterdayModel2.getStartPrice()) {
                        //昨日低开(低于昨天的起始价)
                        //昨日上涨，昨日低开的概率
                        if (yesterdayModel1.getStartPrice() < yesterdayModel2.getStartPrice()&&yesterdayModel1.getTodayPricre()>yesterdayModel1.getStartPrice()) {
                            //System.out.println(todayModel.getName() + todayModel.getCode());
                            //昨日上涨
                            //if (yesterdayModel1.getTodayP2ricre() > todayModel.getStartPrice()) {
                            // System.out.println(todayModel.getName() + todayModel.getCode());
                            //昨日高点不超过前天低点
                            //不加最高点的配置
                            if(yesterdayModel1.getTodayPricre()<yesterdayModel2.getStartPrice()) {
                                if (todayModel.getTodayPricre() > todayModel.getYesterDayPrice()) {
                                    //今日分上涨的股票
                                    count3++;
                                    System.out.println("================》：" + todayModel.getName() + todayModel.getCode());
                                } else {
                                    count2++;
                                    //今日分下跌的股票
                                    System.out.println(todayModel.getName() + todayModel.getCode());
                                }
                            }else{
                                if (todayModel.getTodayPricre() > todayModel.getStartPrice()) {
                                    //今日分上涨的股票
                                    //count5++;
                                    //System.out.println("================》：" + todayModel.getName() + todayModel.getCode());
                                } else {
                                    //count6++;
                                    //今日分下跌的股票
                                    //System.out.println(todayModel.getName() + todayModel.getCode());
                                }
                            }

                            //}
                        }

                    }
                }
            }

        }
        //低于昨日价格的
        System.out.println("上涨股有"+count3);
        System.out.println("下跌股有"+count2);
        //高于昨日价格
        System.out.println("上涨股有"+count5);
        System.out.println("下跌股有"+count6);
    }


    public void caculatexingtai3Test(List<Diff> todayList, HashMap<String,Diff> yesterdayHashMap1,HashMap<String,Diff> yesterdayHashMap2,HashMap<String,Diff> yesterdayHashMap3){
        // ============================================================================
        int  count3=0;
        int count2=0;

        int  count5=0;
        int count6=0;
        for(int i=0;i<todayList.size();i++){
            if(!todayList.get(i).getName().contains("ST")) {
                Diff todayModel = todayList.get(i);
                //今天是上涨的
                if (yesterdayHashMap2.containsKey(todayModel.getCode()) && yesterdayHashMap1.containsKey(todayModel.getCode())) {
                    Diff yesterdayModel2 = yesterdayHashMap2.get(todayModel.getCode());
                    Diff yesterdayModel1 = yesterdayHashMap1.get(todayModel.getCode());
                    //前天是上涨的行情
                    //前天高开
                    if (yesterdayModel2.getTodayPricre() > yesterdayModel2.getStartPrice() && yesterdayModel2.getYesterDayPrice() < yesterdayModel2.getStartPrice()) {
                        //昨日低开(低于昨天的起始价)
                        //昨日上涨，昨日低开的概率
                        if (yesterdayModel1.getStartPrice() < yesterdayModel2.getStartPrice()&&yesterdayModel1.getTodayPricre()>yesterdayModel1.getStartPrice()) {
                            //System.out.println(todayModel.getName() + todayModel.getCode());
                            //昨日上涨
                            //if (yesterdayModel1.getTodayP2ricre() > todayModel.getStartPrice()) {
                            // System.out.println(todayModel.getName() + todayModel.getCode());
                            //昨日高点不超过前天低点
                            //不加最高点的配置
                            if(yesterdayModel1.getTodayPricre()<yesterdayModel2.getStartPrice()) {
                                if (todayModel.getTodayPricre() > todayModel.getStartPrice()) {
                                    //今日分上涨的股票
                                    count3++;
                                    System.out.println("================》：" + todayModel.getName() + todayModel.getCode());
                                } else {
                                    count2++;
                                    //今日分下跌的股票
                                    System.out.println(todayModel.getName() + todayModel.getCode());
                                }
                            }else{
                                if (todayModel.getTodayPricre() > todayModel.getStartPrice()) {
                                    //今日分上涨的股票
                                    count5++;
                                    System.out.println("================》：" + todayModel.getName() + todayModel.getCode());
                                } else {
                                    count6++;
                                    //今日分下跌的股票
                                    System.out.println(todayModel.getName() + todayModel.getCode());
                                }
                            }

                            //}
                        }

                    }
                }
            }

        }
        //低于昨日价格的
        System.out.println("上涨股有"+count3);
        System.out.println("下跌股有"+count2);
        //高于昨日价格
        System.out.println("上涨股有"+count5);
        System.out.println("下跌股有"+count6);
    }




    /***
     * 第一天高开，第二天低开
     */
    public void calculateXingtai5(List<Diff> list, HashMap<String,Diff> map1, HashMap<String,Diff> map2, HashMap<String,Diff> map3, HashMap<String,Diff> map4, HashMap<String,Diff> map5){

        int countUp6Down=0;
        int countUp6Up=0;
        int countDown6Down=0;
        int countDown6Up=0;
        for(int i=0;i<list.size();i++){
            if(map1.containsKey(list.get(i).getCode())&&map2.containsKey(list.get(i).getCode())&&map3.containsKey(list.get(i).getCode())&&map4.containsKey(list.get(i).getCode())){

                if(isDown(map1.get(list.get(i).getCode()))&&isDown(map2.get(list.get(i).getCode()))&&isDown(map3.get(list.get(i).getCode()))&&isDown(map4.get(list.get(i).getCode()))&&isDown(map5.get(list.get(i).getCode()))){
                    if(map5.get(list.get(i).getCode()).getYesterDayPrice()>map1.get(list.get(i).getCode()).getTodayPricre()*1.05||map5.get(list.get(i).getCode()).getYesterDayPrice()*0.95>map1.get(list.get(i).getCode()).getTodayPricre())
                        //5日线向上
                        if(list.get(i).getTodayPricre()>list.get(i).getYesterDayPrice()){
                            countUp6Up++;
                            System.out.println("================》：" + list.get(i).getName() + list.get(i).getCode());
                        }else {
                            countUp6Down++;
                        }

                }
//                else {
//                    if(list.get(i).getTodayPricre()>list.get(i).getYesterDayPrice()){
//                        countDown6Up++;
//                    }else {
//                        countDown6Down++;
//                    }
//                }

            }
        }
        System.out.println("Up-up"+countUp6Up);
        System.out.println("Up-down"+countUp6Down);
        System.out.println("Up-baifenbi"+countUp6Down/((countUp6Up+countUp6Down)*1.0f));


        System.out.println("down-up"+countDown6Up);
        System.out.println("down-down"+countDown6Down);
        System.out.println("down-baifenbi"+countDown6Down/((countDown6Up+countDown6Down)*1.0f));
    }

    public String getPredayPath(Calendar preDay) throws Exception {
        preDay= Config.getPreNeedDay(preDay);
        if(Utils.getStringFromDate( preDay.getTime()).contains("20210310")){
            preDay=Config.getPreNeedDay(preDay);
        }
        String preDayName=Utils.getStringFromDate(preDay.getTime()).substring(0,8);
        String preDayPath=Config.getFilePath(preDayName);
        return preDayPath;
    }


    public boolean isDown(Diff model){
        if(model.getStartPrice()>model.getTodayPricre()){
            return true;
        }else {
            return false;
        }

    }
}
