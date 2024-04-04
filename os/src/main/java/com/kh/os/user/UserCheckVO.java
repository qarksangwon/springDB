package com.kh.os.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCheckVO {
    private String val1 = "";  // id, phoneNumber
    private String val2 = "";  // password , name
    private String userCheck = "";
}
