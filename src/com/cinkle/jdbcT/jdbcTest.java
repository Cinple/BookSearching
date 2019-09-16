package com.cinkle.jdbcT;

import java.sql.*;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class jdbcTest implements Runnable{
    private Connection con;
    private Statement stmt;
    private ResultSet rs;
    private LinkedList<CellBean> list=new LinkedList<>();
    private Boolean bool=false;
    static class CellBean{
        int id;
        int side;
        int line;
        int row;
        String first;
        String second;
        public CellBean(int id,int side,int line,int row,String first,String second){
            this.id=id;
            this.side=side;
            this.line=line;
            this.row=row;
            this.first=first;
            this.second=second;
        }
    }
    public void init(String sql){
        try{
            //加载驱动器，下面的代码为加载MySQL驱动器
            Class.forName("com.mysql.cj.jdbc.Driver");
            //注册MySQL驱动器
            //DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            //连接到数据库的URL
            String url = "jdbc:mysql://localhost:3306/library?useUnicode=true&serverTimezone=UTC";
            String username="root";
            String password="hoh473726";
            con= DriverManager.getConnection(url,username,password);
            stmt=con.createStatement();
            rs = stmt.executeQuery(sql);
        }catch(Exception e){
            System.out.println("this is error");
        }
    }
    public void close() throws Exception{
        if(con != null)
            con.close();
        if(stmt != null)
            stmt.close();
        if(rs != null)
            rs.close();
    }
    public LinkedList<CellBean> getList(){
        return list;
    }
    @Override
    public void run(){
        String sql ="select * from branch";
        try{
            init(sql);
            while(rs.next()){
                int id =rs.getInt("shelf_id");
                int side=rs.getInt("side");
                int line=rs.getInt("line_shelf");
                int row=rs.getInt("row_shelf");
                String first=rs.getString("number_first");
                String second=rs.getString("number_second");
                list.add(new CellBean(id,side,line,row,first,second));
            }
            bool =true;
            System.out.println(list.size());
            close();
        }catch(Exception e){
            System.out.println("database raised error");
        }
    }
    public static void main(String[] args) throws Exception{
        ExecutorService executorService = new ThreadPoolExecutor(4,8,60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>());

        executorService.submit(new jdbcTest());
    }
}