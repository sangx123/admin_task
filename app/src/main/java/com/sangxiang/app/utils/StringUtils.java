package com.sangxiang.app.utils;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sendtion on 2016/6/24.
 */
public class StringUtils {


    /**
     * @param targetStr 要处理的字符串
     * @description 切割字符串，将文本和img标签碎片化，如"ab<img>cd"转换为"ab"、"<img>"、"cd"
     */
    public static List<String> cutStringByImgTag(String targetStr) {
        List<String> splitTextList = new ArrayList<String>();
        Pattern pattern = Pattern.compile("<img.*?src=\\\"(.*?)\\\".*?>");
        Matcher matcher = pattern.matcher(targetStr);
        int lastIndex = 0;
        while (matcher.find()) {
            if (matcher.start() > lastIndex) {
                splitTextList.add(targetStr.substring(lastIndex, matcher.start()));
            }
            splitTextList.add(targetStr.substring(matcher.start(), matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex != targetStr.length()) {
            splitTextList.add(targetStr.substring(lastIndex, targetStr.length()));
        }
        return splitTextList;
    }

    /**
     * 获取多个目标字符串
     */
    public static List<String> cutStringByLineTag(String input) {
        String regex=Pattern.quote("<line>")+"(.*?)"+Pattern.quote("</line>");
        List<String> result = new LinkedList<String>();
        Pattern pat = Pattern.compile(regex, Pattern.DOTALL);
        Matcher mat = pat.matcher(input);
        while (mat.find()) {
            result.add(mat.group(1));
        }
        return result;
    }

    /**
     * 获取img标签中的src值
     * @param content
     * @return
     */
    public static String getImgSrc(String content){
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return str_src;
    }

    /**
     * 从html文本中提取图片地址，或者文本内容
     * @param html 传入html文本
     * @param isGetImage true获取图片，false获取文本
     * @return
     */
    public static ArrayList<String> getTextFromHtml(String html, boolean isGetImage){
        ArrayList<String> imageList = new ArrayList<>();
        ArrayList<String> textList = new ArrayList<>();
        //根据img标签分割出图片和字符串
        List<String> list = cutStringByImgTag(html);
        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i);
            if (text.contains("<img") && text.contains("src=")) {
                //从img标签中获取图片地址
                String imagePath = getImgSrc(text);
                imageList.add(imagePath);
            } else {
                textList.add(text);
            }
        }
        //判断是获取图片还是文本
        if (isGetImage) {
            return imageList;
        } else {
            return textList;
        }
    }
    //将字符串中的src替换成自己设定的src
    public static String replceImgSrc(String content, List<String>  list) {
        if(list==null||list.size()<0)
            return content;
        int count=0;
        String str_src = null;
        //目前img标签标示有3种表达式
        //<img alt="" src="1.jpg"/>   <img alt="" src="1.jpg"></img>     <img alt="" src="1.jpg">
        //开始匹配content中的<img />标签
        Pattern p_img = Pattern.compile("<(img|IMG)(.*?)(/>|></img>|>)");
        Matcher m_img = p_img.matcher(content);
        boolean result_img = m_img.find();
        if (result_img) {
            while (result_img) {
                //获取到匹配的<img />标签中的内容
                String str_img = m_img.group(2);

                //开始匹配<img />标签中的src
                Pattern p_src = Pattern.compile("(src|SRC)=(\"|\')(.*?)(\"|\')");
                Matcher m_src = p_src.matcher(str_img);
                if (m_src.find()) {
                    str_src = m_src.group(3);
                    if(count<list.size()) {
                        content = content.replace(str_src, list.get(count));
                        count++;
                    }
                }
                //结束匹配<img />标签中的src

                //匹配content中是否存在下一个<img />标签，有则继续以上步骤匹配<img />标签中的src
                result_img = m_img.find();
            }
        }
        return content;

    }

}
