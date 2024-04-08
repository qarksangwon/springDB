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
            if(userCheck.getVal1().length()>15){
                model.addAttribute("errorMessage", "없는 아이디 입니다.");
                return "user/signinCheck";
            }
            userVO = new UserVO(userCheck.getVal1(),userCheck.getVal2());
            rs = uDao.signIn(userVO);
            if(rs==1) {
                userVO =uDao.userInfo(userVO);
                session.invalidate();
                session.setAttribute("InUser",userVO);
            }
        }else{
            if(!(userCheck.getVal1().length()==13)||!userCheck.getVal1().contains("-")){
                model.addAttribute("errorMessage", "휴대폰 번호를 정확히 입력해 주세요.(예시 : 010-1234-1234)");
                return "user/signinCheck";
            }
            if(userCheck.getVal2().length()>10){
                model.addAttribute("errorMessage", "이름이 너무 깁니다."); 
                return "user/signinCheck";
            }
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
    public String signUpOK(@ModelAttribute("userInfo")UserVO user, Model model) throws SQLException{
        user.setId(user.getId().trim());
        if(user.getId().contains("-")){
            model.addAttribute("Message","아이디에 -이 들어갈 수 없습니다.");
            return "user/changeCheck";
        }
        if(user.getId().length()<4){
            model.addAttribute("Message","아이디는 4자리 이상 사용해야 합니다.");
            return "user/changeCheck";
        }
        int suRst = uDao.signUp(user);
        if(suRst == 1) return "redirect:/acos/main";
        else{
            model.addAttribute("Message","이미 사용중인 아이디 입니다.");
            return "user/changeCheck";
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
            return "user/fpCheck";
        }
    }


    @PostMapping("/fpCheck")
    public String findpasswordOK(@ModelAttribute("userInfo")UserVO user, Model model) throws  SQLException{
        UserVO infoCheck = uDao.userInfo(user);
        if(infoCheck.getAnswer().equals(user.getAnswer())){
            model.addAttribute("userInfo",user);
            model.addAttribute("newUser", new UserVO());
            return "user/passwordChange";
        }else{
            model.addAttribute("Message", "질문의 답변이 틀렸습니다.");
            return "user/changeCheck";
        }
    }
    @PostMapping("/pwChange")
    public String pwChange(@ModelAttribute("newUser")UserVO user,@ModelAttribute("pwString")String pw, Model model)throws SQLException{
        if(pw.length()>15){
            model.addAttribute("Message","사용할 수 없는 비밀번호 입니다.");
            return "user/changeCheck";
        }
        user.setPassword(pw);
        int rst = uDao.findPassword(user);
        if(rst == 0){
            model.addAttribute("Message","사용할 수 없는 비밀번호 입니다.");
            return "user/changeCheck";
        }else return "redirect:/acos/main";
    }


    @GetMapping("/logout")
    public String logout(){
        session.invalidate();
        return "redirect:/acos/main";
    }

}
