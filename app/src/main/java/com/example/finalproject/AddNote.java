package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class AddNote extends AppCompatActivity {

    Button homeButton;
    Button addButton;
    EditText titleEditText;
    EditText categoryEditText;
    EditText contentEditText;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        db = openOrCreateDatabase("NoteApp", Context.MODE_PRIVATE, null);

        homeButton = (Button) findViewById(R.id.homeButton);
        addButton = (Button) findViewById(R.id.addNoteButton);
        titleEditText = (EditText) findViewById(R.id.titleEditText);
        categoryEditText = (EditText) findViewById(R.id.categoryEditText);
        contentEditText = (EditText) findViewById(R.id.contentEditText);

        homeButton.setOnClickListener(view -> {
            Intent intent = new Intent(AddNote.this, MainActivity.class);
            startActivity(intent);
        });

        addButton.setOnClickListener(view -> {
            try {
                String title = titleEditText.getText().toString();
                String category = categoryEditText.getText().toString();
                String content = contentEditText.getText().toString();

                if(title != null && !title.isEmpty() && category != null && !category.isEmpty() && content != null && !content.isEmpty()) {
                    SQLiteStatement stmt = db.compileStatement("INSERT INTO notes (title, content, category) VALUES (?, ?, ?)");

                    stmt.bindString(1, title);
                    stmt.bindString(2, content);
                    stmt.bindString(3, category);

                    long rowId = stmt.executeInsert();

                    if (rowId != -1) {
                        Intent intent = new Intent(AddNote.this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("Database", "Insertion failed!");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("Try-catch failed", "Something unexpected occurred!");
            }

        });
    }
}