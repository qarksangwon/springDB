package com.kh.os.cls;

import com.kh.os.cls.ClassDao;
import com.kh.os.cls.ClassVo;
import com.kh.os.user.UserVO;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/classtb")
public class CLSController {

    static final UserVO user = new UserVO("test","1234","테스트1","취미가 뭔가요?","운동");
    @GetMapping("/select")
    public String selectClasstb(Model model) {
        ClassDao dao = new ClassDao();
        List<ClassVo> cls = dao.ClassSelect();
        model.addAttribute("CLASS", cls);
        return "clsThymeleaf/classSelect";
    }


    @PostMapping("/apply")
    public String selectApply(@ModelAttribute("applys") ClassVo cvo) {
        ClassDao cDao = new ClassDao();
        int result = cDao.applyForClass(cvo, user);

        if (result == 0) {
            // 수업 신청이 완료된 경우
            return "clsThymeleaf/clsSelectRst";
        } else {
            // 수업 신청에 실패한 경우
            return "clsThymeleaf/clsSelectError";
        }
    }
    @PostMapping("/cancls")
    public String cancleApply(@ModelAttribute("cancles") ApplyVo cvo) {
        ClassDao oDao = new ClassDao();
        oDao.cancelAllAppliedClasses(cvo);
        return "clsThymeleaf/clsCancle";

    }

    @GetMapping("/myclass")
    public String showMyClasses(Model model) {
        ClassDao dao = new ClassDao();
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