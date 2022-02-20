package com.home.inmy.settings.validator;

import com.home.inmy.account.AccountRepository;
import com.home.inmy.settings.form.EmailForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class EmailFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return EmailForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        EmailForm emailForm = (EmailForm) target;

        if(accountRepository.existsByEmail(emailForm.getEmail())){
            errors.rejectValue("email","wrong.value","이미 이메일 닉네임입니다.");
        }

    }
}
