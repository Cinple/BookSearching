package com.cinkle.jdbcT;

import java.sql.*;
public class jdbcTest {
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    public void init() throws Exception{
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
    }
    public void query(String sql) throws Exception{
        rs = stmt.executeQuery(sql);
    }
    public void close()throws Exception{
        if(con != null)
            con.close();
        if(stmt != null)
            stmt.close();
        if(rs != null)
            rs.close();
    }
    public ResultSet getResult(){
        return rs;
    }
    public static void main(String[] args) throws Exception{

        jdbcTest test = new jdbcTest();
        test.init();
        String sql = "select * from fruits";
        test.query(sql);
        ResultSet rs = test.getResult();
        while(rs.next()){
            String id = rs.getString(1);
            int sid = rs.getInt("s_id");
            String name = rs.getString(3);
            float price = rs.getFloat(4);
            System.out.println(id +" " + sid + " " + name +" " + price);
        }
        test.close();
    }
}