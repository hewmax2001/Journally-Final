package com.example.thestubtoendallstubs;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles functionality of entire application.
 */
public final class JController {
    // TODO REMOVE THIS LATER
    public static String userId = "-NVsAnh4G_RYk3B872I7";
    // Current User of application
    private static User currentUser;
    // Current Journal of application
    private static Journal currentJournal;
    // Current Entry of application
    private static Entry currentEntry;
    // Date format used
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    // Filepath of external storage files
    private static String filePath;
    private static final String USER_REF = "users";

    /**
     * Class cannot be instantiated, meant to be statically accessed.
     */
    private JController() {

    }

    public static void start(Context context) {
        CloudDatabase.start();
        setAudioFilePath(context);
    }

    /**
     * Create and insert a new user with username and password.
     * Does not create user if a user with the same username and password already exists.
     * @param call
     * @param name
     * @param password
     */
    public static void insertUser(Callback call, String name, String password) {
        getUserByUsername(new Callback() {
            @Override
            public void onCallback(DataSnapshot snap) {
                System.out.println("Apparently this user exists");
                System.out.println(snap.toString());
                call.onFailure();
            }

            @Override
            public void onFailure() {
                User newUser = createUser(name, password);
                CloudDatabase.insertData(getUsersRef().child(newUser.getId()), newUser);
                call.onCallback(null);
            }
        }, name, password);
    }

    /**
     * Return a DataSnapshot containing all users of the database
     * @param call
     */
    public static void getUsers(Callback call) {
        CloudDatabase.readDataOnce(call, getUsersRef());
    }

    /**
     * Return a DataSnapshot contain a user based on id
     * @param id
     * @param call
     */
    public static void getUser(String id, Callback call) {
        CloudDatabase.readDataOnce(call, getUsersRef().child(id));
    }

