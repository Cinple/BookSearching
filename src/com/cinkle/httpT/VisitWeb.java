package com.cinkle.httpT;

import com.cinkle.swingT.LabelBean;
import com.cinkle.swingT.Search;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.regex.*;

public class VisitWeb {
    public static boolean getInformation(String bookName, int pageNum, Search search){
        String utf8Name=changeUTF8ToURL(bookName);
        String url = getURL(utf8Name,pageNum);
        StringBuilder builder = getSourceCode(url);
        if(builder==null)
            return false;
        String pattern = "<img class=\"weui_media_appmsg_thumb\" src=\"(.+?)jpg\".+?"+
                "<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.*?)</p>"+
                ".+?<li class=\"weui_media_info_meta\">(.*?)</li>";
        Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = p.matcher(builder);

        JPanel panel =search.getDisplay();
        int index=0;                                     //游标
        while(matcher.find(index)){
            LabelBean bean = new LabelBean();
            bean.setImagePath(matcher.group(1)+"jpg");
            bean.setBookName(matcher.group(2));
            bean.setInfoDatail(matcher.group(3));
            bean.setCollection(matcher.group(4));
            search.addBean(bean);
            index =matcher.end();
        }
        if(panel.getComponentCount()==0)
            return false;
        return true;
    }
    public static Boolean isEmpty(String bookName){
        String utf8Name=changeUTF8ToURL(bookName);
        String url = getURL(utf8Name,1);
        StringBuilder builder = getSourceCode(url);
        if(builder==null)
            return true;
//        String pattern = "<img class=\"weui_media_appmsg_thumb\" src=\"(.+?)jpg\".+?"+
//                "<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
//                "<p class=\"weui_media_desc\">(.*?)</p>"+
//                ".+?<li class=\"weui_media_info_meta\">(.*?)</li>";
        String pattern ="<h4 class=\"weui_media_title\">(.+?)</h4>";
        Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = p.matcher(builder);
        return !matcher.find();
    }
/*    private StringBuilder getAllCode(){
        StringBuilder result=new StringBuilder();
        int pagenum = getPageNum();
        //限制页数最多为15页
        pagenum = pagenum>15 ? 15 : pagenum;

        String str = changeUTF8ToURL();
        String net = "http://opac.ouc.edu.cn:8081/m/opac/search.action?q=" + str + "&t=any&page=";
        for(int i=1;i<=pagenum;i++){
            String strurl =net+i;
            result.append(getSourceCode(strurl));
        }
        return result;
    }*/
    //改变字符编码
    public static String changeUTF8ToURL(String bookName){
        String res="";
        try{
            res = URLEncoder.encode(bookName,"UTF-8");
        }catch(UnsupportedEncodingException err){
            System.out.println("中文转URL出现错误");
        }
        return res;
    }
    public static String getURL(String utf8Name,int pageNum){
        return "http://opac.ouc.edu.cn:8081/m/opac/search.action?q=" + utf8Name + "&t=any&page="+pageNum;
    }
    //获取网页源码
    public static StringBuilder getSourceCode(String str_url){
        StringBuilder result= new StringBuilder();
        try{
            URL url = new URL(str_url);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream in =connection.getInputStream();
            InputStreamReader inreader = new InputStreamReader(in,"utf8");
            BufferedReader reader = new BufferedReader(inreader);
            String s="";
            while((s=reader.readLine())!=null)
                result.append(s+"\n");
        }/*catch(MalformedURLException err){
            JOptionPane.showMessageDialog(null,"1.网络连接错误！");
            return null;
        }*/catch(IOException epp){
            JOptionPane.showMessageDialog(null,"2.网络连接错误！");
            return null;
        }
        return result;
    }
    //获取源码页数n
    public static int getPageNum(String url){
        StringBuilder result;
        int res=-1;
        result =getSourceCode(url);

        String pattern ="<div class=\"center\">\\d+/(\\d+)</div>";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(result);

        if(matcher.find()){
            res = Integer.parseInt(matcher.group(1));
            res = res > 15 ? 15 : res;
        }
        else
            res = 1;

        return res;
    }
}
