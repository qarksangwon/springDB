package com.kh.os.user;


import org.apache.catalina.User;
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
public class UserController {
    static UserDAO uDao = new UserDAO();
    @Autowired
    private HttpSession session;

    @GetMapping("/signin")
    public String signinView(Model model){
        model.addAttribute("userInfo", new UserCheckVO());
        return "user/signin";
    }

    @PostMapping("/signin")
    public String signinOK(@ModelAttribute("userInfo")UserCheckVO userCheck) throws SQLException {
        UserVO userVO = null;
        NotUserVO nUserVO = null;
        int rs;
        if(userCheck.getUserCheck().equals("0")){
            userVO = new UserVO(userCheck.getVal1(),userCheck.getVal2());
            rs = uDao.signIn(userVO);
            if(rs==1) {
                userVO =uDao.userInfo(userVO);
                session.setAttribute("InUser",userVO);
            }
        }else{
            nUserVO = new NotUserVO(userCheck.getVal1(),userCheck.getVal2());
            rs = uDao.signIn(nUserVO);
            if(rs==1) {
                nUserVO = uDao.userInfo(nUserVO);
                session.setAttribute("InNotUser",nUserVO);
            }
        }
        switch (rs){
            case 1:
                System.out.println(rs);

                return "redirect:/acos/main";
            case 2:
                System.out.println(rs);
                System.out.println("비밀번호 틀림");
                return "user/signinCheck";
            case 0:
                System.out.println(rs);
                if(nUserVO == null) System.out.println("아이디 없음");
                else System.out.println("이름이 다름");
                return "user/signinCheck";
            default:
                return "main/main";
        }
    }

    @GetMapping("/signup")
    public String signUpView(Model model){
        model.addAttribute("userInfo",new UserVO());
        return "user/signUp";
    }

    @PostMapping("/signup")
    public String signUpOK(@ModelAttribute("userInfo")UserVO user) throws SQLException{
        int suRst = uDao.signUp(user);
        if(suRst == 1) return "redirect:/acos/main";
        else{
            return "redirect:/acos/signup";
        }
    }

    @GetMapping("/logout")
    public String logout(){
        session.removeAttribute("InUser");
        session.removeAttribute("InNotUser");
        return "redirect:/acos/main";
    }

}
