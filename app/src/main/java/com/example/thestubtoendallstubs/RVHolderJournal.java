package com.example.thestubtoendallstubs;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.Consumer;

public class RVHolderJournal extends RecyclerView.ViewHolder {
    private View itemView;
    private CardView cardView;
    private EditText txtTitle;
    private TextView txtDesc;
    private ImageButton btnDelete;
    private Journal journal;

    public RVHolderJournal(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        setElements();
    }

    private void setElements() {
        cardView = itemView.findViewById(R.id.car_journal);
        txtTitle = itemView.findViewById(R.id.txt_journal_title);
        txtDesc = itemView.findViewById(R.id.txt_journal_desc);
        btnDelete = itemView.findViewById(R.id.btn_delete_journal);
    }

    public void setDetails(Journal journal) {
        this.journal = journal;
        txtTitle.setText(journal.getTitle());
        txtDesc.setText(journal.getDescription());
    }

    public void setEvents(eventsInterface events) {
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                journal.setTitle(title);
                events.load(journal);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString();
                journal.setTitle(title);
                events.delete(journal);
            }
        });

        txtTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String title = txtTitle.getText().toString();
                journal.setTitle(title);
                JController.insertJournal(JController.getCurrentUser(), journal);
            }
        });
    }

    public interface eventsInterface {
        void load(Journal journal);
        void delete(Journal journal);
    }
}
