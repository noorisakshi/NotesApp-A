package com.example.notesapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.notesapp.Modals.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTackerActivity extends AppCompatActivity {
    EditText editext_title,editext_notes;
    ImageView imge_save;
    Notes notes;
    boolean isOldNote=false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes_tacker);
        imge_save=findViewById(R.id.image_save);
        editext_title=findViewById(R.id.editext_title);
        editext_notes=findViewById(R.id.editext_notes);
        notes=new Notes();
        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            editext_title.setText(notes.getTitle());
            editext_notes.setText(notes.getNotes());
            isOldNote=true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        imge_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=editext_title.getText().toString();
                String description=editext_notes.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(NotesTackerActivity.this, "pls add some notes", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat formatter=new SimpleDateFormat("EEE,d MMM yyyy HH:mm a");
                Date date=new Date();
                if (!isOldNote){
                    notes=new Notes();
                }

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(formatter.format(date));
                Intent intent=new Intent();
                intent.putExtra("note",notes);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }
}