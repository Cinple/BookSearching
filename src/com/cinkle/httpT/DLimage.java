package com.cinkle.httpT;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
public class DLimage {
    public static ImageIcon changeSize(Image image){
        //构建图片流
        BufferedImage tag = new BufferedImage(65,95,BufferedImage.TYPE_INT_RGB);
        //绘制改变尺寸后的图
        tag.getGraphics().drawImage(image,0,0,65,95,null);
        //返回ImageIcon实例
        return new ImageIcon(tag);
    }
}
