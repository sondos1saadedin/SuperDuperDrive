package com.udacity.jwdnd.course1.cloudstorage.controller;


import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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


    /**
     * Register new user to the application
     */
    @PostMapping
    public String registerUser(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupErrorMsg = null;

        // check if the username is available in the system
        if(!userService.isUsernameAvailable(user.getUsername())) {
            signupErrorMsg = "There is already an account with this username";
        } else {
            int rowsAdded = userService.createUser(user);
            if(rowsAdded < 0 ) {
                signupErrorMsg = "Error - Registration process has failed for some reason, please try again";
            }
        }

        if (signupErrorMsg == null) {
            // redirect the user to the login page as he successfully signed up.
            redirectAttributes.addFlashAttribute("SuccessMessage","Sign Up Successfully");
            return "redirect:/login";
        } else {
            model.addAttribute("signupError", signupErrorMsg);
        }

        return "signup";
    }
}
