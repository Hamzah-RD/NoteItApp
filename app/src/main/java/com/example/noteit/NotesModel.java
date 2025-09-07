package com.example.noteit;

public class NotesModel {
    private  String id;
    private String title;
    private String subtitle;
    private String noteText;
    private  String  dateTime;
    private  String uid;
    private String color;

    public NotesModel(String id, String title, String subtitle, String noteText, String dateTime, String uid, String color) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.noteText = noteText;
        this.dateTime = dateTime;
        this.uid = uid;
        this.color = "#FFFFFF";
    }



    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
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

    public String getsubtitle() {
        return subtitle;
    }

    public void setsubtitle(String subtitle) {
        this.subtitle = subtitle;
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
