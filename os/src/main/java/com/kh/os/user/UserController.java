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
    public String signinOK(@ModelAttribute("userInfo")UserCheckVO userCheck, Model model) throws SQLException {
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
                System.out.println(session);

                return "redirect:/acos/main";
            case 2:
                System.out.println(rs);;
                model.addAttribute("errorMessage", "비밀번호가 틀렸습니다.");
                return "user/signinCheck";

            case 0:
                System.out.println(rs);
                if(nUserVO == null) model.addAttribute("errorMessage", "가입된 아이디가 없습니다.");
                else model.addAttribute("errorMessage", "가입된 번호에 이름이 다릅니다.");
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

    @GetMapping("/change")
    public String changeView(Model model) throws SQLException{
        UserVO inUser = (UserVO) session.getAttribute("InUser");
        model.addAttribute("userInfo",inUser);
        return "user/change";
    }
    @PostMapping("/change")
    public String changeOK(@ModelAttribute("userInfo")UserVO user, Model model) throws SQLException{
        int rst = uDao.updateUser(user);
        if(rst == 1) {
            session.invalidate();
            session.setAttribute("InUser", user);
            model.addAttribute("Message","변경이 완료됐습니다.");
        }
        else{
            model.addAttribute("Message","변경 불가능한 값이 들어있습니다.");
        }
        return "user/changeCheck";
    }

    @GetMapping("/withdraw")
    public String withDraw(){
        return "user/withdraw";
    }

    @PostMapping("/withdraw")
    public String withDrawOK() throws SQLException{
        UserVO user = (UserVO) session.getAttribute("InUser");
        uDao.withDraw(user);
        session.invalidate();
        return "redirect:/acos/main";
    }

    @GetMapping("/findpassword")
    public String findpasswordView(Model model){
        model.addAttribute("userID",new String());
        return "user/findpassword";
    }

    @PostMapping("/findpassword")
    public String findpasswordCheck(@ModelAttribute("userID")String userid, Model model) throws SQLException{
        UserVO user = new UserVO();
        user.setId(userid);
        UserVO userCheck = uDao.userInfo(user);
        if(userCheck == null){
            model.addAttribute("Message","없는 아이디 입니다.");
            return "user/changeCheck";
        }else {
            model.addAttribute("userInfo",userCheck);
            model.addAttribute("userCheck", new UserVO());
            return "user/fpCheck";
        }
    }


    @PostMapping("/fpCheck")
    public String findpasswordOK(@ModelAttribute("userInfo")UserVO user, Model model) throws  SQLException{
        UserVO infoCheck = uDao.userInfo(user);
        if(infoCheck.getAnswer().equals(user.getAnswer())){
            model.addAttribute("userInfo",user);
            return "user/passwordChange";
        }else{
            model.addAttribute("Message", "질문의 답변이 틀렸습니다.");
            return "user/changeCheck";
        }
    }

    @GetMapping("/logout")
    public String logout(){
        session.invalidate();
        return "redirect:/acos/main";
    }

}
