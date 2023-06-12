package com.example.thestubtoendallstubs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a User of the application.
 * Represented in Database.
 */
public class User {
    // Unique ID
    private String id;
    // Discern first name and last name by parsing the space in between the two sub-strings as a delimiter
    private String username;
    private String password;
    // List of Journal Objects
    private HashMap<String, Journal> listJournals;

    /**
     * Empty constructor used by Firebase Database methods to instantiate a new User Object from a database read.
     */
    public User() {
        listJournals = new HashMap<String, Journal>();
    }

    /**
     * Instantiate a new User object with passed id and name.
     * @param id
     * @param name
     */
    public User(String id, String name, String password) {
        this.id = id;
        this.username = name;
        this.password = password;
        listJournals = new HashMap<>();
    }

    /**
     * Get id of User.
     * @return String id
     */
    public String getId() {
        return id;
    }

    /**
     * Set id of User.
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get name of User.
     * @return String name
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set name of User.
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    /**
     * Get list of journals of User.
     * @return List<Journal> listJournals
     */
    public HashMap<String, Journal> getListJournals() {
        return listJournals;
    }

    /**
     * Set list of journals of User.
     * @param listJournals
     */
    public void setListJournals(HashMap<String, Journal> listJournals) {
        this.listJournals = listJournals;
    }

    /**
     * Get a specific journal of User.
     * @return Journal OR null
     */
    public Journal getJournal(String id) {
        return listJournals.get(id);
    }

    public void removeJournal(Journal journal) {
        listJournals.remove(journal.getId());
    }

    /**
     * Add a new journal to the journal list of User.
     * @param journal
     */
    public void addJournal(Journal journal) {
        listJournals.put(journal.getId(), journal);
    }
}

