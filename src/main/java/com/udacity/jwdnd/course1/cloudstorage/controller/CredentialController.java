package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    private final UserService userService;
    private final CredentialService credentialService;

    public CredentialController(UserService userService, CredentialService credentialService) {
        this.userService = userService;
        this.credentialService = credentialService;
    }

    @PostMapping("/addOrUpdateCredential")
    public String addOrUpdateCredential(@ModelAttribute Credential credential, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        credential.setUserId(userId);
        if(credential.getCredentialId() != null) {
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(credential);
        }

        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        return "home";
    }


    @GetMapping("/deleteCredential/{credentialId}")
    public String deleteCredential(@PathVariable String credentialId, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        if(credentialId!= null) {
            credentialService.deleteCredential(Integer.parseInt(credentialId));
        }

        model.addAttribute("credentials", credentialService.getUserCredentials(userId));
        return "home";
    }


    @RequestMapping(value = "/getDecryptedPassword" ,method = RequestMethod.POST)
    public @ResponseBody  String getDecryptedPassword(@RequestBody Credential credential) {
        return credentialService.getDecryptedPassword(credential.getPassword(), credential.getKey());
    }
}
