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

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping("/addOrUpdateNote")
    public String addNote(@ModelAttribute Note note, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        note.setUserId(userId);
        if(note.getNoteId() != null) {
            noteService.updateNote(new Note(note.getNoteId(), note.getNoteTitle(), note.getNoteDescription(), userId));
        } else {
            noteService.addNote(note);
        }

        model.addAttribute("notes", noteService.getUserNotes(userId));
        return "home";
    }


    @GetMapping("/deleteNote/{noteId}")
    public String deleteNote(@PathVariable String noteId, Model model, Authentication authentication) {
        Integer userId = userService.getUser(authentication.getName()).getUserid();
        if(noteId!= null) {
            noteService.deleteNote(Integer.parseInt(noteId));
        }

        model.addAttribute("notes", noteService.getUserNotes(userId));
        return "home";
    }
}
