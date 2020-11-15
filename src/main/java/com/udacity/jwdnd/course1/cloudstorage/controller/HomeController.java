package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Controller
//@RequestMapping("/home")
public class HomeController {
    private final FileService fileService;
    private final UserService userService;

    public HomeController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String retrieveFiles(Model model, Authentication authentication) {
        model.addAttribute("files", fileService.getUserFiles(userService.getUser(authentication.getName()).getUserid()));
        return "home";
    }


    @PostMapping("/upload")
    public String saveFile(@ModelAttribute("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) {
        String fileName = StringUtils.cleanPath(fileUpload.getOriginalFilename());

        // save the file on the local file system
        try {
            fileService.saveFile(fileUpload, userService.getUser(authentication.getName()).getUserid());

        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("files", fileService.getUserFiles(userService.getUser(authentication.getName()).getUserid()));
        return "home";
    }




    @GetMapping("/download/{id}")
    public ResponseEntity getFile(@PathVariable String id, Authentication authentication) {
        File fileDB = fileService.getUserFileByFileName(userService.getUser(authentication.getName()).getUserid(), id);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDB.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFileName() + "\"")
                .body(new ByteArrayResource(fileDB.getFileData()));
    }

}
