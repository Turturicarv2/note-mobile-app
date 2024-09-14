package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText;
    EditText categoryEditText;
    EditText contentEditText;

    Button editButton;
    Button homeButton;
    Button deleteButton;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        db = openOrCreateDatabase("NoteApp", Context.MODE_PRIVATE, null);

        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        contentEditText = findViewById(R.id.contentEditText);

        editButton = findViewById(R.id.editButton);
        homeButton = findViewById(R.id.homeButton);
        deleteButton = findViewById(R.id.deleteButton);

        titleEditText.setEnabled(false);
        categoryEditText.setEnabled(false);
        contentEditText.setEnabled(false);

        Intent intent = getIntent();
        Integer id = intent.getIntExtra("NOTE_ID", -1);

        try {
            Cursor data = db.rawQuery("SELECT note_id, title, content, category FROM notes WHERE note_id = " + id + ";", null);

            if(data != null && data.moveToFirst()) {
                String title = data.getString(1);
                String content = data.getString(2);
                String category = data.getString(3);

                // Now use these values as needed
                titleEditText.setText(title);
                categoryEditText.setText(category);
                contentEditText.setText(content);


            } else {
                Log.w("Database", "No data found for the provided ID: " + id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Try-catch failed", "Something unexpected occurred!");
        }

        deleteButton.setOnClickListener(view -> {
            // Assuming you have a context (like 'this' in an Activity) and an AlertDialog.Builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this note?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked Yes, perform the deletion action here
                            // For example, call a method to delete the note
                            db.execSQL("DELETE FROM notes WHERE note_id = " + id);
                            Intent intent1 = new Intent(NoteDetailsActivity.this, MainActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finish(); // Optional: Finish the current activity if you don't need to return to it
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // User clicked No, do nothing or dismiss the dialog
                            dialog.dismiss();
                        }
                    })
                    .show();

        });

        // Set a click listener on the Edit button
        editButton.setOnClickListener(view -> {
            // Convert TextViews to EditTexts for editing
            if("Edit".equals(editButton.getText().toString())) {
                titleEditText.setEnabled(true);
                categoryEditText.setEnabled(true);
                contentEditText.setEnabled(true);

                editButton.setText("Save");
            }
            else if("Save".equals(editButton.getText().toString())) {
                String newTitle = titleEditText.getText().toString();
                String newCategory = categoryEditText.getText().toString();
                String newContent = contentEditText.getText().toString();

                db.execSQL("UPDATE notes " +
                                "SET title = ?, " +
                                "category = ?, " +
                                "content = ? " +
                                "WHERE note_id = ?;",
                        new String[]{newTitle, newCategory, newContent, String.valueOf(id)});


                titleEditText.setEnabled(false);
                categoryEditText.setEnabled(false);
                contentEditText.setEnabled(false);

                editButton.setText("Edit");

            }

            // Set other necessary attributes for editing (e.g., background color, etc.)
        });

        homeButton.setOnClickListener(view -> {
            Intent intent1 = new Intent(NoteDetailsActivity.this, MainActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent1);
            finish(); // Optional: Finish the current activity if you don't need to return to it
        });

        // Display the note details in the activity


    }
}
