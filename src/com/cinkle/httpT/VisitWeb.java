package com.cinkle.httpT;

import com.cinkle.swingT.LabelBean;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.*;
//使用单例模式
public class VisitWeb {
    private String bookname;
    private static VisitWeb visitweb;
    private VisitWeb(String name){
        this.bookname=name;
    }
    private VisitWeb(){ }

    public static VisitWeb getVisitweb(){
        if(visitweb == null){
            visitweb = new VisitWeb();
        }
        return visitweb;
    }
    public void setBookName(String name){
        this.bookname = name;
    }

    //调用此方法前必须初始化bookname，否则将会抛出异常
    public ArrayList<LabelBean> getInfomation(){
        StringBuilder builder = getAllCode();
        String pattern = "<img class=\"weui_media_appmsg_thumb\" src=\"(.+?)jpg\".+?"+
                "<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.*?)</p>"+
                ".+?<li class=\"weui_media_info_meta\">(.*?)</li>";
        Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = p.matcher(builder);

        ArrayList<LabelBean> informaion = new ArrayList<>(151);
        int index=0;                                     //游标
        while(matcher.find(index)){
            LabelBean bean = new LabelBean();
            bean.setImagePath(matcher.group(1)+"jpg");
            bean.setBookName(matcher.group(2));
            bean.setInfoDatail(matcher.group(3));
            bean.setCollection(matcher.group(4));
            informaion.add(bean);
            index =matcher.end();
        }

        return informaion;

    }
    private StringBuilder getAllCode(){
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
    }
    //改变字符编码
    private String changeUTF8ToURL(){
        String res="";
        try{
            res = URLEncoder.encode(bookname,"UTF-8");
        }catch(UnsupportedEncodingException err){
            System.out.println("中文转URL出现错误");
        }
        return res;
    }
    //获取网页源码
    public static StringBuilder getSourceCode(String str_url){
        StringBuilder result= new StringBuilder();
        try{
            URL url = new URL(str_url);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream in =connection.getInputStream();
            InputStreamReader inreader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inreader);
            String s="";
            while((s=reader.readLine())!=null)
                result.append(s+"\n");
        }catch(MalformedURLException err){
            JOptionPane.showMessageDialog(null,"网络连接错误！");
        }catch(IOException epp){
            JOptionPane.showMessageDialog(null,"网络连接错误！");
        }
        return result;
    }
    //获取源码页数
    private int getPageNum(){
        StringBuilder result;
        int res=-1;

        String str=changeUTF8ToURL();
        str="http://opac.ouc.edu.cn:8081/m/opac/search.action?q="+str+"&t=any";
        result =getSourceCode(str);

        String pattern ="<div class=\"center\">\\d+/(\\d+)</div>";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(result);

        if(matcher.find()){
            res = Integer.parseInt(matcher.group(1));
        }
        else
            res = 1;

        return res;
    }
}
