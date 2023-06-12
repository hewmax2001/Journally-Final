package com.example.thestubtoendallstubs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Entry of the application.
 * Represented in Database.
 */
public class Entry {
    // Unique ID
    private String id;
    private String title;
    private String creationDate;
    // List of Content objects
    private String content;
    // Id of audio file
    private String audioId = null;
    //Boolean to check bookmarked or not
    private boolean bookmark = false;

    /**
     * Empty constructor used by Firebase Database methods to instantiate a new Entry Object from a database read.
     */
    public Entry() {

    }

    /**
     * Instantiate a new Entry object with passed id, title and creation date.
     * @param id
     * @param title
     * @param creationDate
     */
    public Entry(String id, String title, String creationDate) {
        this.id = id;
        this.title = title;
        this.creationDate = creationDate;
    }

    // Getters and setters

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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getAudioId() {return audioId;}

    public void setAudioId(String audioId) {this.audioId = audioId;}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}

