package com.kh.os.cls;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassVo {
    private int openNum; // 수업 번호
    private String title; // 수업 이름
    private String teacher; // 강사 이름
    private String room; // 강의장
    private int applicant; // 신청 인원
    private int max; // 제한 인원
    private String applicantId; // 신청자 ID

    // 생성자 추가
    public ClassVo(int openNum, String title, String teacher, String room, int applicant, int max) {
        this.openNum = openNum;
        this.title = title;
        this.teacher = teacher;
        this.room = room;
        this.applicant = applicant;
        this.max = max;
    }
}