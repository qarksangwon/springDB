package com.kh.os.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private  String id = "";
    private String password = "";
    private String name = "";
    private String question = "";
    private String answer = "";


}
