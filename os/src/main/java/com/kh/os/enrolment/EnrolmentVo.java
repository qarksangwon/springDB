package com.kh.os.enrolment;


    public class EnrolmentVo {
        private String name; // 사용자 이름
        private String date; // 상담 예약을 하는 해당 날짜
        private String phoneNumber; // 사용자 전화번호
        private String inquiry; // 문의 내용
        private String id;



        public EnrolmentVo(String name, String date, String phoneNumber, String inquiry, String id) {
            this.name = name;
            this.date = date;
            this.phoneNumber = phoneNumber;
            this.inquiry = inquiry;
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getInquiry() {
            return inquiry;
        }

        public void setInquiry(String inquiry) {
            this.inquiry = inquiry;
        }
        public String getId() {return id;}

        public void setId(String id) {this.id = id;}
    }


