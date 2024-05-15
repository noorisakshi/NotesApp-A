package com.example.notesapp;

import androidx.cardview.widget.CardView;

import com.example.notesapp.Modals.Notes;

public interface NotesClickListener {
    void onclick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
