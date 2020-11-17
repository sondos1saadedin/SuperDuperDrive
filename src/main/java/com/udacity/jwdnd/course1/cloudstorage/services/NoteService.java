package com.udacity.jwdnd.course1.cloudstorage.services;


import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public void addNote(Note note) {
       this.noteMapper.insert(note);
    }

    public List<Note> getUserNotes(Integer userId) {
        return this.noteMapper.getUserNotes(userId);
    }

    public int updateNote(Note note) {
        return this.noteMapper.updateUserNote(note);
    }

    public int deleteNote(int noteId) {
        return this.noteMapper.deleteUserNote(noteId);
    }
}
