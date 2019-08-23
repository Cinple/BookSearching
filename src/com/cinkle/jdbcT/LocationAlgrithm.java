package com.cinkle.jdbcT;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocationAlgrithm {
    String type ="";
    String arrayNumber="";
    public LocationAlgrithm(String infodetail){

    }
    private boolean splitInfoDetail(String info){
        String pattern = "([A-Z0-9,.,-]+)/([0-9,.,]+)";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(info);
        if(matcher.find()){
            type = matcher.group(1);
            arrayNumber = matcher.group(2);
        }
        else{
            return false;
        }
        return true;
    }
    private boolean isTwoZone(){
        if((!type.equals("")) && type.charAt(0) == 'I')
            return true;
        return false;
    }
    private void getResult(){

    }
    private void transformResult(){

    }
    private void queryDatabase(){

    }
    private void startUpUnity(){

    }
}
