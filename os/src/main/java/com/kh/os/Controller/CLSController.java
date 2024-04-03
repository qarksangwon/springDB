package com.kh.os.Controller;

import com.kh.os.cls.ClassDao;
import com.kh.os.cls.ClassVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/classtb")
public class CLSController {

    @GetMapping("/select")
    public String selectClasstb(Model model) {
        ClassDao dao = new ClassDao();
        List<ClassVo> cls = dao.ClassSelect();
        model.addAttribute("CLASS", cls);
        return "thymeleafEx/classSelect";
    }
}