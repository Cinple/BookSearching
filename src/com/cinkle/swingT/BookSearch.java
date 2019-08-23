package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;

public class BookSearch extends JPanel {
    private Search search;
    private JPanel display = new JPanel();
    private JLabel home = new JLabel(new ImageIcon("res/home.jpg"));
    private JPanel Home = new JPanel();
    public BookSearch(Search sear){
        search = sear;
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

        GridLayout grid = new GridLayout(0,2);
        grid.setVgap(30);
        display.setLayout(grid);
        inputDisplay();
        //添加滚动条
        JScrollPane scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(1000,520));
        scroll.getVerticalScrollBar().setUnitIncrement(20);

        setPreferredSize(new Dimension(1200,800));
        add(Home);
        add(search);
        add(scroll);
    }
    private void inputDisplay(){
        LinkedList<LabelBean> list = search.getList();
        LabelBean temp;
        for(int i =0 ;i < list.size();i++){
            temp = list.get(i);
            temp.initial();
            display.add(temp);
        }
    }
}
