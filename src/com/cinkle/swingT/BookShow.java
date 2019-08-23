package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookShow extends JPanel{
    private LinkedList<LabelBean> list;
    private JPanel display=new JPanel();
    boolean newHot=false;
    String name;

    JLabel back = new JLabel();
    JLabel home = new JLabel();
    JPanel backhome = new JPanel();
    public BookShow(boolean newhot,String n){
        newHot = newhot;
        name =n;
        getInformation();
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
        initial();
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
                    frame.getContentPane().add(new NewHotSortUI(newHot));
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
    private void initial(){
        GridLayout grid = new GridLayout(0,2);
        grid.setVgap(20);
        display.setLayout(grid);
        for(int i=0;i<list.size();i++){
            list.get(i).initial();
            display.add(list.get(i));
        }
        JScrollPane scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(1000,620));
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll);
    }
    private void getInformation(){
        String newbook ="<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.+?)</p>.+?"+
                "<li class=\"weui_media_info_meta\">(.+?)</li>";
        String hotbook ="<h4 class=\"weui_media_title\">(.+?)</h4>.*?"+
                "<p class=\"weui_media_desc\">(.+?)/(.+?)</p>";
        if(newHot){
            list =getInfo(hotbook);
        }
        else{
            list =getInfo(newbook);
        }
    }
    private LinkedList<LabelBean> getInfo(String pa){
        String pattern = pa;
        StringBuilder builder = getAllCode();
        Pattern p = Pattern.compile(pattern,Pattern.DOTALL);
        Matcher matcher = p.matcher(builder);

        LinkedList<LabelBean> informaion = new LinkedList<>();
        int index=0;                   //游标
        while(matcher.find(index)){
            LabelBean bean = new LabelBean();
            bean.setBookName(matcher.group(1));
            bean.setInfoDatail(matcher.group(2));
            bean.setCollection(matcher.group(3));
            informaion.add(bean);
            index =matcher.end();
        }
        if(index==0)
            System.out.println("对象没有匹配");
        return informaion;
    }
    private StringBuilder getAllCode(){
        StringBuilder result=new StringBuilder();
        int pagenum = getPageNum();
        //限制页数最多为15页
        pagenum = pagenum>15 ? 15 : pagenum;

        String str = getURLstr();
        for(int i=1;i <= pagenum;i++){
            String strurl = str+"&page="+i;
            result.append(VisitWeb.getSourceCode(strurl));
        }
        return result;
    }
    private String getURLstr(){
        String url="";
        char first = name.charAt(9);
        if(newHot){
            url = "http://opac.ouc.edu.cn:8081/m/info/top_lend.action?clsNo=" + first;
        }
        else{
            url = "http://opac.ouc.edu.cn:8081/m/info/newbook.action?clsNo=" + first;
        }
        return url;
    }
    private int getPageNum(){
        String str = getURLstr();
        StringBuilder code = VisitWeb.getSourceCode(str);

        String pattern ="<div class=\"center\">\\d+/(\\d+)</div>";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(code);

        int res =0;
        if(matcher.find()){
            res = Integer.parseInt(matcher.group(1));
        }
        else
            res = 1;

        return res;
    }
}
