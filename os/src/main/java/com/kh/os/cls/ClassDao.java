package com.kh.os.cls;


import com.kh.os.dbconn.DbConn;
import com.kh.os.user.UserVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClassDao {
    Connection conn = null;
    Statement stmt = null; // createStatement 방식
    PreparedStatement pStmt = null; // Prepared Statement 방식
    ResultSet rs = null; // database 로 부터 결과를 받는 변수
    Scanner sc = new Scanner(System.in);

    //SELECT 문 (조회)
    public List<ClassVo> ClassSelect() {
        List<ClassVo> list = new ArrayList<>();
        try {
            conn = DbConn.getConnection();
            stmt = conn.createStatement();
            String sql = "SELECT * FROM ClassTb WHERE APPLICANT < MAX";
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int openNum = rs.getInt("OPENNUM");
                String title = rs.getString("TITLE");
                String teacher = rs.getString("TEACHER");
                String room = rs.getString("ROOM");
                int applicant = rs.getInt("APPLICANT");
                int max = rs.getInt("MAX");
                list.add(new ClassVo(openNum, title, teacher, room, applicant, max));
            }
            DbConn.close(rs);
            DbConn.close(stmt);
            DbConn.close(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public void classSelectPrn(List<ClassVo> list) {
        for (ClassVo e : list) {
            System.out.print(e.getOpenNum() + ".");
            System.out.print(e.getTitle() + " ");
            System.out.print(e.getTeacher() + " ");
            System.out.print(e.getRoom() + " ");
            System.out.print("[" + e.getApplicant() + "]/");
            System.out.println("[" + e.getMax() + "]");
        }
    }

    public int applyForClass(ClassVo cvo, UserVO user) {
        int result = -1; // 기본적으로 오류로 초기화

        try {
            // 데이터베이스 연결
            conn = DbConn.getConnection();

            // SQL 쿼리문 작성: 선택한 수업 번호에 해당하는 수업 정보 가져오기
            String selectSql = "SELECT * FROM ClassTb WHERE OPENNUM = ?";
            pStmt = conn.prepareStatement(selectSql);
            pStmt.setInt(1, cvo.getOpenNum());
            ResultSet rs = pStmt.executeQuery();

            // 해당 수업 정보가 있는지 확인
            if (rs.next()) {
                int currentApplicant = rs.getInt("APPLICANT");
                int max = rs.getInt("MAX");
                String title = rs.getString("TITLE");
                String room = rs.getString("ROOM");

                selectSql = "SELECT * FROM APPLYUSER WHERE ID = ? AND TITLE = ?";
                pStmt = conn.prepareStatement(selectSql);
                pStmt.setString(1, user.getId());
                pStmt.setString(2, title);
                rs = pStmt.executeQuery();
                if (rs.next()) {
                    // 이미 신청된 내역이 있음
                    System.out.println("이미 신청된 내역이 있습니다.");
                    result = 1;
                } else if (currentApplicant < max) {
                    // 신청 가능한 상태
                    String updateSql = "UPDATE ClassTb SET APPLICANT = APPLICANT + 1 WHERE TITLE = ?";
                    pStmt = conn.prepareStatement(updateSql);
                    pStmt.setString(1, cvo.getTitle());
                    int updateResult = pStmt.executeUpdate();
                    if (updateResult > 0) {
                        String insertSql = "INSERT INTO APPLYUSER (ID, NAME, TITLE, ROOM) VALUES (?, ?, ?, ?)";
                        pStmt = conn.prepareStatement(insertSql);
                        pStmt.setString(1, user.getId());
                        pStmt.setString(2, user.getName());
                        pStmt.setString(3, title);
                        pStmt.setString(4, room);
                        int insertResult = pStmt.executeUpdate();
                        if (insertResult > 0) {
                            // 성공적으로 신청 완료
                            System.out.println("수업 신청이 완료되었습니다.");
                            result = 0;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
        return result;
    }

    public List<ApplyVo> getAppliedClasses(UserVO user) {
        List<ApplyVo> appliedClasses = new ArrayList<>();
        try {
            conn = DbConn.getConnection();
            String sql = "";
            if(user.getId().equals("master")) sql = "SELECT * FROM APPLYUSER ORDER BY ROOM";
            else sql = "SELECT * FROM APPLYUSER WHERE ID = ?";
            pStmt = conn.prepareStatement(sql);
            if(!user.getId().equals("master")) pStmt.setString(1, user.getId());
            rs = pStmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ID");
                String name = rs.getString("NAME");
                String title = rs.getString("TITLE");
                String room = rs.getString("ROOM");
                appliedClasses.add(new ApplyVo(id, name, title, room));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
        return appliedClasses;
    }

    public int printAppliedClasses(List<ApplyVo> appliedClasses) {
        int i = 1;
        if (appliedClasses.isEmpty()) {
            System.out.println("신청한 수업이 없습니다.");
            return 1;
        } else {
            for (ApplyVo e : appliedClasses) {
                System.out.println((i++) + "- " + e.getTitle() + " (" + e.getRoom() + ")");
            }
            return 0;
        }
    }

    public void cancelAllAppliedClasses(ApplyVo avo) {
        try {
            conn = DbConn.getConnection();

            // APPLYUSER 테이블에서 해당 사용자의 모든 항목 삭제
            String deleteSql = "DELETE FROM APPLYUSER WHERE ID = ? AND TITLE = ?";
            pStmt = conn.prepareStatement(deleteSql);
            pStmt.setString(1, avo.getId());
            pStmt.setString(2, avo.getTitle());

            pStmt.executeUpdate();

            // CLASSTB 테이블의 해당 수업의 APPLICANT 수 감소
            String updateSql = "UPDATE ClassTb SET APPLICANT = APPLICANT - 1 WHERE TITLE = ?";
            pStmt = conn.prepareStatement(updateSql);
            pStmt.setString(1, avo.getTitle());
            pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
    }


}