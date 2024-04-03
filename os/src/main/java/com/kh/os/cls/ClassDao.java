package com.kh.os.cls;


import com.kh.os.dbconn.DbConn;

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

    public void applyForClass(ClassVo cvo) {
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
//                if (isAlreadyApplied(userId)) {
//                    System.out.println("이미 해당 수업을 신청하셨습니다.");
//                    return; // 중복 신청이므로 메서드 실행 중단
//                }
                int currentApplicant = rs.getInt("APPLICANT");
                int max = rs.getInt("MAX");
                String title = rs.getString("TITLE");
                String room = rs.getString("ROOM");

                selectSql = "SELECT * FROM APPLYUSER WHERE ID = ? AND TITLE = ?";
                pStmt = conn.prepareStatement(selectSql);
                pStmt.setString(1, "test");
                pStmt.setString(2, title);
                rs = pStmt.executeQuery();
                if (rs.next()) {
                    System.out.println("이미 신청된 내역이 있습니다.");
                    DbConn.close(rs);
                    DbConn.close(pStmt);
                    DbConn.close(conn);
                    return;
                }
                // 현재 신청 인원이 최대 인원을 초과하지 않으면
                if (currentApplicant < max) {
                    // SQL 쿼리문 작성: 선택한 수업의 신청 인원을 1 증가시킴
                    String updateSql = "UPDATE ClassTb SET APPLICANT = APPLICANT + 1 WHERE TITLE = ?";
                    pStmt = conn.prepareStatement(updateSql);
                    pStmt.setString(1, cvo.getTitle()); // SQL 쿼리문의 ? 위치에 수업 번호를 설정
                    int result = pStmt.executeUpdate();

                    // 업데이트 결과에 따라 메시지 출력
                    if (result > 0) {
                        System.out.println("수업 신청이 완료되었습니다.");

                        // SQL 쿼리문 작성: USERAPPLY 테이블에 수업 신청 정보 추가
                        String insertSql = "INSERT INTO APPLYUSER (ID, NAME, TITLE, ROOM) VALUES (?, ?, ?, ?)";
                        pStmt = conn.prepareStatement(insertSql);
                        pStmt.setString(1, "test");
                        pStmt.setString(2, "개명한이름"); // 전달받은 userName 사용
                        pStmt.setString(3, title);
                        pStmt.setString(4, room);
                        pStmt.executeUpdate();
                    } else {
                        System.out.println("수업 신청에 실패했습니다. 다시 시도해주세요.");
                    }
                } else {
                    // 최대 인원을 초과한 경우 에러 메시지 출력
                    System.out.println("신청 인원이 초과되었습니다.");
                }
            } else {
                // 해당 수업이 존재하지 않는 경우 에러 메시지 출력
                System.out.println("해당 수업이 존재하지 않습니다.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 자원 해제
            DbConn.close(rs);
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
    }

    public List<ApplyVo> getAppliedClasses(String userId) {
        List<ApplyVo> appliedClasses = new ArrayList<>();
        try {
            conn = DbConn.getConnection();
            String sql = "SELECT * FROM APPLYUSER WHERE ID = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, userId);
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
                System.out.println((i++) + "- " + e.getTITLE() + " (" + e.getROOM() + ")");
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
            pStmt.setString(1, avo.getID());
            pStmt.setString(2, avo.getTITLE());

            pStmt.executeUpdate();

            // CLASSTB 테이블의 해당 수업의 APPLICANT 수 감소
            String updateSql = "UPDATE ClassTb SET APPLICANT = APPLICANT - 1 WHERE TITLE = ?";
            pStmt = conn.prepareStatement(updateSql);
            pStmt.setString(1, avo.getTITLE());
            pStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbConn.close(pStmt);
            DbConn.close(conn);
        }
    }


}