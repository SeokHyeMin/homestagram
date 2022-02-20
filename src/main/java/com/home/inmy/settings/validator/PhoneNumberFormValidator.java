package com.home.inmy.settings.validator;

import com.home.inmy.account.AccountRepository;
import com.home.inmy.settings.form.PhoneNumberForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PhoneNumberFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return PhoneNumberForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        PhoneNumberForm phoneNumberForm = (PhoneNumberForm) target;

        if(accountRepository.existsByPhoneNumber(phoneNumberForm.getPhoneNumber())){
            errors.rejectValue("phoneNumber","wrong.value","이미 사용중인 번호입니다.");
        }

    }
}
