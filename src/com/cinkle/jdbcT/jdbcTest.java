package com.cinkle.jdbcT;

import java.sql.*;
public class jdbcTest {
    public static void main(String[] args) throws Exception{
        Connection con;
        Statement stmt;
        ResultSet rs;
        //加载驱动器，下面的代码为加载MySQL驱动器
        Class.forName("com.mysql.cj.jdbc.Driver");
        //注册MySQL驱动器
        //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        //连接到数据库的URL
        String url = "jdbc:mysql://localhost:3306/loo?useUnicode=true&serverTimezone=UTC";
        String username="root";
        String password="hoh473726";
        con= DriverManager.getConnection(url,username,password);
        stmt=con.createStatement();
        rs=stmt.executeQuery("select * from fruits");
        while(rs.next()){
            String id = rs.getString(1);
            int sid = rs.getInt("s_id");
            String name = rs.getString(3);
            float price = rs.getFloat(4);
            System.out.println(id +" " + sid + " " + name +" " + price);
        }
        rs.close();
        stmt.close();
        con.close();
    }
}