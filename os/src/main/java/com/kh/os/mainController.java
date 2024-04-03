package com.kh.os;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/acos")
public class mainController {
    @GetMapping("/main")
    public String viewMain(){
        return "main/main";
    }
}
