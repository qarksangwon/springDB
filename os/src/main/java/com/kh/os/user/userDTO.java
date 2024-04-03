package com.kh.os.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userDTO {
    private String phoneNumber;
    private  String id;
    private String password;
    private String name;
    private String question;
    private String answer;

    public userDTO(String id, String password, String name, String question, String answer){
        this.id = id;
        this.password = password;
        this.name = name;
        this.question = question;
        this.answer = answer;
    }
    public userDTO(String phoneNumber, String name){
        this.phoneNumber = phoneNumber;
        this.name = name;
    }
}
