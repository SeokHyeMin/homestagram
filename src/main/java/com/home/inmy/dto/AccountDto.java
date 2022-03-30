package com.home.inmy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class AccountDto {

    private String nickname;
    private String dateToBirth;
    private String email;
    private String name;
    private String password;
    private String loginId;
    private String phoneNumber;
    private LocalDateTime joinedAt;
    private List<String> roles;
}
