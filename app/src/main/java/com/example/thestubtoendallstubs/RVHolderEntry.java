package com.example.thestubtoendallstubs;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

public class RVHolderEntry extends RecyclerView.ViewHolder {
    private View itemView;
    private CardView cardView;
    private EditText txtTitle;
    private ImageButton btnDelete;
    private Entry entry;

    public RVHolderEntry(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        setElements();
    }

    private void setElements() {
        cardView = itemView.findViewById(R.id.car_entry);
        txtTitle = itemView.findViewById(R.id.txt_entry_title);
        btnDelete = itemView.findViewById(R.id.btn_delete_entry);
    }

    public void setDetails(Entry entry) {
        this.entry = entry;
        txtTitle.setText(entry.getTitle());
    }

    public void setEvents(eventsInterface events) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                entry.setTitle(title);
                events.load(entry);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                entry.setTitle(title);
                events.delete(entry);
            }
        });

        txtTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String title = txtTitle.getText().toString();
                entry.setTitle(title);
                JController.insertEntry(JController.getCurrentUser(), JController.getCurrentJournal(), entry);
            }
        });
    }

    public interface eventsInterface {
        void load(Entry entry);
        void delete(Entry entry);
    }
}
