package com.example.thestubtoendallstubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a Journal within the application.
 * Represented in Database.
 */
public class Journal {
    // Unique ID
    private String id;
    // Title
    private String title;
    private String description;
    private String creationDate;
    // List of Entry objects
    private HashMap<String, Entry> listEntries;

    /**
     * Empty constructor used by Firebase Database methods to instantiate a new Journal Object from a database read.
     */
    public Journal() {
        listEntries = new HashMap<String, Entry>();
    }

    /**
     * Instantiate a new Journal object with passed id, title, name and creationDate
     * @param id
     * @param title
     * @param description
     * @param creationDate
     */
    public Journal(String id, String title, String description, String creationDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creationDate = creationDate;
        listEntries = new HashMap<String, Entry>();
    }

    /**
     * Get id of Journal
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id of Journal
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get title of Journal
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of Journal
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get description of Journal
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description of Journal
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get creationDate of Journal
     * @return creationDate
     */
    public String getCreationDate() {
        return creationDate;
    }

    /**
     * Set creationDate of Journal
     * @param creationDate
     */
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Get listEntries of Journal
     * @return listEntries
     */
    public HashMap<String, Entry> getListEntries() {
        return listEntries;
    }

    /**
     * Set listEntries of Journal
     * @param listEntries
     */
    public void setListEntries(HashMap<String, Entry> listEntries) {
        this.listEntries = listEntries;
    }

    /**
     * Get specified Entry of Journal based on Entry's id
     * @param id
     * @return Entry OR null
     */
    public Entry getEntry(String id) {
        return listEntries.get(id);
    }

    public void removeJournal(Entry entry) {
        listEntries.remove(entry.getId());
    }

    /**
     * Add an entry to the entry list of Journal
     * @param entry
     */
    public void addEntry(Entry entry) {
        listEntries.put(entry.getId(), entry);
    }

}

