### SpringBoot - TeamProject
### 🏫 학원 운영 시스템
---

### 🎯 목적
- 학원 운영 시스템을 만들어 보면서<br>SpringBoot, Jdbc(oracle), thymeleaf를 사용해<br> Springboot Framework의 이해도를 높이는데 목적을 둔 프로젝트입니다.

<br>

### 👥 멤버구성
|<img src="https://avatars.githubusercontent.com/u/113305463?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/161570968?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/163942942?v=4" width="150" height="150"/>|<img src="https://avatars.githubusercontent.com/u/161571071?v=4" width="150" height="150"/>|
|:-:|:-:|:-:|:-:|
|박상원<br/>[@qarksangwon](https://github.com/qarksangwon)|김현근<br/>[@hyunkk1](https://github.com/hyunkk1)|안준영<br/>[@hojunahn](https://github.com/hojunahn)|김기주<br/>[@kkimkiju](https://github.com/kkimkiju)|
  - 박상원 (qarksangwon) : 회원관리 시스템, 운영자 시스템, DAO, VO, Cotroller, Thymeleaf, DB
  - 김기주 (kkimkiju) : 수강신청 시스템, 수강신청 DB, 수강신청 DAO, Controller , 수강신청 UI
  - 안준영 (hojunahn) : 상담 콘솔, UI (HTML, CSS)  
  - 김현근 (hyunkk1) : Q&A 콘솔
<br><br>


### 📌주요 기능
__회원 관리 시스템__ - 로그인/ 회원가입/ 비밀번호 찾기/ 회원 탈퇴/ 정보 수정/ 운영자 계정(특수 계정 추가 기능)

__상담 시스템__ - 상담 예약/ 상담 조회/ 상담 취소

__수강 신청 시스템__ - 수강 신청/ 신청 수강 조회/ 수강 취소

__Q&A 시스템__ - 자주하는 질문 출력/ 질문 입력/ 질문 확인/ 질문 삭제

<br>

### 🔧 개발 환경
프론트엔드 - `html`, `css`, `JavaScript`

백엔드 - `SpringBoot(2.7)`

데이터베이스 - `oracle`

템플릿 엔진 - `thymeleaf`


---

### 📜요구사항 
![image](https://github.com/qarksangwon/springDB/assets/113305463/b10e2dae-633c-47a1-9fe7-7375625626e8)

---

### 개념적 기능 설계 - DFD
😊 __DFD ( USER )__ 
![image](https://github.com/qarksangwon/springDB/assets/113305463/d9274b94-62e5-4977-9afa-a1590e9f4376)

요구사항 명세를 기반으로 구현한 데이터 흐름도

__💡특이사항__

  - 일반 사용자가 접속해서 __시스템을 이용할 때, 로그인이 필수__ 며 

  - __비회원 로그인은 최초 1회 휴대전화 번호랑 이름으로 자동 회원가입__, 그 이후엔 가입된 번호에 맞는 명의로 로그인 가능.

  - __수강 신청__ 은 __회원만 가능__ 하도록 설계

  - 비밀번호 찾기에서 __운영자 계정__ 을 찾으려고 하면 운영자 계정은 찾을 수 없다고 __오류 출력__

---

😎 __DFD (Master)__
![image](https://github.com/qarksangwon/springDB/assets/113305463/60535893-4d35-482f-98fb-71c6a5c72fc5)

__💡특이사항__

  - 시스템에서 지정한 __운영자 계정__ 으로 로그인 시, 기능이 다름.

  - 운영자 계정 로그인 시 __회원 탈퇴 불가능__

  - 수강신청 내역, 상담 신청 내역 모두 조회 가능, __강제 삭제 가능__

  - __Q&A 답변 달기, 내역 조회 가능__

---

### 논리적 기능설계 ( ERD )
![image](https://github.com/qarksangwon/springDB/assets/113305463/d27a08cf-48b8-432d-82dd-c362a85ccc51)
_FK에 CASCADE 설정해 회원이 탈퇴하면 자동으로 데이터가 사라지도록 설정_

---

___UI 구현 부분과 더욱 자세한 내용은 아래의 링크의 Notion 통해 확인할 수 있습니다!___

  <a href="https://www.notion.so/Spring-boot-jdbc-68818c41e7634e99b3d615e4057acce2">
    <img src="https://img.shields.io/badge/TeamProject-0000ff?style=for-the-badge&logo=notion&logoColor=#ECD53F">
  </a>
