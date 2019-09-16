package com.cinkle.swingT;

import com.cinkle.httpT.DLimage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.net.URL;

public class LabelBean extends JPanel{

    private String imagepath="";
    private String bookname;
    private String infodetail;
    private String collect;
    private String imagename;

    public void setImagePath(String path){
        this.imagepath = path;
    }
    public void setBookName(String name){
        this.bookname = name;
    }
    public void setInfoDatail(String detail){
        this.infodetail = detail;
    }
    public void setCollection(String col){
        this.collect = col;
    }

    JLabel image = new JLabel();

    JLabel name = new JLabel();
    JLabel info = new JLabel();
    JLabel collection = new JLabel();
    JLabel pos = new JLabel("3D位置");

    JPanel jp = new JPanel();
    public LabelBean(){
    }
    public void initial(){
        //image.setIcon(new ImageIcon("res/default.jpg"));
        setImage();
        checkBookName();
        name.setText(bookname);
        checkInfoDetail();
        info.setText(infodetail);
        collection.setText(collect);

        GridLayout gl = new GridLayout(4,1);
        gl.setVgap(10);
        jp.setLayout(gl);
        pos.setForeground(Color.BLUE);
        pos.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("位置被单击了");
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
                pos.setText("<html><u>3D位置</u><html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                pos.setText("3D位置");
            }
        });
        jp.add(name);
        jp.add(info);
        jp.add(collection);
        jp.add(pos);

        FlowLayout flow = new FlowLayout();
        //将标签左对齐
        flow.setAlignment(FlowLayout.LEFT);
        setLayout(flow);
        add(image);
        add(jp);
    }
//为LabelBea设置图片
    public void setImage(){
        if(imagepath.equals("") || imagepath.equals("/m/mopac/inner/images/no-book.jpg"))
            image.setIcon(new ImageIcon("res/default.jpg"));
        else{
            try{
                ImageIcon icon = new ImageIcon(new URL(imagepath));
                ImageIcon con = DLimage.changeSize(icon.getImage());
                image.setIcon(con);
            }catch(MalformedURLException e){
                image.setIcon(new ImageIcon("res/default.jpg"));
                System.out.println("图片加载错误");
            }
        }
    }
//限制图书名称长度大小
    public void checkBookName(){
        if(bookname.length()>=35)
            bookname = bookname.substring(0,35);
    }
//限制图书信息长度大小
    public void checkInfoDetail(){
        if(infodetail.length()>=40)
            infodetail = infodetail.substring(0,40);
    }
}
