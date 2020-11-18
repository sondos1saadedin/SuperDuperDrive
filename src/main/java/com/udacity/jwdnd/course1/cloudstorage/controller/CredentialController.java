package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;
    private final HomeController homeController;


    public CredentialController(UserService userService, CredentialService credentialService, HomeController homeController) {
        this.userService = userService;
        this.credentialService = credentialService;
        this.homeController = homeController;
    }

    @PostMapping("/addOrUpdateCredential")
    public String addOrUpdateCredential(@ModelAttribute Credential credential, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        credential.setUserId(userId);
        int editedRowsNum = -1;
        if(credential.getCredentialId() != null) {
            editedRowsNum = credentialService.updateCredential(credential);
            if(editedRowsNum > 0) {
                model.addAttribute("credentialSuccessMsg", "Credential updated successfully.");
            } else {
                model.addAttribute("credentialErrorMsg", "Couldn't update the note, please try again!");
            }
        } else {
            editedRowsNum = credentialService.addCredential(credential);
            if(editedRowsNum > 0) {
                model.addAttribute("credentialSuccessMsg", "Credential added successfully.");
            } else {
                model.addAttribute("credentialErrorMsg", "Couldn't add the note, please try again!");
            }
        }

        homeController.setHomeInfo(model, userId);
        return "home";
    }


    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(@PathVariable String credentialId, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        if(credentialId != null) {
            int rowsDeletedNum = credentialService.deleteCredential(Integer.parseInt(credentialId));
            if(rowsDeletedNum > 0) {
                model.addAttribute("credentialSuccessMsg", "Credential deleted successfully.");
            } else {
                model.addAttribute("credentialErrorMsg", "Couldn't delete the note, please try again!");
            }
        }

        homeController.setHomeInfo(model, userId);
        return "home";
    }


    @RequestMapping(value = "/getDecryptedPassword" ,method = RequestMethod.POST)
    public @ResponseBody  String getDecryptedPassword(@RequestBody Credential credential) {
        return credentialService.getDecryptedPassword(credential.getPassword(), credential.getKey());
    }
}
