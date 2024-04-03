package com.kh.os.user;

import com.kh.os.dbconn.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userDAO {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // 회원 or 비 로그인
    // 비회원 최초 1회 바로 회원가입
    //그 이후 이름과 휴대전화 번호로, 번호와 이름 다를 시 로그인 실패
    //휴대전화 번호에 이름 같은 경우, 처음 가입하는 회원 회원가입 됐을 때 return 1,
    // 번호에 이름이 다르면 return 0
    // val 1 -> id, ph
    // val 2 -> password, name
    // return 1 성공, return 2 (회원)비밀번호 다름, return 0 (비회원) 이름다름 
    public int signIn(int isUser, String val1 , String val2) throws SQLException {
        conn = DbConn.getConnection();
        int rrs = 0;
        if(isUser == 0) {
            String q = "SELECT * FROM notusertb WHERE phonenumber = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1, val1);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (val2.equals(rs.getString("name"))) {
                    rrs = 1;
                }
            } else {
                q = "INSERT INTO notusertb (phonenumber,name) VALUES (?,?)";
                pstmt = conn.prepareStatement(q);
                pstmt.setString(1, val1);
                pstmt.setString(2, val2);
                rrs = pstmt.executeUpdate();
            }
            DbConn.close(rs);
            DbConn.close(pstmt);
            DbConn.close(conn);
            return rrs;
        }else{
            String q = "SELECT * FROM usertb WHERE id = ?";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1, val1);
            rs = pstmt.executeQuery(q);
            if(rs.next()){
                // id 있는 경우
                if(rs.getString("password").equals(val2)){
                    rrs = 1; // 로그인 성공
                }else{
                    rrs = 2; //비밀 번호 다름
                }
            }
            DbConn.close(rs);
            DbConn.close(pstmt);
            DbConn.close(conn);
            return rrs;
        }
    }
}
