package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phonebookapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'phonebookapp' library on application startup.
    static {
        System.loadLibrary("phonebookapp");
    }

    // Memory address of C++ PhoneBook instance
    private long  ptBook = 0;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // UI elements
        // Buttons
        Button btShowAll = findViewById(R.id.showAllButton);
        Button btClear = findViewById(R.id.clearButton);
        Button btFindContact = findViewById(R.id.findButton);
        Button btAddContact = findViewById(R.id.addButton);
        Button btDebug = findViewById(R.id.debugButton);

        // Other
        ListView contactsList = findViewById(R.id.contactView);
        EditText searchField = findViewById(R.id.searchEditText);
        EditText newNameField = findViewById(R.id.newNameEditText);
        EditText newNumberField = findViewById(R.id.newNumberEditText);
        TextView totalContacts = binding.contactsCounter;

        // Set default value to 'totalContacts'
        totalContacts.setText("Всего контактов: 0");

        // Create a cpp class
        ptBook = createBook();

    }


    // Native methods implemented in cpp library
    private native long createBook();
    private native String sendAllContacts(long ptBook);
    private native String sendContactByName(long ptBook, String request);
    private native void addNewContact(long ptBook, String jsonStr);
}