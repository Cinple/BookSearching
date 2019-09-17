package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
* 这个类专门用于新书通报与热门借阅中图书信息的展示
* */
public class BookShow extends JPanel{

    private JPanel display=new JPanel();
    boolean newHot;
    String name;

    JLabel back = new JLabel();
    JLabel home = new JLabel();
    JPanel backhome = new JPanel();

    CountDownLatch latch;
    ExecutorService service;
    static class BeanTask implements Runnable{
        CountDownLatch lat;
        BookShow show;
        String url;
        BeanTask(CountDownLatch lat,BookShow show,String url){
            this.lat = lat;
            this.show = show;
            this.url = url;
        }
        @Override
        public void run(){
            show.getInformation(show,url);
            lat.countDown();
        }
    }
    public BookShow(boolean newhot, String n, ExecutorService s){
        newHot = newhot;
        name =n;
        service=s;

        Search.initialDisplay(display);
        String url = getURLstr();
        int pageNum =VisitWeb.getPageNum(url);
        latch = new CountDownLatch(pageNum);
        BeanTask[] task = new BeanTask[pageNum];
        for(int i=0;i<=pageNum-1;i++){
            String URL = getURL(url,i+1);
            task[i]=new BeanTask(latch,this,URL);
            service.submit(task[i]);
        }

        try{
            latch.await();
        }catch(InterruptedException p){
            System.out.println("error int addActionListener in BookShow class");
        }

        FlowLayout flowLayout =new FlowLayout();
        setLayout(flowLayout);
        setPreferredSize(new Dimension(1200,800));

        back.setIcon(new ImageIcon("res/back.jpg"));
        home.setIcon(new ImageIcon("res/home.jpg"));
        home.setText("    ");
        home.setHorizontalTextPosition(JLabel.LEFT);
        addMouseActToBH();

        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        backhome.setLayout(flow);
        backhome.add(back);
        backhome.add(home);
        backhome.setPreferredSize(new Dimension(1150,50));

        add(backhome);
        add(Search.addScrollForDisplay(display,1000,620));
    }
    public void addBean(LabelBean bean){
        bean.initial();
        synchronized (display){
            display.add(bean);
        }
    }
    public JPanel getDisplay(){
        return display;
    }
    public String getURL(String url,int page){
        return url+"&page="+page;
    }
    private void addMouseActToBH(){
        class action implements MouseListener {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame frame = UIT.getJframe();
                frame.getContentPane().removeAll();
                if(e.getSource() == home){
                    JPanel panel = ((UIT)frame).getMainJpanel();
                    frame.getContentPane().add(panel);
                }
                else if(e.getSource() == back){
                    frame.getContentPane().add(new NewHotSortUI(newHot,service));
                }
                frame.getContentPane().validate();
                frame.getContentPane().repaint();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
        home.addMouseListener(new action());
        back.addMouseListener(new action());
    }
    private void getInformation(BookShow show,String url){
        String newbook ="<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.+?)</p>.+?"+
                "<li class=\"weui_media_info_meta\">(.+?)</li>";
        String hotbook ="<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.+?)/(.+?)</p>";
        if(newHot){
            getInfo(hotbook,show,url);
        }
        else{
            getInfo(newbook,show,url);
        }
    }
    private void getInfo(String pa,BookShow show,String url){
        String pattern = pa;
        StringBuilder builder = VisitWeb.getSourceCode(url);
        Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = p.matcher(builder);

        int index=0;                   //游标
        while(matcher.find(index)){
            LabelBean bean = new LabelBean();
            bean.setBookName(matcher.group(1));
            bean.setInfoDatail(matcher.group(2));
            bean.setCollection(matcher.group(3));
            show.addBean(bean);
            index =matcher.end();
        }
    }
    private String getURLstr(){
        String urll="";
        char first = name.charAt(9);
        if(newHot){
            urll = "http://opac.ouc.edu.cn:8081/m/info/top_lend.action?clsNo=" + first;
        }
        else{
            urll = "http://opac.ouc.edu.cn:8081/m/info/newbook.action?clsNo=" + first;
        }
        return urll;
    }
}
