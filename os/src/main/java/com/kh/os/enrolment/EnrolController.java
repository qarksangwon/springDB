//package com.kh.os.enrolment;
//
//import com.kh.os.user.UserVO;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpSession;
//import java.sql.SQLException;
//
//@Controller
//@RequestMapping("/enrolment")
//public class EnrolController {
//
//    private final EnrolmentDao enrolmentDao;
//
//    public EnrolController(EnrolmentDao enrolmentDao) {
//        this.enrolmentDao = enrolmentDao;
//    }
//
//    @GetMapping("/reservation")
//    public String showReservationForm(Model model, HttpSession session) {
//        UserVO user = (UserVO) session.getAttribute("user");
//        if (user == null) {
//            // 로그인되지 않은 경우 예약 폼을 보여주지 않음
//            return "redirect:/login"; // 로그인 페이지로 리다이렉트
//        }
//        model.addAttribute("user", user);
//        return "reservation_form";
//    }
//
//    @PostMapping("/reservation")
//    public String makeReservation(Model model, HttpSession session, String date, String phoneNumber, String inquiry) {
//        UserVO user = (UserVO) session.getAttribute("user");
//        if (user == null) {
//            return "redirect:/login"; // 로그인 페이지로 리다이렉트
//        }
//        try {
//            int result = enrolmentDao.insertEnr(user, date, inquiry);
//            if (result > 0) {
//                return "reservation_success";
//            } else {
//                model.addAttribute("error", "이미 해당 날짜에 예약이 있습니다.");
//                return "reservation_form";
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            model.addAttribute("error", "예약에 실패했습니다. 잠시 후 다시 시도해주세요.");
//            return "reservation_form";
//        }
//    }
//}