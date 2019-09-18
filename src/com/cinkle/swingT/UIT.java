package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;

/*
*这个类是图书可视化查询系统主界面UI
* 由三个组件构成：搜索栏，热门借阅和新书通报
**/
public class UIT extends JFrame implements MouseListener,Runnable {

    private JPanel mMainJpanel=new JPanel();
    private JPanel mContantPanel = new JPanel();
    private static JFrame mJframe;

    Search search;

    private JLabel newbook = new JLabel("新书通报");
    private JLabel newbookimage = new JLabel(new ImageIcon("res/newbok.jpg"));           //300*180
    private JPanel nbook = new JPanel();

    private JLabel hotborrow = new JLabel("热门借阅");
    private JLabel hotborrowimage = new JLabel(new ImageIcon("res/hotbok.jpg"));      //300*180
    private JPanel hborrow = new JPanel();

    private ExecutorService executorService;
    public UIT(ExecutorService exec){
        this.executorService=exec;
    }

    @Override
    public void run(){
        init();
    }
    public void init(){
        search = new Search(executorService);

        JPanel newhot=new JPanel();
        //新书通报标签
        nbook.setLayout(new FlowLayout());
        nbook.add(newbookimage);
        Font font = newbook.getFont();
        font=font.deriveFont((float) 16);
        newbook.setFont(font);
        nbook.add(newbook);
        nbook.setPreferredSize(new Dimension(310,220));
        nbook.addMouseListener(this);

        setSize(1200,800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      添加窗口关闭事件：关闭线程池
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                executorService.shutdownNow();
            }
        });

        //热门借阅标签
        hborrow.setLayout(new FlowLayout());
        hborrow.add(hotborrowimage);
        font = hotborrow.getFont();
        font = font.deriveFont((float)16);
        hotborrow.setFont(font);
        hborrow.add(hotborrow);
        hborrow.setPreferredSize(new Dimension(310,220));
        hborrow.addMouseListener(this);

        //新书和热门两者结合
        FlowLayout flayout = new FlowLayout();
        flayout.setHgap(90);
        newhot.setLayout(flayout);
        newhot.add(nbook);
        newhot.add(hborrow);

        //设置主页面
        FlowLayout allayout = new FlowLayout();
        allayout.setVgap(120);
        mMainJpanel.setLayout(allayout);
        mMainJpanel.add(search);
        mMainJpanel.add(newhot);
        mMainJpanel.setPreferredSize(new Dimension(1000,600));
        mContantPanel.add(mMainJpanel);

        setLayout(new FlowLayout());
        setContentPane(mContantPanel);
        setTitle("中国海洋大学图书可视化系统");
        ImageIcon icon = new ImageIcon("res/schimg.jpg");
        setIconImage(icon.getImage());
        setVisible(true);
        mJframe = this;
    }
    //设置新书和热门的监视器
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()== nbook){
            getContentPane().removeAll();
            getContentPane().add(new NewHotSortUI(false,executorService));
            getContentPane().validate();
            getContentPane().repaint();
        }
        else if(e.getSource() == hborrow){
            getContentPane().removeAll();
            getContentPane().add(new NewHotSortUI(true,executorService));
            getContentPane().validate();
            getContentPane().repaint();
        }
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

    public static JFrame getJframe(){
        return mJframe;
    }
    public JPanel getMainJpanel(){
        search.clearContext();
        return mMainJpanel;
    }
    public JPanel getContPanel(){
        return mContantPanel;
    }
}
