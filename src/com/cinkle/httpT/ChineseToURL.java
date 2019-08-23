package com.cinkle.httpT;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ChineseToURL {
    public static void main(String[] args) {
        try{
            String name ="追风筝的人";
            String cont = URLEncoder.encode(name,"UTF-8");
            System.out.println(cont);
        }catch(UnsupportedEncodingException e){

        }

    }
}
