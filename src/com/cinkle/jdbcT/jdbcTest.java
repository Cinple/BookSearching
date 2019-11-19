package com.cinkle.jdbcT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;
//单例模式
public class jdbcTest implements Runnable{
    private Connection con;
    private Statement stmt;
    private LinkedList<CellBean> list=new LinkedList<>();
//    private HashMap<String,Integer> category=new HashMap<>();
    private HashSet<String> category = new HashSet<>();
    private Properties proper = new Properties();
    static private jdbcTest jdbc;

    static class CellBean{
        int id;
        int side;
        int line;
        int row;
        String first;
        String second;
        private CellBean(int id,int side,int line,int row,String first,String second){
            this.id=id;
            this.side=side;
            this.line=line;
            this.row=row;
            this.first=first;
            this.second=second;
        }
    }
    public static jdbcTest getJdbc(){
        return jdbc;
    }
    public jdbcTest(){}
    public void init(){
        try{
            proper.load(new FileInputStream("res/config.properties"));
            //加载驱动器，下面的代码为加载MySQL驱动器
            String Driver = proper.getProperty("driver");
            String url =proper.getProperty("url");
            String user =proper.getProperty("user");
            String password = proper.getProperty("password");
            Class.forName(Driver);
            con= DriverManager.getConnection(url,user,password);
            if(con==null)
                System.out.println("con is null");
            stmt=con.createStatement();
            if(stmt==null)
                System.out.println("stmt is null");
        }catch(FileNotFoundException e){
            System.out.println("FileNotFound");
        }catch(ClassNotFoundException e){
            System.out.println("ClassNotFound");
        }catch(SQLException e){
            System.out.println("SQL");
        }catch(IOException e){
            System.out.println("load Error");
        }
    }
    public ResultSet query(String sql){
        ResultSet rs;
        try{
            rs=stmt.executeQuery(sql);
            return rs;
        }catch(SQLException e){
            System.out.println("There was a SQLException when get result");
        }
        return null;
    }
    public void close(){
        try{
            if(con != null)
                con.close();
            if(stmt != null)
                stmt.close();
        }catch(SQLException e){
            System.out.println("error when close");
        }
    }
    private void changeBean() throws Exception{
        for(CellBean bean : list){
            if(!bean.first.contains("/")){
                bean.first=bean.second;
                String sql="update branch set number_first = \""+bean.second+"\" where shelf_id = "+bean.id+" && side = "+bean.side+" && line_shelf = "+bean.line+
                        " && row_shelf = "+bean.row;
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }
            if(!bean.second.contains("/")){
                bean.second=bean.first;
                String sql="update branch set number_second = \""+bean.first+"\" where shelf_id = "+bean.id+" && side = "+bean.side+" && line_shelf = "+bean.line+
                        " && row_shelf = "+bean.row;
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }
        }
    }
    public LinkedList<CellBean> getList(){
        return list;
    }
    public HashSet<String> getSet(){
        return category;
    }
    private void setList(){
        String sql ="select * from branch";
        ResultSet rs=query(sql);
        try{
            while(rs.next()){
                int id =rs.getInt("shelf_id");
                int side=rs.getInt("side");
                int line=rs.getInt("line_shelf");
                int row=rs.getInt("row_shelf");
                String first=rs.getString("number_first");
                String second=rs.getString("number_second");
                list.add(new CellBean(id,side,line,row,first,second));
            }
        }catch(Exception e){
            System.out.println("error when set List");
        }
    }
    private void setCategory(){
        String sql="select * from total";
        ResultSet  rs=query(sql);
        try{
            while(rs.next()){
                String  str = rs.getString("number");
//                不需要书架的ID是因为ID不全，一个类型可能分布在多个书架上
//                int id=rs.getInt("shelf_id");
                category.add(str);
            }
        }catch(SQLException e){
            System.out.println("error when set Set");
        }
    }
    @Override
    public void run(){
        init();
        setList();

        setCategory();
        jdbc=this;
    }

    public static void main(String[] args) throws Exception{
        jdbcTest test = new jdbcTest();
        test.run();
    }
}