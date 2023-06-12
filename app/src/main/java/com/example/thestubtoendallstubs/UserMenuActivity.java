package com.example.thestubtoendallstubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class UserMenuActivity extends AppCompatActivity {
    private RecyclerView recJournals;
    private ImageButton btnAddJournal;
    private static final String DEFAULT_TITLE = "New Journal";
    private static final String DEFAULT_DESC = "Some Description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        setElements();
    }

    protected void onResume() {
        super.onResume();
        setUpRecycler();
    }

    private void setElements() {
        recJournals = findViewById(R.id.rec_journals);
        btnAddJournal = findViewById(R.id.btn_add_journal);

        setEvents();
    }

    private void setEvents() {
        btnAddJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Journal newJournal = JController.createJournal(DEFAULT_TITLE, DEFAULT_DESC);
                JController.insertJournal(JController.getCurrentUser(), newJournal);
                setUpRecycler();
            }
        });
    }

    private void setUpRecycler() {
        clearRecycler();
        HashMap<String, Journal> journals = JController.getCurrentUser().getListJournals();
        List<Journal> journalList = new ArrayList<>();
        for (Map.Entry<String, Journal> entry: journals.entrySet()) {
            Journal journal = entry.getValue();
            journalList.add(journal);
        }

        RVAdapterJournal adapterJournal = new RVAdapterJournal(new RVHolderJournal.eventsInterface() {
            @Override
            public void load(Journal journal) {
                loadJournal(journal);
            }

            @Override
            public void delete(Journal journal) {
                deleteJournal(journal);
            }
        }, journalList) ;
        recJournals.setAdapter(adapterJournal);
    }

    private void clearRecycler() {
        recJournals.removeAllViewsInLayout();
        recJournals.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadJournal(Journal journal) {
        JController.setCurrentJournal(journal);
        /*String title = "Test Entry 2";
        Entry newEntry = JController.createEntry(title);
        JController.insertEntry(JController.getCurrentUser(), journal, newEntry);*/
        Intent journalIntent = new Intent(this, JournalMenuActivity.class);
        startActivity(journalIntent);
    }

    private void deleteJournal(Journal journal) {
        JController.deleteJournal(JController.getCurrentUser(), journal);
        setUpRecycler();
    }
}