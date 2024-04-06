package com.kh.os.enrolment;

import com.kh.os.dbconn.DbConn;
import com.kh.os.user.NotUserVO;
import com.kh.os.user.UserVO;
import org.apache.catalina.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



    public class EnrolmentDao {
        public static void main(String[] args) throws SQLException {
        }
        Connection conn = null; // db 연결
        Statement stmt = null; // 자바에서 쿼리 실행
        PreparedStatement pStmt = null; // 위에 놈 상속/상위호환
        ResultSet rs = null; // 딕셔너리 같은 역할을 하는 그릇

        public List<EnrolmentVo> allEnrList(UserVO user) throws SQLException {
            List<EnrolmentVo> list = new ArrayList<>();
            conn = DbConn.getConnection();
            String q;
            if(user.getId().equals("master")){
                q = "SELECT * FROM ENROLMENT ORDER BY enrdate";
                pStmt = conn.prepareStatement(q);
            }else {
                q = "SELECT * FROM ENROLMENT WHERE id = ? ORDER BY enrdate ";
                pStmt = conn.prepareStatement(q);
                pStmt.setString(1, user.getId());
            }
            rs = pStmt.executeQuery();

            while (rs.next()){
                String name = rs.getString("name");
                String date = rs.getString("ENRDATE");
                String phoneNumber = rs.getString("phonenumber");
                String inquiry = rs.getString("inquiry");
                String id = rs.getString("id");
                EnrolmentVo ev = new EnrolmentVo(name,date,phoneNumber,inquiry,id);

                list.add(ev);
            }
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
            return list;
        }
        public List<EnrolmentVo> allEnrList(NotUserVO nUser) throws SQLException {
            List<EnrolmentVo> list = new ArrayList<>();
            conn = DbConn.getConnection();
            String q = "SELECT * FROM ENROLMENT WHERE phonenumber = ? ORDER BY enrdate";
            pStmt = conn.prepareStatement(q);
            pStmt.setString(1,nUser.getPhoneNumber());
            rs = pStmt.executeQuery();

            while (rs.next()){
                String name = rs.getString("name");
                String date = rs.getString("ENRDATE");
                String phoneNumber = rs.getString("phonenumber");
                String inquiry = rs.getString("inquiry");
                String id = rs.getString("id");
                EnrolmentVo ev = new EnrolmentVo(name,date,phoneNumber,inquiry,id);

                list.add(ev);
            }
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
            return list;
        }

        public int insertEnr(UserVO user, EnrolmentVo enr) throws SQLException {
            conn = DbConn.getConnection();
            String q;
            q = "SELECT * FROM ENROLMENT WHERE ENRDATE = ?";
            pStmt = conn.prepareStatement(q);
            pStmt.setString(1, enr.getDate());
            rs = pStmt.executeQuery();
            if (rs.next()){
                return 0;
            }
            q = "INSERT INTO enrolment(NAME,  ENRDATE, INQUIRY, ID) VALUES (?, ?, ?, ?)";
            pStmt = conn.prepareStatement(q);
            // 여기서 ?에 해당하는 값들을 설정
            pStmt.setString(1,user.getName() );
            pStmt.setString(2, enr.getDate());
            pStmt.setString(3,enr.getInquiry() );
            pStmt.setString(4, user.getId());
            int rst = pStmt.executeUpdate();
            DbConn.close(pStmt);
            DbConn.close(conn);
            return rst;
        }
        public int insertEnr(NotUserVO notUser,EnrolmentVo enr ) throws SQLException {
            conn = DbConn.getConnection();
            String q;
            q = "SELECT * FROM ENROLMENT WHERE ENRDATE = ?";
            pStmt = conn.prepareStatement(q);
            pStmt.setString(1, enr.getDate());
            rs = pStmt.executeQuery();
            if (rs.next()){
                return 0;
            }
            // 비로그인 상태인 경우
            q = "INSERT INTO enrolment(NAME, ENRDATE, PHONENUMBER, INQUIRY) VALUES (?, ?, ?, ?)";
            pStmt = conn.prepareStatement(q);
            // 여기서 ?에 해당하는 값들을 설정
            pStmt.setString(1, notUser.getName());
            pStmt.setString(2,enr.getDate() );
            pStmt.setString(3, notUser.getPhoneNumber());
            pStmt.setString(4, enr.getInquiry());
            // 쿼리 실행
            int rst = pStmt.executeUpdate();
            DbConn.close(pStmt);
            DbConn.close(conn);
            return rst;
        }

        public int deleteEnr(UserVO user, String date) throws SQLException{
            conn  = DbConn.getConnection();
            String q = "DELETE FROM ENROLMENT WHERE ID = ? AND ENRDATE = ?";
            pStmt = conn.prepareStatement(q);
            pStmt.setString(1, user.getId());
            pStmt.setString(2,date );
            int rst = pStmt.executeUpdate();
            DbConn.close(pStmt);
            DbConn.close(conn);
            return rst;
        }
        public int deleteEnr(NotUserVO notUser, String date) throws SQLException{
            conn  = DbConn.getConnection();
            String q = "DELETE FROM ENROLMENT WHERE phonenumber = ? AND ENRDATE = ?";
            pStmt = conn.prepareStatement(q);
            pStmt.setString(1, notUser.getPhoneNumber());
            pStmt.setString(2,date);
            int rst = pStmt.executeUpdate();
            DbConn.close(pStmt);
            DbConn.close(conn);
            return rst;
        }

    }

