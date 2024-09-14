package com.example.finalproject;

public class Note {
    private Integer noteId;
    private String title;
    private String content;
    private String categoryName;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int not) {
        this.noteId = not;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String tit) {
        this.title = tit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String con) {
        this.content = con;
    }

}
