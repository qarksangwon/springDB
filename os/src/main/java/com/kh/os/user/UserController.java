package com.kh.os.user;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;

@Controller
@RequestMapping("/acos")
public class UserController {

    @GetMapping("/signin")
    public String signinView(Model model){
        model.addAttribute("userInfo", new UserVO());
        return "user/signin";
    }

    @PostMapping("/signin")
    public String signinOK(@ModelAttribute("userInfo")UserVO user) throws SQLException {
        UserDAO uDao = new UserDAO();
        int rs = uDao.signIn(user);
        return "user/signinRst";

    }

}
