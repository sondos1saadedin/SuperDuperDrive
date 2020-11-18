package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {
    private final FileService fileService;
    private final UserService userService;
    private final HomeController homeController;


    public FileController(FileService fileService, UserService userService, HomeController homeController) {
        this.fileService = fileService;
        this.userService = userService;
        this.homeController = homeController;
    }

    @PostMapping("/upload")
    public String saveFile(@ModelAttribute("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();

        if (StringUtils.isEmpty(fileUpload.getOriginalFilename())) {
            model.addAttribute("errorMsg", "Please choose file, and try again");
        } else {
            try {
                if (fileService.isFileNameAvailable(userId, fileUpload.getOriginalFilename())) {
                    fileService.saveFile(userId, fileUpload);
                    model.addAttribute("successMsg", "File uploaded successfully.");
                } else {
                    model.addAttribute("errorMsg", "Already this file name is existed, please choose another one, and try again.");
                }
            } catch (IOException e) {
                model.addAttribute("errorMsg", "Upload process failed for some reason, please try again!");
                e.printStackTrace();
            }
        }
        homeController.setHomeInfo(model, userId);
        return "home";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String id, Authentication authentication, Model model) {
        File fileDB = fileService.getUserFileByFileName(userService.getUser(authentication.getName()).getUserid(), id);

        if (fileDB == null) {
            model.addAttribute("errorMsg", "Download file failed, please try again!");
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDB.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFileName() + "\"")
                .body(new ByteArrayResource(fileDB.getFileData()));
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable String id, Authentication authentication, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        int rowCountEffected = fileService.deleteUserFile(userService.getUser(authentication.getName()).getUserid(), id);

        if (rowCountEffected < 0) {
            model.addAttribute("errorMsg", "Delete file failed, please try again!");
        } else {
            model.addAttribute("successMsg", "File deleted successfully.");
        }

        homeController.setHomeInfo(model, userId);
        return "home";
    }
}
