package com.example.phonebookapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


        /*
         * "Find" button
         */

        button_FindContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear listView
                contactsList.setAdapter(null);

                // Get input string
                String strRequest = et_searchField.getText().toString();

                // Send request to c++, then store answer
                ArrayList<String> foundContactsArr = getContactsByName(strRequest);

                // Create new adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, foundContactsArr);

                // Refresh info
                contactsList.setAdapter(adapter);
                tv_totalContacts.setText(getString(R.string.counter_messages, String.valueOf(foundContactsArr.size())));
            }

        });


        /*
         * "Add contact" button
         */

        button_AddContact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Store input info
                String strName = et_newNameField.getText().toString();
                String strNums = et_newNumberField.getText().toString();

                // ДОБАВИТЬ ПРОВЕРОК??? ФОРМАТИРОВАНИЕ НОМЕРА??? РЕГУЛЯРКИ?!

                // Check empty input
                if (strName.length() == 0)
                {
                    strName = "EmptyName";
                }

                if (strNums.length() == 0)
                {
                    strNums = "EmptyNumber";
                }


                et_newNameField.setText("");
                et_newNumberField.setText("");


                // Create JSON Object
                JSONObject newContact = new JSONObject();

                try {
                    newContact.put("Name", strName);
                    newContact.put("Number", strNums);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // Send serialized JSONObject to С++
                addNewContact(ptBook, newContact.toString());

            }
        });


        /*
         * "DEBUG" button
         */

        button_Debug.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DebugActivity.class);
                startActivity(intent);
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


    ArrayList<String> getContactsByName(String request)
    {
        // Result array
        ArrayList<String> contactsList = new ArrayList<String>();

        // Create JSON Object
        JSONObject jRequest = new JSONObject();

        String result = "None";

        // Java sends JSON to C++, then answer is stored in 'result'
        try {
            jRequest.put("Name", request);
            result = sendContactByName(ptBook, jRequest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Translate JSON string from C++
        try
        {
            JSONArray contactList = (new JSONObject(result)).getJSONArray("contactList");

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