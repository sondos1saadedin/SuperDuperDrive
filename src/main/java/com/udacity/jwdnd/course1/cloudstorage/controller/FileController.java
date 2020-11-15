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

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping("/upload")
    public String saveFile(@ModelAttribute("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) {
        try {
            fileService.saveFile(fileUpload, userService.getUser(authentication.getName()).getUserid());
        } catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("files", fileService.getUserFiles(userService.getUser(authentication.getName()).getUserid()));
        return "home";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> getFile(@PathVariable String id, Authentication authentication) {
        File fileDB = fileService.getUserFileByFileName(userService.getUser(authentication.getName()).getUserid(), id);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(fileDB.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getFileName() + "\"")
                .body(new ByteArrayResource(fileDB.getFileData()));
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable String id, Authentication authentication, Model model) {
        fileService.deleteUserFile(userService.getUser(authentication.getName()).getUserid(), id);
        model.addAttribute("files", fileService.getUserFiles(userService.getUser(authentication.getName()).getUserid()));
        return "home";
    }
}
