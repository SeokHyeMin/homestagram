package com.home.inmy.controller;

import com.home.inmy.service.impl.AccountServiceImpl;
import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AccountServiceImpl accountService;

    @GetMapping("/admin")
    public String managementAccountView(Model model, @CurrentUser Account account,
                                        @RequestParam(required = false, defaultValue = "false") String pageSelect,
                                        @RequestParam(required = false, defaultValue = "0", value = "page")int page){

        Page<Account> accountList = accountService.getAccountList(page);

        Map<String, Integer> map = getPage(accountList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("accountList",accountList);
        model.addAttribute("account",account);

        if(pageSelect.equals("true")){ //페이지를 눌렀다면 table만 바뀌도로
            return "management/account :: #account-table";
        }

        //페이지를 누른게 아닌 처음 admin페이지 들어왔다면 페이지 자체를 바꿔줌.
        return "management/account";

    }

    @PostMapping("/admin/account/delete")
    public String managementAccountDelete(Model model, @CurrentUser Account account, @RequestParam Long accountId,
                                          @RequestParam(required = false, defaultValue = "0", value = "page")int page){

        accountService.deleteAccount(accountId); //해당계정 삭제

        Page<Account> accountList = accountService.getAccountList(page);

        Map<String, Integer> map = getPage(accountList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("accountList",accountList);
        model.addAttribute("account",account);

        return "management/account :: #account-table";

    }

    @PostMapping("/admin/account/role")
    public String accountRoleUpdate(Model model, @CurrentUser Account account, @RequestParam Long accountId,
                                          @RequestParam String roleName,
                                          @RequestParam(required = false, defaultValue = "0", value = "page")int page){

        accountService.updateAccountRole(accountId, roleName); //권한 변경

        Page<Account> accountList = accountService.getAccountList(page);

        Map<String, Integer> map = getPage(accountList); //페이지 계산

        model.addAttribute("startBlockPage", map.get("startBlockPage"));
        model.addAttribute("endBlockPage", map.get("endBlockPage"));
        model.addAttribute("accountList",accountList);
        model.addAttribute("account",account);

        return "management/account :: #account-table";

    }

    private Map<String, Integer> getPage(Page<Account> list){ //페이지 계산하여 시작블럭, 마지막 블럭 담아 반환.

        Map<String, Integer> map = new HashMap<>();

        int pageNum = list.getPageable().getPageNumber(); // 현재 페이지
        int pageBlock = 5; // 블럭의 수
        int startBlockPage = (pageNum / pageBlock) * pageBlock + 1;
        int endBlockPage = startBlockPage + pageBlock - 1;
        int totalPage = list.getTotalPages();
        endBlockPage = Math.min(totalPage, endBlockPage);

        map.put("startBlockPage",startBlockPage);
        map.put("endBlockPage",endBlockPage);

        return map;
    }
}
