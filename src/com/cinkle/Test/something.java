package com.cinkle.Test;

import com.cinkle.swingT.SwingConsole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;
import java.util.List;

public class something {
    static class RandomLocation{
//        消息字符串
        private String location;
//        随机数生成对象
        private  Random random;
//        构造函数
        public RandomLocation(){
            random=new Random();
        }
//        生成位置信息
        public void setLocation(){
            int id=random.nextInt(22)+1;
            int side=random.nextInt(2);
            int line=0;
            if(id==5||id==11||id==17){
                line =random.nextInt(13)+1;
            }else{
                line =random.nextInt(17)+1;
            }
            int row=0;
            if(id==1||id==2){
                row=random.nextInt(4)+1;
            }else{
                row=random.nextInt(6)+1;
            }
            location = id+"#"+side+"#"+line+"#"+row;
        }
        public String getLocation(){
            return location;
        }
//        启动进程
        public void startUp(){
            ProcessBuilder processBuilder=new ProcessBuilder();
            processBuilder.command("E:\\PlanA.exe");
            try{
                Process process =processBuilder.start();
                OutputStream outputStream=process.getOutputStream();

                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                bufferedWriter.write(location);
                bufferedWriter.flush();
                System.out.println("此次操作已经完成，进程ID是"+process.pid());
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
//    程序入口
    public static void main(String[] args) {
        RandomLocation lt=new RandomLocation();
        lt.setLocation();
        lt.startUp();
    }
}
