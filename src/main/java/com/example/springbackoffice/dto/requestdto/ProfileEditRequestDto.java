package com.example.springbackoffice.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEditRequestDto {
    private String password;
    private String changepassword;
    private String selfIntroduction;
}
