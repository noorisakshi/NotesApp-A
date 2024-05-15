package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.notesapp.Adapters.NotesListAdapter;
import com.example.notesapp.Database.RoomDB;
import com.example.notesapp.Modals.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    RecyclerView recycler_home;
    FloatingActionButton fab_add;
    NotesListAdapter notesListAdapter;
    List<Notes> notes=new ArrayList<>();
    RoomDB database;
    SearchView searchview;
    Notes selectednote;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        recycler_home=findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);
        searchview=findViewById(R.id.searchview);
        database=RoomDB.getInstance(this);
        notes=database.mainDAO().getAll();
        updateRecycler(notes);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, NotesTackerActivity.class);
                startActivityForResult(intent,101);
            }
        });
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }

            private void filter(String newText) {
                List<Notes> filterList=new ArrayList<>();
                for (Notes singlenotes:notes){
                    if (singlenotes.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singlenotes.getNotes().toLowerCase().contains(newText.toLowerCase())){
                        filterList.add(singlenotes);

                    }
                }
                notesListAdapter.filteredList(filterList);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101){
            if (resultCode== Activity.RESULT_OK){
                Notes new_notes=new Notes();
                 new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        } else if (requestCode==102) {
            if (resultCode==Activity.RESULT_OK){
                Notes new_notes= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }

        }

    }

    private void updateRecycler(List<Notes> notes) {
        recycler_home.setHasFixedSize(true);
        recycler_home.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        notesListAdapter=new NotesListAdapter(MainActivity.this,notes,notesClickListener);
        recycler_home.setAdapter(notesListAdapter);
    }
    private final NotesClickListener notesClickListener= new NotesClickListener() {
        @Override
        public void onclick(Notes notes) {
            Intent i=new Intent(MainActivity.this, NotesTackerActivity.class);
            i.putExtra("old_note",notes);
            startActivityForResult(i,102);

        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectednote=new Notes();
            selectednote=notes;
            showPopUp(cardView);


        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu=new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popmenu);
        popupMenu.show();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int item1=item.getItemId();
        if (item1==R.id.pin){
            if (selectednote.getPinned()){
                database.mainDAO().pin(selectednote.getID(),false);
                Toast.makeText(this, "unpinned!", Toast.LENGTH_SHORT).show();

            }
            else {
                database.mainDAO().pin(selectednote.getID(),true);
                Toast.makeText(this, "pinned", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDAO().getAll());
            notesListAdapter.notifyDataSetChanged();
            return  true;

        }
        if (item1==R.id.delete){
            database.mainDAO().delete(selectednote);
            notes.remove(selectednote);
            notesListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "note Deleted", Toast.LENGTH_SHORT).show();
            return true;


        }
        else {
            return false;
        }



    }
}