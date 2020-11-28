package com.allanguan.notepad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    private List<Note> noteList;
    private MainActivity mainAct;

    NoteAdapter(List<Note> nList, MainActivity ma){
        this.noteList = nList;
        mainAct = ma;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note note = noteList.get(position);


        holder.titleId.setText(note.getTitle());
        holder.noteId.setText(note.getTruncatedNote());

//        Date ds = new Date();
//        String pattern = "EEE MMM dd, h:mm a";
//        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        holder.dateId.setText(note.getDate());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
