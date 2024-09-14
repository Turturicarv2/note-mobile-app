package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.finalproject.Note;
import com.example.finalproject.NoteAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView list_of_notes;
    Button new_note;

    SQLiteDatabase db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new_note = (Button) findViewById(R.id.new_note);
        new_note.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddNote.class);
            startActivity(intent);
            finish();
        });

        list_of_notes = (RecyclerView) findViewById(R.id.list_of_notes);
        list_of_notes.setLayoutManager(new LinearLayoutManager(this));

        openDatabase();

        List<Note> noteList = createNoteList();
        NoteAdapter adapter = new NoteAdapter(noteList);
        list_of_notes.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void openDatabase() {
        db = openOrCreateDatabase("NoteApp", Context.MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS notes(" +
                "    note_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    title VARCHAR," +
                "    content TEXT," +
                "    category VARCHAR);");

    }

    private void deleteDatabase() {
        db.execSQL("DELETE FROM notes");
    }

    private void insertIntoDatabase() {
        try {
            String tit = "Sample Title";
            String con = "This is a sample note content";

            ContentValues values = new ContentValues();
            values.put("title", tit);
            values.put("content", con);

            long newRowId = db.insert("notes", null, values);
            if (newRowId == -1) {
                Log.e("Insertion", "Something went wrong with the insertion!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception error", e.getMessage());
        }
    }

    private List<Note> createNoteList() {
        List<Note> noteList = new ArrayList<>();
        Cursor data = null;

        try {
            data = db.rawQuery("SELECT note_id, title, content FROM notes", null);

            if(data == null) {
                Log.w("Database", "Nothing in the database!");
            }

            if (data != null && data.moveToFirst()) {
                do {
                    Note note = new Note();

                    Integer id = data.getInt(0);
                    String title = data.getString(1);
                    String content = data.getString(2);

                    note.setNoteId(id);
                    note.setTitle(title);
                    note.setContent(content);

                    noteList.add(note);
                } while (data.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions (e.g., Cursor errors)
        } finally {
            if (data != null && !data.isClosed()) {
                data.close();
            }
        }

        return noteList;
    }
}