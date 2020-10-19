package com.giriseematechme.bankingforms.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.giriseematechme.bankingforms.R;

public class AddForms extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "EXTRA_NAME";
    public static final String EXTRA_ADDRESS =
            "EXTRA_ADDRESS";
    public static final String EXTRA_EMAIL =
            "EXTRA_EMAIL";
    public static final String EXTRA_PHONE =
            "EXTRA_PHONE";
    public static final String EXTRA_AGE =
            "EXTRA_AGE";

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextEmail;
    private EditText editTextPhoneNumber;
    private EditText age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forms);

        editTextName = findViewById(R.id.edit_text_title);
        editTextAddress = findViewById(R.id.edit_text_address);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPhoneNumber = findViewById(R.id.edit_text_phone);
        age = findViewById(R.id.number_picker_priority);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Form");
    }
    private void saveForm() {
        String userName = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        String email = editTextEmail.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String ageValue = age.getText().toString();
        if (userName.trim().isEmpty() || address.trim().isEmpty()||
                email.trim().isEmpty() || phoneNumber.trim().isEmpty()||
                ageValue.trim().isEmpty()){
            Toast.makeText(this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, userName);
        data.putExtra(EXTRA_ADDRESS, address);
        data.putExtra(EXTRA_EMAIL, email);
        data.putExtra(EXTRA_PHONE, phoneNumber);
        data.putExtra(EXTRA_AGE, Integer.parseInt(ageValue));
        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_form:
                saveForm();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}