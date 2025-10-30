package com.example.labex7;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBHelper db;
    private ListView listView;
    private Button btnAddBook;

    private ArrayAdapter<Book> adapter;
    private List<Book> books;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DBHelper(this);
        listView = findViewById(R.id.listView);
        btnAddBook = findViewById(R.id.btnAddBook);

        loadBooks();

        btnAddBook.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, AddEditBookActivity.class);
            startActivity(i);
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Book clickedBook = books.get(position); // get the Book object at this position
            Intent i = new Intent(MainActivity.this, AddEditBookActivity.class);
            i.putExtra("BOOK_ID", clickedBook.id); // pass real database ID
            startActivity(i);
        });
    }

    private void loadBooks() {
        books = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                books
        );
        listView.setAdapter(adapter);

        Cursor cursor = db.getAllBooks();
        if (cursor.moveToFirst()){
            //we're reading from the beginning
            do {
                // Read values from the cursor
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow("genre"));
                int year = cursor.getInt(cursor.getColumnIndexOrThrow("year"));

                // Create a Book object with data
                Book book = new Book();
                book.id = id;
                book.title = title;
                book.author = author;
                book.genre = genre;
                book.year = year;

                // Add to list
                books.add(book);

            } while (cursor.moveToNext());
            //close the cursor after the transaction
            cursor.close();
            //notifying the adapter that the dataset is changed
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "The fetch process failed", Toast.LENGTH_SHORT).show();
        }
    }

    private class Book {
        private int id;
        private String title;
        private String author;
        private String genre;
        private int year;

        @Override
        public String toString() {
            return "Book{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", author='" + author + '\'' +
                    ", genre='" + genre + '\'' +
                    ", year=" + year +
                    '}';
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks(); // refresh when coming back
    }
}