    /**
     * Return a DataSnapshot containing a user based on username and password.
     * @param call
     * @param username
     * @param password
     */
    public static void getUserByUsername(Callback call, String username, String password) {
        // Get all users and find user with username and password
        getUsers(new Callback() {
            @Override
            public void onCallback(DataSnapshot snap) {
                // Iterate through all users
                Iterable<DataSnapshot> snaps = snap.getChildren();
                for (DataSnapshot userSnap: snaps) {
                    User user = userSnap.getValue(User.class);
                    // If username and password are the same as parameters
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        // Return DataSnapshot based on found user id
                        CloudDatabase.readDataOnce(call, getUsersRef().child(user.getId()));
                        return;
                    }
                }
                call.onFailure();
            }

            @Override
            public void onFailure() {
                Log.d("GetUser(username + password)", "Getting users failed");
            }
        });
    }

    //region Entity CRUD
    //---------------------------------------------------------------------------------------
    /**
     * Create and return new User object using passed String name.
     * @param name
     * @return
     */
    public static User createUser(String name, String password) {
        // Get unique ID from database
        String id = CloudDatabase.getNewId();
        // Create and return
        User newUser = new User(id, name, password);
        return newUser;
    }

    public static void updateUser(User user) {
        DatabaseReference userRef = getUserRef(user.getId());
        CloudDatabase.insertData(userRef, user);
    }

    public static void deleteUser(User user) {
        DatabaseReference userRef = getUserRef(user.getId());
        CloudDatabase.insertData(userRef, null);
    }


    /**
     * Create and return new Journal object using passed Strings title and description.
     * @param title
     * @param description
     * @return
     */
    public static Journal createJournal(String title, String description) {
        // Get unique ID from database
        String id = CloudDatabase.getNewId();
        // Get current date of creation
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String creationDate = dtf.format(LocalDateTime.now());
        // Create and return
        Journal newJournal = new Journal(id, title, description, creationDate);
        return newJournal;
    }

    /**
     * Insert Journal into the database at reference based on User and Journal ID
     * Performs both create and update functionality of database.
     * @param user
     * @param journal
     */
    public static void insertJournal(User user, Journal journal) {
        DatabaseReference jourRef = getJournalRef(user, journal);
        if (!user.getListJournals().containsKey(journal.getId())) {
            user.addJournal(journal);
        }
        CloudDatabase.insertData(jourRef, journal);
    }

    public static void deleteJournal(User user, Journal journal) {
        DatabaseReference jourRef = getJournalRef(user, journal);
        if (user.getListJournals().containsKey(journal.getId())) {
            user.removeJournal(journal);
        }
        CloudDatabase.insertData(jourRef, null);
    }


    /**
     * Creates and returns new Entry using passed String title.
     * @param title
     * @return
     */
    public static Entry createEntry(String title) {
        // Get unique ID from database
        String id = CloudDatabase.getNewId();
        // Get current date of creation
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String creationDate = dtf.format(LocalDateTime.now());
        // Create and return
        Entry newEntry = new Entry(id, title, creationDate);
        // Create and insert default content
        return newEntry;
    }

    /**
     * Insert Entry into the database at reference based on User, Journal and Entry ID
     * Performs both create and update functionality of database.
     * @param user
     * @param journal
     * @param entry
     */
    public static void insertEntry(User user, Journal journal, Entry entry) {
        DatabaseReference entryRef = getEntryReference(user, journal, entry);
        if (!journal.getListEntries().containsKey(entry.getId())) {
           journal.addEntry(entry);
        }
        CloudDatabase.insertData(entryRef, entry);
    }

    public static void deleteEntry(User user, Journal journal, Entry entry) {
        DatabaseReference entryRef = getEntryReference(user, journal, entry);
        if (journal.getListEntries().containsKey(entry.getId())) {
            journal.removeJournal(entry);
        }
        CloudDatabase.insertData(entryRef, null);
    }
    //---------------------------------------------------------------------------------------
    //endregion



    //region Reference Handling
    //---------------------------------------------------------------------------------------
    /**
     * Get references to users list.
     * @return
     */
    public static DatabaseReference getUsersRef() {
        return CloudDatabase.getRef(USER_REF);
    }

    /**
     * Get reference to a specific user based on id.
     * @param id
     * @return
     */
    private static DatabaseReference getUserRef(String id) {
        return getUsersRef().child(id);
    }

    /**
     * Returns Database reference of passed Journal argument, requires parent User of Journal argument.
     * @param user
     * @param journal
     * @return
     */
    private static DatabaseReference getJournalRef(User user, Journal journal) {
        DatabaseReference userRef = getUserRef(user.getId());
        DatabaseReference jourRef = userRef.child("listJournals").child(journal.getId());
        return jourRef;
    }

    /**
     * Returns Database reference of passed Entry argument, requires parent User and parent Journal argument.
     * @param user
     * @param journal
     * @param entry
     * @return
     */
    private static DatabaseReference getEntryReference(User user, Journal journal, Entry entry) {
        DatabaseReference jourRef = getJournalRef(user, journal);
        DatabaseReference entryRef = jourRef.child("listEntries").child(entry.getId());
        return entryRef;
    }
    //---------------------------------------------------------------------------------------
    //endregion



    // Getters + Setters

    /**
     * Sets file path to external audio files.
     */
    private static void setAudioFilePath(Context context) {
        // Static instance of application context.
        filePath = context.getExternalFilesDir(null).getAbsolutePath();
    }

    /**
     * Returns AudioFilePath.
     * @return
     */
    public static String getAudioFilePath() {
        return filePath;
    }

    /**
     * Returns currentUser.
     * @return
     */
    public static User getCurrentUser() { return currentUser; }

    /**
     * Sets currentUser.
     * @param currentUser
     */
    public static void setCurrentUser(User currentUser) { JController.currentUser = currentUser; }

    /**
     * Returns currentJournal.
     * @return
     */
    public static Journal getCurrentJournal() { return currentJournal; }

    /**
     * Sets currentJournal.
     * @param currentJournal
     */
    public static void setCurrentJournal(Journal currentJournal) {
        JController.currentJournal = currentJournal;
    }

    /**
     * Returns currentEntry.
     * @return
     */
    public static Entry getCurrentEntry() { return currentEntry; }

    /**
     * Sets currentEntry.
     * @param currentEntry
     */
    public static void setCurrentEntry(Entry currentEntry) {
        JController.currentEntry = currentEntry;
    }
}

