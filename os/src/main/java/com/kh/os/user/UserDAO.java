package com.kh.os.user;

import com.kh.os.dbconn.DbConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UserDAO {
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
    // return 1 성공, return 2 (회원)비밀번호 다름, return 0 아이디 없음
    public int signIn(UserVO user) throws SQLException {
        conn = DbConn.getConnection();
        int rrs = 0;
        String q = "SELECT * FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1, user.getId());
        rs = pstmt.executeQuery();
        if(rs.next()){
            // id 있는 경우
            if(rs.getString("password").equals(user.getPassword())){
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
        
    //비회원
    // return 1 로그인 성공 및 아이디 없었다면 회원가입까지 한번에 성공
    // return 0 이미 가입된 폰번호가 있는데 이름이 다름
    public int signIn(NotUserVO nUser) throws SQLException {
        conn = DbConn.getConnection();
        String q = "SELECT * FROM notusertb WHERE phonenumber = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,nUser.getPhoneNumber());
        rs = pstmt.executeQuery();
        int rrs = 0;
        if(rs.next()){
            if(nUser.getName().equals(rs.getString("name"))){
                rrs = 1;
            }
        }else{
            q = "INSERT INTO notusertb (phonenumber,name) VALUES (?,?)";
            pstmt = conn.prepareStatement(q);
            pstmt.setString(1,nUser.getPhoneNumber());
            pstmt.setString(2,nUser.getName());
            rrs = pstmt.executeUpdate();
        }
        DbConn.close(rs);
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rrs;
    }
    
    //회원가입 , ID 중복 확인 먼저 한 뒤 Password, 이름, 본인확인 질문, 답변 입력
    // return 0 -> ID 중복, return 1 -> 아이디 생성
    public int signUp(UserVO user) throws SQLException{
        conn = DbConn.getConnection();
        String q = "SELECT id, password FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,user.getId());
        int rrs = 0;
        rs = pstmt.executeQuery();
        if(!rs.next()){
            q = "INSERT INTO usertb VALUES(?,?,?,?,?)";
            try {
                pstmt = conn.prepareStatement(q);
                pstmt.setString(1,user.getId());
                pstmt.setString(2,user.getPassword());
                pstmt.setString(3,user.getName());
                pstmt.setString(4,user.getQuestion());
                pstmt.setString(5,user.getAnswer());
                rrs = pstmt.executeUpdate();
            }catch (Exception e ){
                e.printStackTrace();
            }
        }
        DbConn.close(rs);
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rrs;
    }

    //비밀번호 수정
    // return 0 -> 수정 실패, return 1 -> 수정 성공
    public int findPassword(UserVO user) throws SQLException{
        conn = DbConn.getConnection();
        String q="UPDATE usertb SET password = ? WHERE id = ?";;
        int rrs = 0;
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1, user.getPassword());
        pstmt.setString(2,user.getId());
        rrs = pstmt.executeUpdate();
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rrs;
    }

    //회원 정보 가져오기
    public UserVO userInfo(UserVO user)throws SQLException{
        conn = DbConn.getConnection();
        String q = "SELECT * FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1, user.getId());
        rs = pstmt.executeQuery();
        UserVO userDTO = null;
        if(rs.next()){
            userDTO = new UserVO(rs.getString("id"),rs.getString("password")
                    ,rs.getString("name"),rs.getString("question"),rs.getString("answer"));
        }
        DbConn.close(pstmt);
        DbConn.close(conn);
        return userDTO;
    }

    public NotUserVO userInfo(NotUserVO nUser)throws SQLException{
        conn = DbConn.getConnection();
        String q = "SELECT * FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1, nUser.getPhoneNumber());
        rs = pstmt.executeQuery();
        NotUserVO userDTO = null;
        if(rs.next()){
            userDTO = new NotUserVO(rs.getString("phonenumber"),rs.getString("name"));
        }
        DbConn.close(pstmt);
        DbConn.close(conn);
        return userDTO;
    }

    //회원 탈퇴
    // 아이디, 비밀번호, 본인확인 질문 보여 준 뒤 답변 받고 맞으면
    // return 0 -> 아이디 없음
    // return 1 -> 삭제완료
    // return 2 -> 비밀번호 틀림
    // return 3 -> 본인확인 답변 틀림
    public int withDraw(UserVO user) throws SQLException{
        int rrs = 0;
        conn = DbConn.getConnection();
        String q = "SELECT * FROM usertb WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,user.getId());
        rs = pstmt.executeQuery();
        if(rs.next()){
            if(user.getPassword().equals(rs.getString("password"))){
                if(user.getAnswer().equals(rs.getString("answer"))) {
                    q = "DELETE FROM usertb WHERE id = ? ";
                    pstmt = conn.prepareStatement(q);
                    pstmt.setString(1, user.getId());
                    rrs = pstmt.executeUpdate();
                }
                else rrs = 3;
            }
            else rrs = 2;
        }
        DbConn.close(rs);
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rrs;
    }


    // 내 정보 수정
    // User 값을 받아 정보 수정 1 이면 수정 성공
    public int updateUser(UserVO user)throws SQLException{
        conn = DbConn.getConnection();
        String q = "UPDATE usertb SET password = ?, name = ?, question = ?, answer = ? WHERE id = ?";
        pstmt = conn.prepareStatement(q);
        pstmt.setString(1,user.getPassword());
        pstmt.setString(2,user.getName());
        pstmt.setString(3,user.getQuestion());
        pstmt.setString(4,user.getAnswer());
        pstmt.setString(5,user.getId());
        int rst = pstmt.executeUpdate();
        DbConn.close(rs);
        DbConn.close(pstmt);
        DbConn.close(conn);
        return rst;
    }
}

