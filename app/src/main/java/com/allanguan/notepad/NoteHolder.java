package com.allanguan.notepad;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteHolder extends RecyclerView.ViewHolder {

    TextView titleId;
    TextView noteId;
    TextView dateId;

    NoteHolder(@NonNull View itemView) {
        super(itemView);
        titleId = itemView.findViewById(R.id.title);
        noteId = itemView.findViewById(R.id.note);
        dateId = itemView.findViewById(R.id.date);
    }
}
