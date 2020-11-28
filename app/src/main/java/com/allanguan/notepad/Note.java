package com.allanguan.notepad;

import androidx.annotation.NonNull;

import java.io.Serializable;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Note implements Serializable {

    private String title;
    private String notes;
    private String truncatedNote;
    private String noteDate;
    ;


    Note(String t, String n){
        this.title = t;
        this.notes = n;
        updateDate();
        updateTruncated();
    }


    public String getTitle(){
        return title;
    }
    public String getNotes(){
        return notes;
    }
    public String getTruncatedNote() {
        return truncatedNote;
    }
    public String getDate(){
        return noteDate;
    }



    public void setTitle(String t){
        this.title = t;
    }
    public void setNotes(String n){
        this.notes = n;
    }

    public void setTruncatedNote(String t){
        this.truncatedNote = t;
    }

    public void setDate(String d){
        this.noteDate = d;
    }

    public void updateTruncated(){
        if(this.notes.length() > 79){
            this.truncatedNote = notes.substring(0,80) + "...";
        }
        else{
            this.truncatedNote = notes;
        }
    }

    public void updateDate(){
        Date currentDate = new Date();
        String pattern = "EEE MMM dd, h:mm a";
        SimpleDateFormat sDate = new SimpleDateFormat(pattern);
        this.noteDate = sDate.format(currentDate);
    }


    @NonNull
    @Override
    public String toString() {
        return "Note: "+ title;
    }
}
