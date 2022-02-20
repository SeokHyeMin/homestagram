package com.home.inmy.settings.validator;

import com.home.inmy.account.AccountRepository;
import com.home.inmy.settings.form.LoginIdForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class LoginIdFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginIdForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LoginIdForm loginIdForm = (LoginIdForm) target;

        if(accountRepository.existsByLoginId(loginIdForm.getLoginId())){
            errors.rejectValue("loginId","wrong.value","이미 사용중인 아이디입니다.");
        }

    }
}
