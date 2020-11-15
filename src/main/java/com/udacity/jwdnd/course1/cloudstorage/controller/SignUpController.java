package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String signupView() {
        return "signup";
    }

    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model) {
        String signupErrorMsg = null;

        if(!userService.isUsernameAvailable(user.getUsername())) {
            signupErrorMsg = "There is already an account with this username";
        } else {
            int rowsAdded = userService.createUser(user);
            if(rowsAdded < 0 ) {
                signupErrorMsg = "Error - Registration process has failed for some reason, please try again";
            }
        }

        if (signupErrorMsg == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupErrorMsg);
        }

        return "signup";
    }
}
