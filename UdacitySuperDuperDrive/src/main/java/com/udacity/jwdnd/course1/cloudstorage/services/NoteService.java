package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private NoteMapper noteMapper;
    private Logger logger= LoggerFactory.getLogger(NoteService.class);

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public Integer insertNote(Note note){
        return noteMapper.insertNote(note);
    }

    public List<Note> getNotesForUser(Integer userId){
        return noteMapper.getNotesForUser(userId);
    }

    public Integer updateNote(Note note){
        return noteMapper.updateNote(note);
    }

    public Integer deleteNote(Integer noteid){
        return noteMapper.deleteNote(noteid);
    }

    public Integer getLastNoteId() {return noteMapper.getLastNoteId();}

}
