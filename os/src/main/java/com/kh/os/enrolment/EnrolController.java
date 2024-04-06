package com.kh.os.enrolment;
import com.kh.os.user.NotUserVO;
import com.kh.os.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
@RequestMapping("/acos")
public class EnrolController{
    static EnrolmentDao eDao = new EnrolmentDao();
    @Autowired
    HttpSession session;

    @GetMapping("/enrolment")
    public String enrMain(Model model){
        if(session.getAttribute("InUser")==null &&
        session.getAttribute("InNotUser")==null){
            session.invalidate();
            return "redirect:/acos/signin";
        }
        return "enrolment/enrolment";
    }

    @GetMapping("/reservation")
    public String reservationView(Model model){
        model.addAttribute("date",new String());
        model.addAttribute("time",new String());
        model.addAttribute("inquiry",new String());
        return "enrolment/reservationPage";
    }

    @PostMapping("/reservation")
    public String reservation(@ModelAttribute("date")String date,@ModelAttribute("time")String time,
                              @ModelAttribute("inquiry")String inquiry,Model model)throws SQLException {
        if(date.equals("")){
            model.addAttribute("Message","날짜를 선택해 주세요");
            return "enrolment/enrOK";
        }
        String[] dateArr = date.split("-");
        if(Integer.parseInt(dateArr[0])>2999){
            model.addAttribute("Message","3000년도 이후에 오시게요?");
            return "enrolment/enrOK";
        }
        String enrDate = date+"-"+time;
        UserVO user = (UserVO) session.getAttribute("InUser");
        NotUserVO nUser = (NotUserVO) session.getAttribute("InNotUser");
        EnrolmentVo enr;
        int rst = 0;
        if(user != null){
            enr = new EnrolmentVo(user.getId(),user.getName(),enrDate,inquiry);
            rst = eDao.insertEnr(user,enr);
        }else if(nUser != null){
            enr = new EnrolmentVo(nUser.getPhoneNumber(), nUser.getName(),enrDate,inquiry);
            rst = eDao.insertEnr(nUser,enr);
        }
        if(rst == 1){
            model.addAttribute("Message","상담 신청 완료.");
            return "enrolment/enrOK";
        }else{
            model.addAttribute("Message","이미 신청한 사람이 있습니다.");
            return "enrolment/enrOK";
        }
    }

}