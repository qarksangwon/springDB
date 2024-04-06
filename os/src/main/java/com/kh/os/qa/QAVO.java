package com.kh.os.qa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QAVO {
    private String id="";
    private String phonenumber ="";
    private String question ="";
    private String answer ="";
    public QAVO(String val1, String question, String answer) {
        if(val1.contains("-")){
            this.phonenumber = val1;
        }else{
            this.id = val1;
        }
        this.question = question;
        this.answer = answer;
    }

}
