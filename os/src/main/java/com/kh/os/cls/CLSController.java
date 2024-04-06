package com.kh.os.cls;

import com.kh.os.cls.ClassDao;
import com.kh.os.cls.ClassVo;
import com.kh.os.user.UserVO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/acos")
public class CLSController {

    @Autowired
    private HttpSession session;
    @GetMapping("/select")
    public String selectClasstb(Model model) {
        Object sessionVal = session.getAttribute("InUser");
        if(sessionVal == null) {
            sessionVal = session.getAttribute("InNotUser");
            if(sessionVal == null) {
                session.invalidate();
                return "redirect:/acos/signin";
            }
            else{
                model.addAttribute("errorMessage","로그인 후 이용 가능합니다.");
                return "user/signinCheck";
            }
        }
        ClassDao dao = new ClassDao();
        List<ClassVo> cls = dao.ClassSelect();
        model.addAttribute("CLASS", cls);
        return "clsThymeleaf/classSelect";
    }


    @PostMapping("/apply")
    public String selectApply(@ModelAttribute("applys") ClassVo cvo) {
        ClassDao cDao = new ClassDao();
        UserVO user = (UserVO) session.getAttribute("InUser");
        if(user.getId().equals("master")) return "redirect:/acos/select";
        int result = cDao.applyForClass(cvo, user);

        if (result == 0) {
            // 수업 신청이 완료된 경우
            return "clsThymeleaf/clsSelectRst";
        } else {
            // 수업 신청에 실패한 경우
            return "clsThymeleaf/clsSelectError";
        }
    }

    @GetMapping("/myclass")
    public String showMyClasses(Model model) {
        ClassDao dao = new ClassDao();
        UserVO user = (UserVO) session.getAttribute("InUser");
        List<ApplyVo> myclasses = dao.getAppliedClasses(user);
        model.addAttribute("myclasses",myclasses);
        return "clsThymeleaf/myclass";
    }

    @PostMapping("/myclass")
    public String classCancel(@ModelAttribute("id") String classId, @ModelAttribute("title") String classTitle){
        ApplyVo avo = new ApplyVo();
        avo.setId(classId);
        avo.setTitle(classTitle);
        ClassDao dao = new ClassDao();
        dao.cancelAllAppliedClasses(avo);
        return "clsThymeleaf/clsCancle";
    }


}