package com.example.noteit;

public class NotesModel {
    private  String id;
    private String title;
    private String subtiltel;
    private String noteText;
    private  String  dateTime;
    private  String uid;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public NotesModel(String id, String title, String subtiltel, String noteText, String dateTime, String uid) {
        this.id = id;
        this.title = title;
        this.subtiltel = subtiltel;
        this.noteText = noteText;
        this.dateTime = dateTime;
        this.uid = uid;
    }

    public NotesModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtiltel() {
        return subtiltel;
    }

    public void setSubtiltel(String subtiltel) {
        this.subtiltel = subtiltel;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }


}
