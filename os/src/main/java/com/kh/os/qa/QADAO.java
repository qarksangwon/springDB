package com.kh.os.qa;

import com.kh.os.dbconn.DbConn;
import com.kh.os.user.NotUserVO;
import com.kh.os.user.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QADAO {

    public List<QAVO> loadMasterAnswer() throws SQLException{
        Connection conn = DbConn.getConnection();
        String q = "SELECT * FROM qatb WHERE answer is not null";
        PreparedStatement pstmt = conn.prepareStatement(q);
        ResultSet rs = pstmt.executeQuery();
        List<QAVO> qaList = new ArrayList<QAVO>();
        while(rs.next()){
            QAVO qvo;
            String isIdNull = rs.getString("id");
            if(isIdNull == null) qvo = new QAVO(rs.getString("phonenumber"), rs.getString("question"), rs.getString("answer"));
            else qvo = new QAVO(rs.getString("id"), rs.getString("question"), rs.getString("answer"));
            qaList.add(qvo);
        }
        DbConn.close(rs);
        DbConn.close(pstmt);
        DbConn.close(conn);
        return qaList;
    }

    public List<QAVO> loadQA(UserVO user) throws SQLException {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QAVO> qaList = new ArrayList<QAVO>();
        String q;
        try {
            conn = DbConn.getConnection();
            stmt = conn.createStatement();
            if (user.getId().equals("master")) {
                q = "SELECT * from QATB WHERE answer is null";
            } else {
                q = "SELECT * from QATB WHERE id = '" + user.getId() + "'";
            }
            rs = stmt.executeQuery(q);
            if(user.getId().equals("master")) {
                while(rs.next()) {
                    QAVO qvo;
                    String isIdNull = rs.getString("id");
                    if(isIdNull == null) qvo = new QAVO(rs.getString("phonenumber"), rs.getString("question"), "");
                    else qvo = new QAVO(rs.getString("id"), rs.getString("question"), "");
                    qaList.add(qvo);
                }
            } else {
                while(rs.next()) {
                    QAVO qvo = new QAVO(rs.getString("id"), rs.getString("question"),rs.getString("answer"));
                    qaList.add(qvo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            // Finally block to ensure resources are closed
            DbConn.close(rs);
            DbConn.close(stmt);
            DbConn.close(conn);
        }
        return qaList;
    }


    public List<QAVO> loadQA(NotUserVO nUser) throws SQLException {
        String rrs = null;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<QAVO> qaList = new ArrayList<QAVO>();
        try {
            conn = DbConn.getConnection();
            stmt = conn.createStatement();
            String q = "SELECT * from qatb WHERE phonenumber = '" + nUser.getPhoneNumber()+ "'";
            rs = stmt.executeQuery(q);
            while(rs.next()) {
                QAVO qvo = new QAVO(rs.getString("phonenumber"), rs.getString("question"),rs.getString("answer"));
                qaList.add(qvo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        DbConn.close(rs);
        DbConn.close(stmt);
        DbConn.close(conn);
        return qaList;
    }

    public int updateQA(QAVO qav) throws SQLException{
        Connection conn = DbConn.getConnection();
        PreparedStatement pstmt;
        String q;
        if(!qav.getId().equals("")) {
            q = "UPDATE qatb SET answer = ? WHERE id = ? AND question = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,qav.getAnswer());
            pstmt.setString(2,qav.getId());
            pstmt.setString(3,qav.getQuestion());
        }else{
            q = "UPDATE qatb SET answer = ? WHERE phonenumber = ? AND question = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1, qav.getAnswer());
            pstmt.setString(2,qav.getPhonenumber());
            pstmt.setString(3,qav.getQuestion());
        }
        int rs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rs;
    }

    //1성공
    public int insertQA(QAVO qa) throws SQLException{
        Connection conn = DbConn.getConnection();
        PreparedStatement pstmt= null;
        String q = null;
        if(qa.getPhonenumber().equals("")) q = "INSERT INTO qatb(id,question) VALUES(?,?)";
        else q = "INSERT INTO qatb(phonenumber,question) VALUES(?,?)";
        pstmt = conn.prepareStatement(q);
        if(qa.getPhonenumber().equals("")) pstmt.setString(1,qa.getId());
        else pstmt.setString(1,qa.getPhonenumber());
        pstmt.setString(2,qa.getQuestion());
        int rs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rs;
    }

    public int deleteQA(QAVO qa) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int rs = 0;
        try {
            conn = DbConn.getConnection();
            String deleteSql = null;
            if(!qa.getId().equals("")) deleteSql = "DELETE FROM qatb WHERE ID = ? AND QUESTION = ?";
            else deleteSql = "DELETE FROM qatb WHERE phonenumber = ? AND QUESTION = ?";
            pstmt = conn.prepareStatement(deleteSql);
            if(!qa.getId().equals("")) pstmt.setString(1, qa.getId());
            else pstmt.setString(1,qa.getPhonenumber());
            pstmt.setString(2, qa.getQuestion());

            rs = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pstmt);
            DbConn.close(conn);
        }
        return rs;
    }

}
