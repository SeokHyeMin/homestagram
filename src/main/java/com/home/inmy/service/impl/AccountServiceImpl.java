package com.home.inmy.service.impl;

import com.home.inmy.domain.entity.Profile;
import com.home.inmy.domain.entity.Account;
import com.home.inmy.domain.entity.Role;
import com.home.inmy.mail.EmailMessage;
import com.home.inmy.mail.EmailService;
import com.home.inmy.repository.AccountRepository;
import com.home.inmy.form.AccountForm;
import com.home.inmy.form.ProfileForm;
import com.home.inmy.form.SignUpForm;
import com.home.inmy.repository.RoleRepository;
import com.home.inmy.service.AccountService;
import com.home.inmy.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;

    private final RoleService roleService;
    private final EmailService emailService;

    @Override
    public void createAccount(SignUpForm signUpForm) {
        //비밀번호 암호화하여 저장.
        signUpForm.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        Account account = modelMapper.map(signUpForm, Account.class);

        //권한 부여, 기본회원가입의 경우 USER
        Role role = roleService.createRoleIfNotFound("ROLE_USER", "사용자");
        account.setAccountRole(role);

        //현재 시간으로 가입일 지정
        account.setJoinedAt(LocalDateTime.now());

        //회원 저장.
        accountRepository.save(account);
    }

    @Override //회원 탈퇴
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override //프로필 수정
    public void updateProfile(Account account, ProfileForm profileForm) {

        account.setProfile(new Profile(profileForm.getBio(), profileForm.getUrl(), profileForm.getImage()));
        accountRepository.save(account);
    }

    @Override //비밀번호 수정
    public void updatePassword(Account account, String newPassword) {

        //비밀번호를 암호화하여 저장.
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Override   //계정 수정
    public void updateAccount(Account account, AccountForm accountForm) {

        account.setNickname(accountForm.getNickname());
        account.setPhoneNumber(accountForm.getPhoneNumber());
        account.setEmail(accountForm.getEmail());
        account.setLoginId(accountForm.getLoginId());
        accountRepository.save(account);

    }

    @Override
    public Account getAccount(String loginId) {return accountRepository.findByLoginId(loginId);}

    @Override   //회원 리스트
    public Page<Account> getAccountList(int page){
        PageRequest pageRequest = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));
        return accountRepository.findAll(pageRequest);
    }

    @Override   //회원 권한 수정
    public void updateAccountRole(Long id, String roleName) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("일치하는 계정이 없습니다."));
        Role role = roleRepository.findByRoleName(roleName);
        account.setAccountRole(role); //변경감지
    }

    @Override   //비밀번호 찾기
    public void findPassword(Account account) {

        //임시 비밀번호 생성.
        StringBuilder pw = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            pw.append((char) ((Math.random() * 26) + 97));
        }

        Context context = new Context(); //model과 비슷한 기능, 사용할 값을 넣어주도록 하자.
        context.setVariable("loginId", account.getLoginId());
        context.setVariable("message", "임시비밀번호가 발급되었습니다. 임시비밀번호로 로그인 해주세요.");
        context.setVariable("password", pw.toString());

        String message = templateEngine.process("mail/mailSend", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("Homestagram 임시비밀번호입니다.") //메일 제목
                .message(message)
                .build();

        emailService.sendEmail(emailMessage); //이메일 보내기

        //이메일 보낸 후에 임시비밀번호를 해당 계정의 비밀번호로 변경해주기.
        updatePassword(account, pw.toString());
    }
}
