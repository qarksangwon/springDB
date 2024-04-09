package com.kh.os.qa;

import com.kh.os.user.NotUserVO;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/acos")
public class QAController {
    QADAO qDao = new QADAO();
    @Autowired
    HttpSession session;

    @GetMapping("/qapage")
    public String qaMain(Model model) {
        UserVO user = (UserVO) session.getAttribute("InUser");
        NotUserVO nUser = (NotUserVO) session.getAttribute("InNotUser");
        if (user == null) {
            if (nUser == null) {
                session.invalidate();
                return "redirect:/acos/signin";
            } else {
                model.addAttribute("user", nUser.getPhoneNumber());
                return "qa/qaMain";
            }
        } else {
            model.addAttribute("user", user.getId());
            return "qa/qaMain";
        }
    }

    @GetMapping("/questionPage")
    public String questionView(Model model){
        model.addAttribute("qaInfo",new QAVO());
        return "qa/questionPage";
    }

    @PostMapping("/questionPage")
    public String questionInput(@ModelAttribute("qaInfo")QAVO qa, Model model) throws SQLException {
        UserVO user = null;
        NotUserVO nUser = null;
        List<QAVO> currentList = null;
        user = (UserVO)session.getAttribute("InUser");
        nUser = (NotUserVO)session.getAttribute("InNotUser");
        qa.setQuestion(qa.getQuestion().trim());
        if(user != null) {
            qa.setId(user.getId());
            currentList = qDao.loadQA(user);
            for(QAVO qCheck : currentList){
                if(qCheck.getQuestion().equals(qa.getQuestion())){
                    model.addAttribute("Message","이미 등록한 질문입니다.");
                    return "qa/qaOK";
                }
            }
        }
        else {
            qa.setPhonenumber(nUser.getPhoneNumber());
            currentList = qDao.loadQA(nUser);
            for(QAVO qCheck : currentList){
                if(qCheck.getQuestion().equals(qa.getQuestion())){
                    model.addAttribute("Message","이미 등록한 질문입니다.");
                    return "qa/qaOK";
                }
            }
        }
        if(qa.getQuestion() == ""){
            model.addAttribute("Message","공백입니다.");
            return "qa/qaOK";
        }
        qa.setQuestion(qa.getQuestion().replace("\r\n",""));
        qa.setQuestion(qa.getQuestion().replace("\r",""));
        qa.setQuestion(qa.getQuestion().replace("\n",""));
        qa.setQuestion(qa.getQuestion().replace("\t",""));

        int rst = qDao.insertQA(qa);
        if(rst == 1){
            model.addAttribute("Message", "질문이 등록됐습니다.");
            return "qa/qaOK";
        }else{
            model.addAttribute("Message","질문이 등록되지 않았습니다.");
            return "qa/qaOK";
        }
    }

    @GetMapping("/qaView")
    public String qaList(Model model) throws SQLException{
        UserVO user = (UserVO)session.getAttribute("InUser");
        NotUserVO nUser = (NotUserVO)session.getAttribute("InNotUser");
        List<QAVO> qaList = null;
        if(user != null) {
            if(user.getId().equals("master")) {
                qaList = qDao.loadQA(user);
                model.addAttribute("qaList",qaList);
                return "qa/masterQA_page";
            }
            qaList = qDao.loadQA(user);
        }
        else if(nUser != null) qaList = qDao.loadQA(nUser);
        else qaList = new ArrayList<QAVO>();
        model.addAttribute("qaList",qaList);
        return "qa/userQA_page";
    }

    @PostMapping("/qaView")
    public String qaDel(@ModelAttribute("qaDel")QAVO qaDel, Model model) throws SQLException{
        UserVO user = (UserVO)session.getAttribute("InUser");
        NotUserVO nUser = (NotUserVO)session.getAttribute("InNotUser");
        if(user != null) qaDel.setId(user.getId());
        else if(nUser != null) qaDel.setPhonenumber(nUser.getPhoneNumber());
        int rst = qDao.deleteQA(qaDel);
        if(rst > 0) {
            model.addAttribute("Message", "질문이 삭제됐습니다.");
            return "qa/qaOK";
        }else{
            model.addAttribute("Message","질문이 삭제되지 않았습니다.");
            return "qa/qaOK";
        }
    }

    @PostMapping("/qaMaster")
    public String answerInput(@ModelAttribute("questionStr")String qStr,@ModelAttribute("IOP")String vStr,Model model){
        model.addAttribute("u",vStr);
        model.addAttribute("q",qStr);
        model.addAttribute("a",new String());
        return "qa/answerPage";
    }

    @PostMapping("/answer")
    public String answerOK(@ModelAttribute("q")String q,@ModelAttribute("u")String u,@ModelAttribute("a")String a, Model model)throws SQLException{
        QAVO qa = new QAVO(u,q,a);
        int rst = qDao.updateQA(qa);
        if(rst == 1){
            model.addAttribute("Message", "답변 작성 완료.");
            return "qa/qaOK";
        }else{
            model.addAttribute("Message","답변 작성 실패.");
            return "qa/qaOK";
        }
    }

    @GetMapping("/answerList")
    public String answerList(Model model)throws SQLException{
        List<QAVO> answerList = qDao.loadMasterAnswer();
        model.addAttribute("answerList",answerList);
        return "qa/masterAnswer_page";
    }

}
