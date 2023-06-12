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

public class JournalMenuActivity extends AppCompatActivity {

    private RecyclerView recEntries;
    private ImageButton btnAddEntry;
    private static final String DEFAULT_TITLE = "New Entry";
    private static final String DEFAULT_CONTENT = "Some content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_menu);
        setElements();
    }

    protected void onResume() {
        super.onResume();
        setUpRecycler();
    }

    private void setElements() {
        recEntries = findViewById(R.id.rec_entries);
        btnAddEntry = findViewById(R.id.btn_add_entry);

        setEvents();
    }

    private void setEvents() {
        btnAddEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entry newEntry = JController.createEntry(DEFAULT_TITLE);
                newEntry.setContent(DEFAULT_CONTENT);
                JController.insertEntry(JController.getCurrentUser(), JController.getCurrentJournal(), newEntry);
                setUpRecycler();
            }
        });
    }

    private void setUpRecycler() {
        clearRecycler();
        HashMap<String, Entry> entries = JController.getCurrentJournal().getListEntries();
        List<Entry> entryList = new ArrayList<>();
        for (Map.Entry<String, Entry> mapEntry: entries.entrySet()) {
            Entry entry = mapEntry.getValue();
            entryList.add(entry);
        }

        RVAdapterEntry adapterEntry = new RVAdapterEntry(new RVHolderEntry.eventsInterface() {
            @Override
            public void load(Entry entry) {
                loadEntry(entry);
            }

            @Override
            public void delete(Entry entry) {
                deleteEntry(entry);
            }
        }, entryList) ;
        recEntries.setAdapter(adapterEntry);
    }

    private void clearRecycler() {
        recEntries.removeAllViewsInLayout();
        recEntries.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadEntry(Entry entry) {
        JController.setCurrentEntry(entry);
        Intent entryIntent = new Intent(this, EntryContentActivity.class);
        startActivity(entryIntent);
    }

    private void deleteEntry(Entry entry) {
        JController.deleteEntry(JController.getCurrentUser(), JController.getCurrentJournal(), entry);
        setUpRecycler();
    }
}