package com.cinkle.jdbcT;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//单例模式
public class LocationAlgrithm {
//    原始数据，由LabelBean提供
    String information;
//    分类号，从原始数据中提取
    String typeNumber;
//    排列号，从原始数据中提取
    String arrayNumber;
//    是否找到相同的分类号
    boolean founded;

    private static LocationAlgrithm locationAlgrithm;
    public static LocationAlgrithm getLocationAlgrithm(){
        return locationAlgrithm;
    }

    public LocationAlgrithm(String informDetail){
        this.information=informDetail;
        locationAlgrithm  = this;
    }
    public void setInformation(String informDetail){
        this.information=informDetail;
    }
//    检查原始数据的合法性
    public boolean checkLegitimacy(){
        if(!splitInfoDetail()&&!isTwoZone())
            return false;
        return true;
    }
//    分离图书信息中的分类号和索书号
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
    public void getResult(){
        if(!checkLegitimacy()){
            JOptionPane.showMessageDialog(null,"没有找到相关3D位置信息");
            return;
        }
        String result = getProperType();
        if(founded){
            findProperCell(result);
        }else{
            findFirstCell(result);
        }
    }
    public void findProperCell(String str){
        jdbcTest jdbc =jdbcTest.getJdbc();
        LinkedList<jdbcTest.CellBean> list = jdbc.getList();
        jdbcTest.CellBean result=null;
        jdbcTest.CellBean minDifference=null;
        int deff=Integer.MAX_VALUE;
        int middle=convertArrayNumber(arrayNumber);
        for(jdbcTest.CellBean bean : list){
            String first=getTypeAtRecord(bean.first);
            String firstArray=getArrayAtRecord(bean.first);
            String second=getTypeAtRecord(bean.second);
            String secondArray=getArrayAtRecord(bean.second);
            if(str.equals(first)||str.equals(second)){
                if(str.equals(first)&&str.equals(second)){
                    int front=convertArrayNumber(firstArray);
                    int back=convertArrayNumber(secondArray);
                    if(front<=middle&&back>=middle){
                        result=bean;
                        break;
                    }else{
                        int temp=calculateDifference(front,back);
                        if(temp<deff){
                            minDifference=bean;
                            deff=temp;
                        }
                    }
                }else if(str.equals(first)&&!str.equals(second)){
                    int front=convertArrayNumber(firstArray);
                    if(front<=middle){
                        minDifference=bean;
                        deff=0;
                    }
                }else{
                    int back=convertArrayNumber(secondArray);
                    if(back>=middle){
                        minDifference=bean;
                        deff=0;
                    }
                }
            }
        }
        String message="";
        if(result!=null){
            message=transformResult(result);
        }else{
            message=transformResult(minDifference);
        }
        startUpUnity(message);
    }
//    计算两个排列号的差
    public int calculateDifference(int p,int q){
        return Math.abs(p-q);
    }
    public void findFirstCell(String str){
        jdbcTest jdbc =jdbcTest.getJdbc();
        LinkedList<jdbcTest.CellBean> list = jdbc.getList();
        jdbcTest.CellBean result=null;
        for(jdbcTest.CellBean bean : list){
            String first=getTypeAtRecord(bean.first);
            String second=getTypeAtRecord(bean.second);
            if(str.equals(first)||str.equals(second)){
                if(result==null){
                    result=bean;
                }else{
                    result=comparePosition(result,bean);
                }
            }
        }
        String message = transformResult(result);
        startUpUnity(message);
    }
    private jdbcTest.CellBean comparePosition(jdbcTest.CellBean bean1,jdbcTest.CellBean bean2){
        jdbcTest.CellBean result=null;
        if(bean1.id<bean2.id){
            result=bean1;
        }else if(bean2.id<bean1.id){
            result=bean2;
        }else{
            if(bean1.side>bean2.side){
                result=bean1;
            }else if(bean2.side>bean1.side){
                result=bean2;
            }else{
                if(bean1.line<bean2.line){
                    result=bean1;
                }else if(bean2.line<bean1.line){
                    result=bean2;
                }else{
                    if(bean1.row<bean2.row){
                        result=bean1;
                    }else if(bean2.row<bean1.row){
                        result=bean2;
                    }else{
                        result=bean1;
                    }
                }
            }
        }
        return result;
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
//    获取最合适的类型,并设置founded
    public String getProperType(){
        jdbcTest jdbc = jdbcTest.getJdbc();
        HashSet<String> set=jdbc.getSet();
        String result="";
        if(set.contains(typeNumber)){
            founded=true;
            result = typeNumber;
        }else{
            founded=false;
//            求最大字符串子序列的长度
            int max=0;
//            这里可能非常耗时间
            for(String str : set){
                int temp = longestSequence(str,typeNumber);
                if(temp>max){
                    result = str;
                }
            }
        }
        return result;
    }
//    将排列号转化为整型，用于比较
    public int convertArrayNumber(String str){
        int num=0;
        if(str.contains("-")){
            int t = str.indexOf("-");
            if(str.contains(".")){
                int m = str.indexOf(".");
                String p;
                if(m>t){
                    p=str.substring(0,t)+str.substring(m);
                    num=toInt(p);
                }else{
                    p=str.substring(0,t);
                    num=toInt(p);
                }
            }else{
                String s = str.substring(0,t);
                num=toInt(s);
            }
        }else{
            num=toInt(str);
        }
        return num;
    }
//    转化为整型比较
    private int toInt(String str){
        int num=0;
        String s;
        if(str.contains(".")){
            int t = str.indexOf('.');
            String str1 = str.substring(0,t);
            String str2 = str.substring(t+1);
            str1=fill4(str1);
            str2=fill2(str2);
            s=str1+str2;
        }else{
            s =fill4(str)+"00";
        }
        num=Integer.valueOf(s);
        return num;
    }
    private String fill2(String str){
        switch(str.length()){
            case 1:str="0"+str;break;
        }
        return str;
    }
    private String fill4(String str){
        switch(str.length()){
            case 2:str=str+"00";break;
            case 3:str=str+"0";break;
            case 5:str=str.substring(0,4);break;
        }
        return str;
    }
//    比较排列号
    public boolean compare(int a,int b){
        return a>=b;
    }
//    将CellBean转换为适合传递的格式，比如字符串
    private String transformResult(jdbcTest.CellBean bean){
        return bean.id+"#"+bean.side+"#"+bean.row+"#"+bean.line;
    }
//    启动Unity应用程序,并传送数据
    private void startUpUnity(String result){
        System.out.println(result);
    }
//    求两个字符串的最长字符串子序列的长度。
    public int longestSequence(String str1,String str2){
        if(str1==null||str2==null||str1.equals("")||str2.equals(""))
            return 0;
        int length1=str1.length();
        int length2=str2.length();
        int[] lastLine=new int[length1];
        int[] currentLine=new int[length1];
        char at =' ';
        for(int i=0;i<length2;i++){
            at=str2.charAt(i);
            for(int j=0;j<length1;j++){
                if(at == str1.charAt(j)){
                    for(int k=j;k<length1;k++){
                        currentLine[k]=lastLine[k]+1;
                    }
                    break;
                }
                else{
                    currentLine[j]=lastLine[j];
                }
            }
            int[] temp =lastLine;
            lastLine = currentLine;
            currentLine = temp;
            Arrays.fill(currentLine,0);
        }
        return lastLine[length1-1];
    }
    public static void main(String[] args) {
        LocationAlgrithm algrithm = new LocationAlgrithm("132654");
        System.out.println(algrithm.longestSequence("10203040506","919293945969"));
        System.out.println(algrithm.convertArrayNumber("324"));
    }
}
