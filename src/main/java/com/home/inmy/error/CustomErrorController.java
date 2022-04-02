package com.home.inmy.error;


import com.home.inmy.domain.CurrentUser;
import com.home.inmy.domain.entity.Account;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest, @CurrentUser Account account, Model model){
        Integer statusCode = (Integer) httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if(statusCode != null){
            model.addAttribute("account",account);
            if(statusCode == HttpStatus.NOT_FOUND.value()){ //404에러 일 경우
                return "error/404";
            }else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()){ //500에러일 경우
                return "error/500";
            }
        }

        return "error/404";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
