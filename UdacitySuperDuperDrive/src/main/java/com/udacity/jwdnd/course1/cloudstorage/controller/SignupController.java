package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;
    private Logger logger = LoggerFactory.getLogger(SignupController.class);
    public static final String SIGNUP_ERR = "Sign Failed, Please try again";
    public static final String SIGNUP_USER_EXISTING_ERR ="User name already exists, Please enter different username";
    public static final String SIGNUP_SUCCESS ="Sign up is successful! Welcome ";

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showSignUpPage(@ModelAttribute("User") User user,  Model model){
        return "signup";
    }

    @PostMapping
    public String signUpNewUser(@ModelAttribute("User") User user, Model model, RedirectAttributes redirectAttributes){
        userService.hashUserPassword(user);


        String signup_err = null;
        if(!userService.isUsernameAvailable(user.getUserName()))
            signup_err=SIGNUP_USER_EXISTING_ERR;

        if(signup_err==null){
           int rowAdded=  userService.insertUser(user);
           if(rowAdded<0)
               signup_err =SIGNUP_ERR;
        }

        if(signup_err==null){
            redirectAttributes.addAttribute("isSuccess",true);
            redirectAttributes.addAttribute("signupMsg",SIGNUP_SUCCESS+user.getUserName());
            return "redirect:/login";
        }
        else{
            model.addAttribute("isFailure",true);
            model.addAttribute("signupMsg",signup_err);
            }
       return "signup";
    }
}
