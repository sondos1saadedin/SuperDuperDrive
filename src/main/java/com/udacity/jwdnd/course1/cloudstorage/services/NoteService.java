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

    // insert new Note to the DB.
    public int addNote(Note note) {
       return this.noteMapper.insert(note);
    }

    // return the user's notes by user id.
    public List<Note> getUserNotes(Integer userId) {
        return this.noteMapper.getUserNotes(userId);
    }

    // update the note data
    public int updateNote(Note note) {
        return this.noteMapper.updateUserNote(note);
    }

    // delete the note from the DB
    public int deleteNote(int noteId) {
        return this.noteMapper.deleteUserNote(noteId);
    }
}
