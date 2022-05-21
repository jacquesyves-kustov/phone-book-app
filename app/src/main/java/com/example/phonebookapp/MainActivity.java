package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phonebookapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
        Button button_ShowAll = findViewById(R.id.showAllButton);
        Button button_Clear = findViewById(R.id.clearButton);
        Button button_FindContact = findViewById(R.id.findButton);
        Button button_AddContact = findViewById(R.id.addButton);
        Button button_Debug = findViewById(R.id.debugButton);

        // Other
        ListView contactsList = findViewById(R.id.contactView);
        EditText et_searchField = findViewById(R.id.searchEditText);
        EditText et_newNameField = findViewById(R.id.newNameEditText);
        EditText et_newNumberField = findViewById(R.id.newNumberEditText);
        TextView tv_totalContacts = binding.contactsCounter;

        // Set default value to 'totalContacts'
        tv_totalContacts.setText(getString(R.string.counter_messages, String.valueOf(0)));

        // Create a cpp class
        ptBook = createBook();


        // Functionality

        /*
         * "Show all" button
         */

        button_ShowAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Refresh contacts list
                contactsList.setAdapter(null);

                // Get all contacts from C++
                ArrayList<String> contactsArr = getAllContacts();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, contactsArr);

                // Refresh info
                contactsList.setAdapter(adapter);
                tv_totalContacts.setText(getString(R.string.counter_messages, String.valueOf(contactsArr.size())));
            }

        });


        /*
         * "Clear" button
         */

        button_Clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Clear contacts list, clear editText field and set default value to counter
                et_searchField.setText("");
                contactsList.setAdapter(null);
                tv_totalContacts.setText(getString(R.string.counter_messages, String.valueOf(0)));
            }

        });

    }


    // Auxiliary methods

    ArrayList<String> getAllContacts() {
        // CPP sends json as string to Java
        String jsonStr_cpp = sendAllContacts(ptBook);

        // Java saves them into array
        ArrayList<String> contactsList = new ArrayList<String>();

        // Deserialize JSON string
        try
        {
            JSONArray contactList = (new JSONObject(jsonStr_cpp)).getJSONArray("contactList");

            for (int i = 0; i < contactList.length(); i++)
            {
                JSONObject contact = contactList.getJSONObject(i);
                String contactName = contact.getString("Name");
                String contactNumbers = contact.getString("Number");

                contactsList.add((i + 1) + ". " + contactName + "\n" + contactNumbers);
            }

        }
        catch (JSONException e)
        {
            contactsList.add("None");
        }


        return contactsList;
    }


    // Native methods implemented in cpp library
    private native long createBook();
    private native String sendAllContacts(long ptBook);
    private native String sendContactByName(long ptBook, String request);
    private native void addNewContact(long ptBook, String jsonStr);
}