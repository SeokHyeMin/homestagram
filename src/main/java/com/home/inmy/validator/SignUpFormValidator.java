package com.home.inmy.validator;

import com.home.inmy.repository.AccountRepository;
import com.home.inmy.form.SignUpForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(SignUpForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpForm signUpForm = (SignUpForm) target;
            
        if(accountRepository.existsByEmail(signUpForm.getEmail())){
                errors.rejectValue("email","invalid.email", new Object[]{signUpForm.getEmail()}, "이미 사용중인 이메일입니다.");
        }

        if(accountRepository.existsByLoginId(signUpForm.getLoginId())){
                errors.rejectValue("loginId","invalid.loginId",new Object[]{signUpForm.getLoginId()},"이미 사용중인 아이디입니다.");
        }

        if(accountRepository.existsByNickname(signUpForm.getNickname())){
                errors.rejectValue("nickName","invalid.nickName",new Object[]{signUpForm.getNickname()},"이미 사용중인 닉네임입니다.");
        }

        if(accountRepository.existsByPhoneNumber(signUpForm.getPhoneNumber())){
                errors.rejectValue("phoneNumber","invalid.phoneNumber",new Object[]{signUpForm.getPhoneNumber()},"이미 사용중인 번호입니다.");
        }
    }
}
