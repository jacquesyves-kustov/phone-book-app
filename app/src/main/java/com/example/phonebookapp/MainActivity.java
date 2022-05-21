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

    }

    /**
     * A native method that is implemented by the 'phonebookapp' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}