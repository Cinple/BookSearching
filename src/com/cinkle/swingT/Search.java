package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

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

    private JPanel display;

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
            VisitWeb.getInfomation(bookName,pageNum,sear);
            lat.countDown();
        }
    }

    public Search(ExecutorService e){
        this.executor=e;
        FlowLayout layout= new FlowLayout();
        setLayout(layout);

        initialDisplay();

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
                    int page = VisitWeb.getPageNum(str);
                    if(page==1 && VisitWeb.isEmpty(str,page)){
                        JOptionPane.showMessageDialog(null,"未找到相关书籍");
                    }
                    else{
//                      使用多线程执行任务
                        latch =new CountDownLatch(page);
                        BeanTask[] tasks = new BeanTask[page];
                        for(int i=0;i <= page-1;i++){
                            tasks[i]=new BeanTask(str,i+1,latch,Search.this);
                            executor.submit(tasks[i]);
                        }

                        try{
                            latch.await();
                        }catch(InterruptedException p){
                            System.out.println("error int addActionListener in Search class");
                        }

                        JFrame frame = UIT.getJframe();
                        frame.getContentPane().removeAll();

                        Search temp = new Search(executor);
                        temp.setFieldText(Search.this.getFieldText());
                        Search.this.setFieldText("");
                        frame.getContentPane().add(new BookSearch(temp,addScrollForDisplay()));

                        frame.getContentPane().validate();
                        frame.getContentPane().repaint();
                    }
                }
            }
        });
        add(text);
        add(button);
    }
    public void initialDisplay(){
        display=new JPanel();
        GridLayout grid = new GridLayout(0,2);
        grid.setVgap(30);
        display.setLayout(grid);
    }
    public JScrollPane addScrollForDisplay(){
        JScrollPane scroll = new JScrollPane(display);
        scroll.setPreferredSize(new Dimension(1000,520));
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        return scroll;
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
