package com.ujs.salarymanagementsystem.db;

import android.util.Log;

import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.LoggedInUser;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MySQLoperation {
    private static Connection conn=MySQLHelper.getConnection();

    public static void createTable() throws SQLException {
        Log.d("1","111111111");
        Statement stmt = conn.createStatement();
        String sql1="create table if not exists TableE (id_E integer primary key autoincrement,id_U integer,infor varchar,FOREIGN KEY (id_U) REFERENCES TableU (id_U))";
        String sql2="create table if not exists TableU (id_U integer primary key autoincrement,icon varchar,id_E integer,username varchar unique,passwordd varchar,FOREIGN KEY (id_E) REFERENCES TableE (id_E))";
        String sql3="create table if not exists TableD (id_D integer primary key autoincrement,id_E integer,Dname varchar unique,FOREIGN KEY (id_E) REFERENCES TableE (id_E))";
        String sql4="create table if not exists Table_S (id_S integer primary key autoincrement,id_D integer,Sname varchar,Sposition varchar,FOREIGN KEY (id_D) REFERENCES TableD (id_D))";
        String sql5="create table if not exists TableW (id_W integer primary key autoincrement,id_E integer,id_S integer,dataa varchar,amount_p double,amount_d double,amount_i double,FOREIGN KEY (id_E) REFERENCES TableE (id_E),FOREIGN KEY (id_S) REFERENCES Table_S (id_S))";
        String sql6="create table if not exists TableR (id_R integer primary key autoincrement,id_E integer,id_U integer,FOREIGN KEY (id_U) REFERENCES TableU (id_U),FOREIGN KEY (id_E) REFERENCES TableE (id_E))";
        stmt.execute(sql1);
        stmt.execute(sql2);
        stmt.execute(sql3);
        stmt.execute(sql4);
        stmt.execute(sql5);
        stmt.execute(sql6);
    }

    //---------------------add??????--------------------------

    /**
     * ?????????????????????????????????
     * @param staff ??????
     * @param department ??????
     * @throws SQLException
     */
    public static void updateTable_S_3(Staff staff,Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="update Table_S set id_D="+ department.getId_D()+" where id_S="+staff.getId_S();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ????????????,?????????????????????????????????
     * @param enterprise ??????
     * @param enterprise ??????
     * @throws SQLException
     */
    public static boolean addTableE(Enterprise enterprise, LoggedInUser loggedInUser) throws SQLException {//????????????
        conn = MySQLHelper.getConnection();
        int id_E;//??????????????????id
        //??????DriverManager?????????????????????
        Statement stmt = conn.createStatement();
        //??????Connection???????????????Statment??????
        String sql = "insert into TableE (infor)VALUES('"+enterprise.getName_E()+"')";
        stmt.executeUpdate(sql);
        sql="select * from TableE";
        ResultSet rs=stmt.executeQuery(sql);
        rs.last();
        if(rs.getString(2).equals(enterprise.getName_E())){
            id_E=rs.getInt(1);
        }
        else return false;
        sql="insert into TableR(id_E,id_U)values ("+id_E+","+loggedInUser.getId_U()+")";
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
        return true;
    }

    /**
     * ????????????
     * @param passwordd ??????
     * @return
     * @throws SQLException
     */

    public static void addTableU(String username, String passwordd) throws SQLException {//????????????
        conn = MySQLHelper.getConnection();
        //??????DriverManager?????????????????????
        Statement stmt = conn.createStatement();
        //??????Connection???????????????Statment??????
        String sql = "insert into TableU (username,passwordd)VALUES('"+username+"','"+passwordd+"')";
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ????????????
     * @param enterprise ??????
     * @param department ??????
     * @throws SQLException
     */
    public static void addTableD(Enterprise enterprise, Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="insert into TableD (id_E,Dname) values ("+enterprise.getId_E()+",'"+department.getDname()+"')";
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ????????????
     * @param department ??????
     * @param staff ????????????
     * @throws SQLException
     */
    public static void addTable_S(Department department, Staff staff) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="insert into Table_S (id_D,Sname,Sposition) values ("+department.getId_D()+",'"+staff.getSname()+"','"+staff.getSposition()+"')";
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ????????????
     * @param enterprise ??????
     * @param staff ??????
     * @param wages ??????
     * @throws SQLException
     */
    public static void addTableW(Enterprise enterprise, Staff staff, Wages wages) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="insert into TableW (id_E,id_S,dataa,amount_p,amount_d,amount_s) values ("+enterprise.getId_E()+","+staff.getId_S()+",'"+wages.getData()+"',"+wages.getAmount_p()+","+wages.getAmount_d()+","+wages.getAmount_s()+")";
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    //----------------------delete-----------------------------

    /**
     * ????????????????????????
     * @param enterprise ??????
     * @throws SQLException
     */
    public static void deleteTableE(Enterprise enterprise) throws SQLException{
        // ????????????ID???????????????
        List<Department> list =  selectDbyE(enterprise);
        // ????????????ID?????????????????????????????????????????????
        for(Department d : list){
            deleteTableS_1(d);
            deleteTableD(d);
        }
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        // ?????????????????????
        String sql="delete from TableR where id_E="+enterprise.getId_E();
        stmt.execute(sql);
        // ????????????
        sql="delete from TableE where id_E="+enterprise.getId_E();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????????????????
     * @param department ??????
     * @throws SQLException
     */
    public static void deleteTableD(Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from TableD where id_D="+department.getId_D();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????????????????
     * @param department ??????
     * @throws SQLException
     */
    public static void deleteTableS_1(Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from Table_S where id_D="+department.getId_D();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }
    /**
     * ??????????????????????????????
     * @param staff ??????
     * @throws SQLException
     */
    public static void deleteTabelS_2(Staff staff) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from Table_S where id_S="+staff.getId_S();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void deleteTableW_1(Wages wages) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from TableW where id_W="+wages.getId_W();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????????????????
     * @param staff ??????
     * @throws SQLException
     */
    public static void deleteTableW_2(Staff staff) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from TableW where id_S="+staff.getId_S();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????????????????
     * @param department ??????
     * @throws SQLException
     */
    public static void deleteTableW_4(Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from TableW where id_S =any(select id_S from Table_S where id_D="+department.getId_D()+")";
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }
    /**
     * ??????????????????????????????
     * @param enterprise ??????
     * @throws SQLException
     */
    public static void deleteTableW_3(Enterprise enterprise) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="delete from TableW where id_E="+enterprise.getId_E();
        stmt.execute(sql);
        conn = MySQLHelper.Close(conn);
    }



    //-----------------------update--------------------------------

    /**
     * ??????????????????
     * @param enterprise ??????
     * @throws SQLException
     */
    public static void updateTableE(Enterprise enterprise) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="update TableE set infor='"+enterprise.getName_E()+"'where id_E="+enterprise.getId_E();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ?????????????????????
     * @param loggedInUser ??????
     * @param passwordd ????????????
     * @throws SQLException
     */
    public static void updateTableU(LoggedInUser loggedInUser,String passwordd) throws SQLException{
        conn = MySQLHelper.getConnection();
        Statement stmt=conn.createStatement();
        String sql="update TableU set passwordd='"+passwordd+"'where id_U="+loggedInUser.getId_U();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param department ??????id
     * @throws SQLException
     */
    public static void updateTableD(Department department) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update TableD set Dname='" + department.getDname() + "'where id_D=" + department.getId_D();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param staff ??????
     * @throws SQLException
     */
    public static void updateTable_S_1(Staff staff) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update Table_S set Sname='" + staff.getSname() + "'where id_S=" + staff.getId_S();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param staff ??????
     * @throws SQLException
     */
    public static void updateTable_S_2(Staff staff) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update Table_S set Sposition='" + staff.getSposition() + "'where id_S=" + staff.getId_S();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void updateTableW_1(Wages wages) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update TableW set dataa='" + wages.getData() + "'where id_W=" + wages.getId_W();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void updateTableW_2(Wages wages) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update TableW set amount_p='" + wages.getAmount_P() + "'where id_W=" + wages.getId_W();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void updateTableW_3(Wages wages) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update TableW set amount_d='" + wages.getAmount_D() + "'where id_W=" + wages.getId_W();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ??????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void updateTableW_4(Wages wages) throws SQLException {
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql = "update TableW set amount_i='" + wages.getAmount_S() + "'where id_W=" + wages.getId_W();
        stmt.executeUpdate(sql);
        conn = MySQLHelper.Close(conn);
    }

    /**
     * ???????????????????????????????????????
     * @param wages ??????
     * @throws SQLException
     */
    public static void updateTable_5(Wages wages) throws SQLException{
        Statement stmt = conn.createStatement();
        double amount_s=wages.getAmount_p()-wages.getAmount_d();
        String sql = "update TableW set amount_i='" + amount_s + "'where id_W='" + wages.getId_W() + "'";
        stmt.executeUpdate(sql);
    }

    //---------------------------------select------------------------------------

    /**
     * ????????????????????????????????????
     * @param username ?????????
     * @param passwordd ??????
     * @return loggedInUser ??????
     * @throws SQLException
     */
    public static LoggedInUser selectUbyUup(String username,String passwordd) throws SQLException {
        conn = MySQLHelper.getConnection();
        LoggedInUser loggedInUser;
        Statement stmt = conn.createStatement();
        String sql="select * from TableU where username='"+username+"'and passwordd='"+passwordd+"'";
        ResultSet res=stmt.executeQuery(sql);
        if(res==null || !res.next()){
            conn = MySQLHelper.Close(conn);
            return null;
        }
        else{
            loggedInUser=new LoggedInUser(res.getInt(1),username);
            conn = MySQLHelper.Close(conn);
            return loggedInUser;
        }
    }

    /**
     * ??????
     * ??????id????????????????????????????????????
     * @param id_U ??????id
     * @return ????????????username???passwordd???
     * @throws SQLException
     */
    public static HashMap<String,String> selectUnpbyUid(int id_U) throws SQLException{
        HashMap<String ,String> map;
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select * from TableU where id_U="+id_U;
        ResultSet res=stmt.executeQuery(sql);
        if (res==null || !res.next()){
            conn = MySQLHelper.Close(conn);
            return null;
        }
        else {
            map=new HashMap<>();
            map.put("username",res.getString(3));
            map.put("passwordd",res.getString(4));
            conn = MySQLHelper.Close(conn);
            return map;
        }
    }

    /**
     * ????????????????????????
     * @param loggedInUser ??????
     * @return List ????????????
     * @throws SQLException
     */
    public static List<Enterprise> selectEbyU(LoggedInUser loggedInUser) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Enterprise> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_E from TableR where id_U="+loggedInUser.getId_U();
        ResultSet res=stmt.executeQuery(sql);
        List<Integer> idList = new ArrayList<>();
        while (res.next()){
            idList.add(res.getInt(1));
        }
        for (int i : idList){
            list.add(selectEinforbyEid(i));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????id????????????
     * @param id_E ??????id
     * @return Enterprise ??????
     * @throws SQLException
     */
    public static Enterprise selectEinforbyEid(int id_E) throws SQLException{
        Enterprise enterprise;
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select infor from TableE where id_E="+id_E;
        ResultSet res=stmt.executeQuery(sql);
        if(res==null || !res.next()){
            return null;
        }
        else{
            enterprise=new Enterprise(id_E,res.getString(1));
            return enterprise;
        }
    }

    /**
     * ????????????????????????
     * @param enterprise ??????
     * @return List<Department> ???????????????
     * @throws SQLException
     */
    public static List<Department> selectDbyE(Enterprise enterprise) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Department> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_D,id_E,Dname from TableD where id_E="+enterprise.getId_E();
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Department(res.getInt(1),res.getInt(2),res.getString(3)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????
     * @param s ??????
     * @return List<Department> ???????????????
     * @throws SQLException
     */
    public static Department selectDbyS(Staff s) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Department> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_D,id_E,Dname from TableD where id_D="+s.getId_D();
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Department(res.getInt(1),res.getInt(2),res.getString(3)));
        }
        conn = MySQLHelper.Close(conn);
        return list.get(0);
    }

    /**
     * ????????????????????????
     * @param department ??????
     * @return List<Staff> ???????????????
     * @throws SQLException
     */
    public static List<Staff> selectSbyD(Department department) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Staff> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_S,id_D,Sname,Sposition from Table_S where id_D="+department.getId_D();
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Staff(res.getInt(1),res.getInt(2),res.getString(3),res.getString(4)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????
     * @param staff ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyS(Staff staff) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s from TableW where id_S="+staff.getId_S()+" order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????
     * @param enterprise ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyE(Enterprise enterprise) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s from TableW where id_E="+enterprise.getId_E()+" order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ??????????????????????????????
     * @param Sname ????????????
     * @return List<Staff> ????????????
     * @throws SQLException
     */
    public static List<Staff> selectSbySname(String Sname) throws SQLException{
        List<Staff> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_S,id_D,Sname,Sposition from Table_S where Sname='"+Sname+"'";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Staff(res.getInt(1),res.getInt(2),res.getString(3), res.getString(4)));
        }
        return list;
    }

    /**
     * ????????????ID????????????
     * @param id_s ??????ID
     * @return List<Staff> ????????????
     * @throws SQLException
     */
    public static Staff selectSbyIDS(int id_s) throws SQLException{
        List<Staff> list=new ArrayList<>();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select id_S,id_D,Sname,Sposition from Table_S where id_S="+id_s;
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Staff(res.getInt(1),res.getInt(2),res.getString(3), res.getString(4)));
        }
        conn = MySQLHelper.Close(conn);
        return list.get(0);
    }

    /**
     * ????????????????????????????????????????????????
     * @param staff ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyLargedataaSid(Staff staff,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+staff.getId_S()+" and dataa>'"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????
     * @param staff ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbydataaSid(Staff staff,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+staff.getId_S()+" and dataa='"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param staff ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbySmalldataaSid(Staff staff,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+staff.getId_S()+" and dataa<'"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param staff ??????
     * @param dataaS ?????????
     * @param dataaB ?????????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyBetweendataaSid(Staff staff,String dataaS,String dataaB) throws SQLException{
        List<Wages> list=new ArrayList<>();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+staff.getId_S()+" and dataa>'"+dataaS+"' and dataa<'"+dataaB+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param department ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbydataaDid(Department department,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        List<Staff> list1=selectSbyD(department);
        Iterator<Staff> iterator=list1.iterator();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        while (iterator.hasNext()){
            String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+iterator.next().getId_S()+" and dataa='"+dataa+"' order by dataa";
            ResultSet res=stmt.executeQuery(sql);
            while (res.next()){
                list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
            }
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param department ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyLargedataaDid(Department department,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        List<Staff> list1=selectSbyD(department);
        Iterator<Staff> iterator=list1.iterator();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        while (iterator.hasNext()){
            String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+iterator.next().getId_S()+" and dataa>'"+dataa+"' order by dataa";
            ResultSet res=stmt.executeQuery(sql);
            while (res.next()){
                list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
            }
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param department ??????
     * @param dataa ??????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbySmalldataaDid(Department department,String dataa) throws SQLException{
        List<Wages> list=new ArrayList<>();
        List<Staff> list1=selectSbyD(department);
        Iterator<Staff> iterator=list1.iterator();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        while (iterator.hasNext()){
            String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+iterator.next().getId_S()+" and dataa<'"+dataa+"' order by dataa";
            ResultSet res=stmt.executeQuery(sql);
            while (res.next()){
                list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
            }
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ????????????????????????????????????????????????
     * @param department ??????
     * @param dataaS ?????????
     * @param dataaB ?????????
     * @return List<Wages> ???????????????
     * @throws SQLException
     */
    public static List<Wages> selectWbyBetweendataaDid(Department department,String dataaS,String dataaB) throws SQLException{
        List<Wages> list=new ArrayList<>();
        List<Staff> list1=selectSbyD(department);
        Iterator<Staff> iterator=list1.iterator();
        conn = MySQLHelper.getConnection();
        Statement stmt = conn.createStatement();
        while (iterator.hasNext()){
            String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_S="+iterator.next().getId_S()+" and dataa>'"+dataaS+"' and dataa<'"+dataaB+"' order by dataa";
            ResultSet res=stmt.executeQuery(sql);
            while (res.next()){
                list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
            }
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ?????????????????????????????????????????????
     * @param enterprise ??????
     * @param dataa ??????
     * @return
     * @throws SQLException
     */
    public static List<Wages> selectWbyLargedataaEid(Enterprise enterprise,String dataa) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_E="+enterprise.getId_E()+" and dataa>'"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ?????????????????????????????????????????????
     * @param enterprise ??????
     * @param dataa ??????
     * @return
     * @throws SQLException
     */
    public static List<Wages> selectWbySmalldataaEid(Enterprise enterprise,String dataa) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_E="+enterprise.getId_E()+" and dataa<'"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ?????????????????????????????????????????????
     * @param enterprise ??????
     * @param dataaS ?????????
     * @param dataaB ?????????
     * @return
     * @throws SQLException
     */
    public static List<Wages> selectWbyBetweendataaEid(Enterprise enterprise,String dataaS,String dataaB) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_E="+enterprise.getId_E()+" and dataa>'"+dataaS+"' and dataa<'"+dataaB+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }

    /**
     * ???????????????????????????????????????
     * @param enterprise ??????
     * @param dataa ?????????
     * @return
     * @throws SQLException
     */
    public static List<Wages> selectWbydataaEid(Enterprise enterprise,String dataa) throws SQLException{
        conn = MySQLHelper.getConnection();
        List<Wages> list=new ArrayList<>();
        Statement stmt = conn.createStatement();
        String sql="select id_W,id_E,id_S,dataa,amount_p,amount_d,amount_s FROM TableW where id_E="+enterprise.getId_E()+" and dataa='"+dataa+"' order by dataa";
        ResultSet res=stmt.executeQuery(sql);
        while (res.next()){
            list.add(new Wages(res.getInt(1),res.getInt(2),res.getInt(3),res.getString(4),res.getDouble(5),res.getDouble(6)));
        }
        conn = MySQLHelper.Close(conn);
        return list;
    }
}
