package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/*
* 这一个类是系统的搜索栏UI
* 它会发生两个动作：
* 第一个动作是将界面跳转到BookSearch界面UI
* 第二个动作是从网上获取图书的所有信息，并加入display中
* */
public class Search extends JPanel {

    private JTextField text=new JTextField(40);
    private JButton button = new JButton("搜索");

    private CountDownLatch latch;
    private ExecutorService executor;
    private Object lock=new Object();

    private JPanel display=new JPanel();

    static class BeanTask implements Runnable{

        private String bookName;
        private int pageNum;
        private CountDownLatch lat;
        private Search sear;

        public BeanTask(String bookName,int pageNum,CountDownLatch lt,Search s){
            this.bookName=bookName;
            this.pageNum=pageNum;
            this.lat=lt;
            this.sear=s;
        }
        @Override
        public void run(){
            VisitWeb.getInformation(bookName,pageNum,sear);
            lat.countDown();
        }
    }

    public Search(ExecutorService e){
        this.executor=e;
        FlowLayout layout= new FlowLayout();
        setLayout(layout);

        initialDisplay(display);

        text.setFont(new Font("宋体",Font.PLAIN,19));
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(text.getText().equals("")){
                    JOptionPane.showMessageDialog(null,"请输入书名！");
                }
                else{
                    String str = text.getText();
                    if(VisitWeb.isEmpty(str)){
                        JOptionPane.showMessageDialog(null,"未找到相关书籍");
                    }
                    else{
                        String utf8Name = VisitWeb.changeUTF8ToURL(str);
                        String url = "http://opac.ouc.edu.cn:8081/m/opac/search.action?q="+utf8Name+"&t=any";
                        int page = VisitWeb.getPageNum(url);
//                      使用多线程执行任务
                        latch =new CountDownLatch(page);
                        BeanTask[] tasks = new BeanTask[page];
                        for(int i=0;i <= page-1;i++){
                            tasks[i]=new BeanTask(str,i+1,latch,Search.this);
                            executor.submit(tasks[i]);
                        }

                        try{
                            latch.await(6,TimeUnit.SECONDS);
                        }catch(InterruptedException p){
                            System.out.println("error int addActionListener in Search class");
                        }
                        JFrame frame = UIT.getJframe();
                        frame.getContentPane().removeAll();

                        Search temp = new Search(executor);
                        temp.setFieldText(Search.this.getFieldText());
                        Search.this.setFieldText("");
                        frame.getContentPane().add(new BookSearch(temp,addScrollForDisplay(display,1000,520)));

                        frame.getContentPane().validate();
                        frame.getContentPane().repaint();
                    }
                }
            }
        });
        add(text);
        add(button);
    }
    public static void initialDisplay(JPanel display){
        GridLayout grid = new GridLayout(0,2);
        grid.setVgap(30);
        display.setLayout(grid);
    }
    public static JScrollPane addScrollForDisplay(JPanel display,int width,int height){
        JScrollPane scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(width,height));
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        return scroll;
    }
    public void addBean(LabelBean bean){
        bean.initial();
        synchronized (lock){
            display.add(bean);
        }
    }
    public JPanel getDisplay(){
        return display;
    }
    public void setFieldText(String str){
        text.setText(str);
    }
    public String getFieldText(){
        return text.getText();
    }
    public void clearContext(){
        display.removeAll();
    }
}
