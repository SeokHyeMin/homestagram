package com.home.inmy.validator;

import com.home.inmy.repository.AccountRepository;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.form.AccountForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class AccountFormValidator implements Validator {

    private final AccountRepository accountRepository;
    private static boolean nicknameChangeCheck = false;
    private static boolean loginIdChangeCheck = false;
    private static boolean emailChangeCheck = false;
    private static boolean phoneNumberChangeCheck = false;


    //변경하지 않고 넘어온 것 있는지 먼저 체크
    public static void check(Account account, AccountForm accountForm){


        //기존 닉네임과 변경할 닉네임이 다르면 변경이 일어난것, 같으면 닉네임은 변경하지 않겠다는 것.
        if(!(account.getNickname().equals(accountForm.getNickname()))){
            nicknameChangeCheck = true;
        }

        if(!(account.getLoginId().equals(accountForm.getLoginId()))){
            loginIdChangeCheck = true;
        }

        if(!(account.getEmail().equals(accountForm.getEmail()))){
            emailChangeCheck = true;
        }

        if(!(account.getPhoneNumber().equals(accountForm.getPhoneNumber()))){
            phoneNumberChangeCheck = true;
        }
    }

    public void checkInitialization(){

        nicknameChangeCheck = false;
        loginIdChangeCheck = false;
        emailChangeCheck = false;
        phoneNumberChangeCheck = false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AccountFormValidator.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        AccountForm accountForm = (AccountForm) target;

        String newNickname = accountForm.getNickname();
        String newLoginId = accountForm.getLoginId();
        String newEmail = accountForm.getEmail();
        String newPhoneNumber = accountForm.getPhoneNumber();

        //닉네임 변경이 일어났는데, 중복닉네임이라면 오류 메시지 출력.
        if(accountRepository.existsByNickname(newNickname) && nicknameChangeCheck){
            errors.rejectValue("nickname","wrong.value","이미 사용중인 닉네임입니다.");
        }

        if(accountRepository.existsByLoginId(newLoginId) && loginIdChangeCheck){
            errors.rejectValue("loginId","wrong.value","이미 사용중인 아이디입니다.");
        }

        if(accountRepository.existsByPhoneNumber(newPhoneNumber) && phoneNumberChangeCheck){
            errors.rejectValue("phoneNumber","wrong.value","이미 사용중인 번호입니다.");
        }

        if(accountRepository.existsByEmail(newEmail) && emailChangeCheck) {
            errors.rejectValue("email","wrong.value","이미 사용중인 이메일 닉네임입니다.");
        }
    }

}
