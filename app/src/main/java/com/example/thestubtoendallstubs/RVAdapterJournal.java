package com.example.thestubtoendallstubs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RVAdapterJournal extends RecyclerView.Adapter<RVHolderJournal> {
    private List<Journal> journals;
    private RVHolderJournal.eventsInterface events;

    public RVAdapterJournal(RVHolderJournal.eventsInterface events, List<Journal> journals) {
        this.events = events;
        this.journals = journals;
    }
    @NonNull
    @Override
    public RVHolderJournal onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View view = lf.inflate(R.layout.journal_item, parent, false);
        return new RVHolderJournal(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolderJournal holder, int position) {
        holder.setDetails(journals.get(position));
        holder.setEvents(events);
    }

    @Override
    public int getItemCount() {
        return journals.size();
    }
}
