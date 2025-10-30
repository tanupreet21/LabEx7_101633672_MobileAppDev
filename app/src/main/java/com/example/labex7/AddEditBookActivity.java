package com.example.labex7;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddEditBookActivity extends AppCompatActivity {

    DBHelper db;
    EditText titleTxt, authorTxt, genreTxt, yearTxt;
    Button btnSave, btnDelete;
    int bookId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_book);

        db = new DBHelper(this);
        titleTxt = findViewById(R.id.title);
        authorTxt = findViewById(R.id.author);
        genreTxt = findViewById(R.id.genre);
        yearTxt = findViewById(R.id.year);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);

        Intent intent = getIntent();

        if (intent.hasExtra("BOOK_ID")) {
            bookId = intent.getIntExtra("BOOK_ID", -1);
            loadBook(bookId);
        } else {
            btnDelete.setVisibility(Button.GONE);
        }

        btnSave.setOnClickListener(v -> saveBook());
        btnDelete.setOnClickListener(v -> {
            db.deleteBook(bookId);
            Toast.makeText(this, "Book deleted successfully!", Toast.LENGTH_SHORT).show();
            finish(); //ends this activity & takes you to main activity
        });


    }

    private void saveBook() {
        String title = titleTxt.getText().toString();
        String author = authorTxt.getText().toString();
        String genre = genreTxt.getText().toString();
        int year = Integer.parseInt(yearTxt.getText().toString());

        if (bookId == -1) {
            db.addBook(title, author, genre, year);
            Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            db.updateBook(bookId, title, author, genre, year);
            Toast.makeText(this, "Book updated successfully!", Toast.LENGTH_SHORT).show();
        }

        finish(); //ends this activity & takes you to main activity
    }

    private void loadBook(int bookId) {
        Cursor c = db.getBookById(bookId);
        if (c.moveToFirst()) {
            titleTxt.setText(c.getString(c.getColumnIndexOrThrow("title")));
            authorTxt.setText(c.getString(c.getColumnIndexOrThrow("author")));
            genreTxt.setText(c.getString(c.getColumnIndexOrThrow("genre")));
            yearTxt.setText(c.getString(c.getColumnIndexOrThrow("year")));
        }
        c.close();
    }
}