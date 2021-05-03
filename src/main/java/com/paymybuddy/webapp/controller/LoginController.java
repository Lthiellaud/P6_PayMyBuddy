package com.paymybuddy.webapp.controller;


import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.service.PMBUserService;
import com.paymybuddy.webapp.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@Controller
public class LoginController {

    @Autowired
    private PMBUserService pmbUserService;


    @GetMapping("/home")
    public String getUser(Model model)
    {
        PMBUser user = pmbUserService.getCurrentUser();
        model.addAttribute("welcome", pmbUserService.getWelcomeMessage(user));
        model.addAttribute("balance", pmbUserService.getBalanceMessage(user));
        //System.out.println(welcome);
        return "homePage";
    }

   @GetMapping("/login")
    public String login(Model model) {
        return "loginPage";
    }

    @GetMapping("/")
    public String enter(Model model) {
        return "sitePage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            User loginUser = (User) ((Authentication) principal).getPrincipal();

            String userInfo = UserUtil.toString(loginUser);

            model.addAttribute("userInfo", userInfo);

            String message = "Hello " + principal.getName()  //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);

        }

        return "403Page";
    }
}
