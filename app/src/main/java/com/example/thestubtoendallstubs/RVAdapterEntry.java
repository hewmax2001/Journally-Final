package com.example.thestubtoendallstubs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class RVAdapterEntry extends RecyclerView.Adapter<RVHolderEntry> {
    private List<Entry> entries;
    private RVHolderEntry.eventsInterface events;

    public RVAdapterEntry(RVHolderEntry.eventsInterface events, List<Entry> entries) {
        this.events = events;
        this.entries = entries;
    }
    @NonNull
    @Override
    public RVHolderEntry onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater lf = LayoutInflater.from(parent.getContext());
        View view = lf.inflate(R.layout.entry_item, parent, false);
        return new RVHolderEntry(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVHolderEntry holder, int position) {
        holder.setDetails(entries.get(position));
        holder.setEvents(events);
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
