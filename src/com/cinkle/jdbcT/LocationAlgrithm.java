package com.cinkle.jdbcT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//单例模式
public class LocationAlgrithm {

    String information;

    String typeNumber ;
    String arrayNumber;
    public LocationAlgrithm(String informDetail){
        this.information=informDetail;
    }
//    检查输入字符串的合法性
    public boolean checkLegitimacy(){
        if(!splitInfoDetail()&&!isTwoZone())
            return false;
        return true;
    }
//    分离图书信息中的分类号和索书号m
    private boolean splitInfoDetail(){
        String pattern = "([A-Z0-9,.,-]+)/([0-9,.,]+)";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(information);
        if(matcher.find()){
            typeNumber = matcher.group(1);
            arrayNumber = matcher.group(2);
        }
        else{
            return false;
        }
        return true;
    }
//    检查图书是否处于中社科二区
    private boolean isTwoZone(){
        if((!typeNumber.equals("")) && typeNumber.charAt(0) == 'I')
            return true;
        return false;
    }
//    获取此图书最合适的CellBean
    private void getResult(){

    }
//    获取记录中索书号的分类号
    public String getTypeAtRecord(String str){
        int index =str.indexOf("/");
        return str.substring(0,index);
    }
//    获取记录中索书号的排列号
    public String getArrayAtRecord(String str){
        int index =str.indexOf("/");
        return str.substring(index+1);
    }
    public void convertArrayNumber(String str){

    }
//    将CellBean转换为适合传递的格式，比如字符串
    private String transformResult(jdbcTest.CellBean bean){
        return bean.id+"#"+bean.side+"#"+bean.row+"#"+bean.line;
    }
//    启动Unity应用程序
    private void startUpUnity(){

    }

    public static void main(String[] args) {

    }
}
