package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.concurrent.ExecutorService;

/*
* 点击主页面UI中的热门借阅和新书通报，就可以进入此界面
* 此页面主要是图书信息的中间环节，分类选取，以获得某一类型的书籍
* 中间环节没有好好设计请见谅
* */
public class NewHotSortUI extends JPanel {

    boolean new_hot;

    JLabel back = new JLabel();
    JLabel home = new JLabel();
    JPanel backhome = new JPanel();

    JLabel choose = new JLabel("选择图书类型");

    JPanel up = new JPanel();

    JLabel[] type = {new JLabel("A.马列主义、毛泽东思想、邓小平理论"),
        new JLabel("B.哲学、宗教"),
        new JLabel("C.社会科学总论"),
        new JLabel("D.政治、法律"),
        new JLabel("E.军事"),
        new JLabel("F.经济"),
        new JLabel("G.文化、科学、教育、体育"),
        new JLabel("H.语言、文字"),
        new JLabel("I.文学"),
        new JLabel("J.艺术"),
        new JLabel("K.历史、地理"),

        new JLabel("N.自然科学总论"),
        new JLabel("O.数理科学与化学"),
        new JLabel("P.天文学、地球科学"),
        new JLabel("Q.生物科学"),
        new JLabel("R.医药、卫生"),
        new JLabel("S.农业科学"),
        new JLabel("T.工业技术"),
        new JLabel("U.交通运输"),
        new JLabel("V.航空、航天"),
        new JLabel("X.环境科学,安全科学"),
        new JLabel("Z.综合性图书") };

    JPanel typeall = new JPanel();

    public NewHotSortUI(boolean bool){
        new_hot = bool;

        FlowLayout father = new FlowLayout();
        father.setAlignment(FlowLayout.LEFT);
        father.setHgap(30);
        father.setVgap(15);
        setLayout(father);
        setPreferredSize(new Dimension(1200,800));

        back.setIcon(new ImageIcon("res/back.jpg"));
        home.setIcon(new ImageIcon("res/home.jpg"));
        home.setText("    ");
        home.setHorizontalTextPosition(JLabel.LEFT);
        addMouseActToBH();

        FlowLayout flowA = new FlowLayout();
        flowA.setAlignment(FlowLayout.LEFT);
        backhome.setLayout(flowA);
        backhome.add(back);
        backhome.add(home);
        backhome.setPreferredSize(new Dimension(1200,50));

        FlowLayout flowB = new FlowLayout();
        flowB.setAlignment(FlowLayout.LEFT);
        up.setLayout(flowB);
        up.setBackground(Color.GRAY);
        choose.setFont(new Font("dialog",Font.PLAIN,18));
        up.add(choose);
        up.setPreferredSize(new Dimension(1130,35));

        GridLayout grid = new GridLayout(11,2);
        grid.setHgap(100);
        grid.setVgap(25);
        typeall.setLayout(grid);
        changeFont();
        for(int i=0;i<type.length;i++)
            addMouseActToLabel(type[i]);
        addPanel();

        add(backhome);
        add(up);
        add(typeall);
    }
    //给JLabel添加监视器
    private void addMouseActToLabel(JLabel label){
        String str = label.getText();
        class ActionT implements MouseListener{
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame frame = UIT.getJframe();
                frame.getContentPane().removeAll();
                frame.getContentPane().add(new BookShow(new_hot,label.getText()));
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
                label.setText("<html><u>"+str+"</u><html>");
                label.setForeground(Color.BLUE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                label.setText(str);
                label.setForeground(Color.BLACK);
            }
        }
        label.addMouseListener(new ActionT());
    }
    //给back和home添加监视器
    private void addMouseActToBH(){
        class action implements MouseListener{
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame frame = UIT.getJframe();
                frame.getContentPane().removeAll();
                JPanel panel = ((UIT)frame).getMainJpanel();
                frame.getContentPane().add(panel);
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
    //改变type中的字体
    private void changeFont(){
        Font font = type[0].getFont();
        font = font.deriveFont((float)20);
        for(int i=0;i<type.length;i++){
            type[i].setFont(font);
        }
    }
    //将type添加进typeall中
    private void addPanel(){
        for(int i =0 ;i<type.length/2;i++){
            typeall.add(type[i]);
            typeall.add(type[i+11]);
        }
    }
}
