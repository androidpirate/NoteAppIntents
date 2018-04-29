package com.example.android.noteappintents;

import com.example.android.noteappintents.adapter.NoteAdapter;
import com.example.android.noteappintents.model.Note;
import com.example.android.noteappintents.utils.FakeDataUtils;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements NoteAdapter.NoteClickListener {
    private List<Note> notes;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get fake data
        notes = FakeDataUtils.getFakeNotes();
        recyclerView = findViewById(R.id.rv_note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set adapter
        NoteAdapter adapter = new NoteAdapter(notes, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick() {
        Toast.makeText(this, "Note is clicked", Toast.LENGTH_SHORT).show();
    }
}
