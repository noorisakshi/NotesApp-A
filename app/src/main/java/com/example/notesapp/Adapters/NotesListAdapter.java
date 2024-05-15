package com.example.notesapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.Modals.Notes;
import com.example.notesapp.NotesClickListener;
import com.example.notesapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {
    Context context;
    List<Notes> notesList;
    NotesClickListener listener;

    public NotesListAdapter(Context context, List<Notes> notesList, NotesClickListener listener) {
        this.context = context;
        this.notesList = notesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesListAdapter.ViewHolder holder, int position) {
        holder.textview_title.setText(notesList.get(position).getTitle());
        holder.textview_title.setSelected(true);
        holder.textview_notes.setText(notesList.get(position).getNotes());
        holder.textview_dates.setText(notesList.get(position).getDate());
        holder.textview_dates.setSelected(true);
        if (notesList.get(position).getPinned()){
            holder.imageview_pin.setImageResource(R.drawable.pin);
        }else {
            holder.imageview_pin.setImageResource(0);
        }
        int colorcode=getRandomcolor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorcode,null));
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onclick(notesList.get(holder.getAdapterPosition()));

            }
        });
        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onLongClick(notesList.get(holder.getAdapterPosition()),holder.notes_container);
                return true;
            }
        });

    }
private  int getRandomcolor(){
        List<Integer> colorcode=new ArrayList<>();

    colorcode.add(R.color.color1);
    colorcode.add(R.color.color2);
    colorcode.add(R.color.color3);
    colorcode.add(R.color.color4);
    colorcode.add(R.color.color5);
    colorcode.add(R.color.color6);
    Random random=new Random();
    int randomcolor=random.nextInt(colorcode.size());
    return colorcode.get(randomcolor);
}
    @Override
    public int getItemCount() {
        return notesList.size();
    }
    public void filteredList(List<Notes> filteredList){
        notesList=filteredList;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        CardView notes_container;
        TextView textview_title,textview_notes,textview_dates;
        ImageView imageview_pin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notes_container=itemView.findViewById(R.id.notes_container);
            textview_title=itemView.findViewById(R.id.textview_title);
            textview_notes=itemView.findViewById(R.id.textview_notes);
            textview_dates=itemView.findViewById(R.id.textview_dates);
            imageview_pin=itemView.findViewById(R.id.imageview_pin);
        }
    }
}
