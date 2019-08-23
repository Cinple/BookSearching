package com.cinkle.swingT;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class labelT extends JFrame {
    private JLabel label = new JLabel("3D位置");
    public labelT(){
        setLayout(new FlowLayout());
        label.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("鼠标点击了");
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                label.setForeground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setForeground(Color.BLACK);
            }
        });
        label.setBackground(Color.YELLOW);
        add(label);
    }

    public static void main(String[] args) {
        SwingConsole.run(new labelT(),500,400);
    }
}
