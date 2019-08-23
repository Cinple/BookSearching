package com.cinkle.swingT;

import com.cinkle.httpT.VisitWeb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Search extends JPanel {

    private VisitWeb visitweb = VisitWeb.getVisitweb();
    private LinkedList<LabelBean> list;

    private JTextField text=new JTextField(40);
    private JButton button = new JButton("搜索");

    public Search(){
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
                    visitweb.setBookName(text.getText());
                    list=visitweb.getInfomation();
                    if(list.size() == 0){
                        JOptionPane.showMessageDialog(null,"未找到相关书籍");
                    }
                    else{
                        JFrame frame = UIT.getJframe();
                        frame.getContentPane().removeAll();

                        Search temp = new Search();
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
    public void setList(LinkedList<LabelBean> L){
        list = L;
    }
    public LinkedList<LabelBean> getList(){
        return list;
    }
}
