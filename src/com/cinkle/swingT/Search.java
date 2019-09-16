package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/*
* 这一个类是系统的搜索栏UI
* 它会发生两个动作：
* 第一个动作是将界面跳转到BookSearch界面UI
* 第二个动作是从网上获取图书的所有信息，并保存在list中
* */
public class Search extends JPanel {

    private JTextField text=new JTextField(40);
    private JButton button = new JButton("搜索");
    private ArrayList<LabelBean> List;
    private CountDownLatch latch;
    private ExecutorService executor;
    static class BeanTask implements Runnable{
        private String bookName;
        private int pageNum;
        private ArrayList<LabelBean> list;
        private CountDownLatch lat;
        public BeanTask(String bookName,int pageNum,CountDownLatch lt){
            this.bookName=bookName;
            this.pageNum=pageNum;
            this.lat=lt;
        }
        @Override
        public void run(){
            list = VisitWeb.getInfomation(bookName,pageNum);
            for(int i=0;i<list.size();i++){
                LabelBean bean=list.get(i);
                bean.initial();
            }
            lat.countDown();
        }
    }

    public Search(ExecutorService e){
        this.executor=e;
        FlowLayout layout= new FlowLayout();
        setLayout(layout);

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
                    if(page==1&&VisitWeb.getInfomation(str,page)==null){
                        JOptionPane.showMessageDialog(null,"未找到相关书籍");
                    }
                    else{
                        latch =new CountDownLatch(page);
                        BeanTask[] tasks = new BeanTask[page];
                        for(int i=0;i <= page-1;i++){
                            tasks[i]=new BeanTask(str,i+1,latch);
                            executor.submit(tasks[i]);
                        }

                        try{
                            latch.await();
                        }catch(InterruptedException p){
                            System.out.println("error int addActionListener in Search class");
                        }
                        //capacity
                        int capacity=page*11;
                        List =new ArrayList<>(capacity);
                        for(int i=0 ; i <= page-1 ; i++)
                            List.addAll(tasks[i].list);

                        JFrame frame = UIT.getJframe();
                        frame.getContentPane().removeAll();

                        Search temp = new Search(executor);
                        temp.setList(Search.this.getList());
                        temp.setFieldText(Search.this.getFieldText());
                        Search.this.setFieldText("");
                        frame.getContentPane().add(new BookSearch(temp));

                        frame.getContentPane().validate();
                        frame.getContentPane().repaint();
                    }
                }
            }
        });
        add(text);
        add(button);
    }
    public void setFieldText(String str){
        text.setText(str);
    }
    public String getFieldText(){
        return text.getText();
    }
    public void setList(ArrayList<LabelBean> L){
        List = L;
    }
    public ArrayList<LabelBean> getList(){
        return List;
    }
}
