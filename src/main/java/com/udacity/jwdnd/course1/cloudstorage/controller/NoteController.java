package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class NoteController {
    private final NoteService noteService;
    private final UserService userService;
    private final HomeController homeController;

    public NoteController(NoteService noteService, UserService userService, HomeController homeController) {
        this.noteService = noteService;
        this.userService = userService;
        this.homeController = homeController;
    }

    @PostMapping("/addOrUpdateNote")
    public String addNote(@ModelAttribute Note note, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        note.setUserId(userId);
        int editedRowsNum = -1;
        if(note.getNoteId() != null) {
            editedRowsNum = noteService.updateNote(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userId));
            if(editedRowsNum > 0) {
                model.addAttribute("NoteSuccessMsg", "Note updated successfully.");
            } else {
                model.addAttribute("NoteErrorMsg", "Couldn't update the note, please try again!");
            }
        } else {
            editedRowsNum = noteService.addNote(note);
            if(editedRowsNum > 0) {
                model.addAttribute("NoteSuccessMsg", "Note added successfully.");
            } else {
                model.addAttribute("NoteErrorMsg", "Couldn't add the note, please try again!");
            }
        }

        homeController.setHomeInfo(model, userId);
        return "home";
    }


    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote(@PathVariable String noteId, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        if(noteId!= null) {
            int rowsDeletedNum = noteService.deleteNote(Integer.parseInt(noteId));
            if(rowsDeletedNum > 0) {
                model.addAttribute("NoteSuccessMsg", "Note deleted successfully.");
            } else {
                model.addAttribute("NoteErrorMsg", "Couldn't delete the note, please try again!");
            }
        }

        homeController.setHomeInfo(model, userId);
        return "home";
    }
}
