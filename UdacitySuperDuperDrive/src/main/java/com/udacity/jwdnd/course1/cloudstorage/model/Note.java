package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {
    Integer noteid;
    String noteTitle;
    String noteDescription;
    Integer userid;

    public Integer getNoteid() {
        return noteid;
    }

    public void setNoteId(Integer noteid) {
        this.noteid = noteid;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
