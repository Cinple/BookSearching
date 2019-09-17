package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
/*
* BookSearch类用于展示图书信息
* 它包含一个Search，可不断重复搜索
* 并把Search中的list字段中保存的信息打印到屏幕上
**/
public class BookSearch extends JPanel {
    private Search search;
    private JLabel home = new JLabel(new ImageIcon("res/home.jpg"));
    private JPanel Home = new JPanel();
    JScrollPane scrollpane;
    public BookSearch(Search sear,JScrollPane scroll){
        search = sear;
        this.scrollpane=scroll;
        search.setPreferredSize(new Dimension(1150,90));

        home.addMouseListener(new MouseListener() {
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
        });

        FlowLayout flow = new FlowLayout();
        flow.setAlignment(FlowLayout.LEFT);
        Home.setLayout(flow);
        Home.setPreferredSize(new Dimension(1150,40));
        Home.add(home);

        setPreferredSize(new Dimension(1200,800));
        add(Home);
        add(search);
        add(scrollpane);
    }
}
