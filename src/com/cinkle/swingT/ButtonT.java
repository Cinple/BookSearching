package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;

public class ButtonT extends JFrame{
    private JButton button=new JButton("3D位置                                          ");
    private JTextPane tp = new JTextPane();
    public ButtonT(){
        setLayout(new FlowLayout());

        tp.setText("pengyou");
        //button.setBackground(Color.DARK_GRAY);
        button.setFocusPainted(false);
        button.setHorizontalTextPosition(SwingConstants.LEFT);
        add(button);
        add(tp);
    }
    public static void main(String[] args) {
        SwingConsole.run(new ButtonT(),300,600);
    }
}
