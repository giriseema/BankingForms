package com.giriseematechme.bankingforms.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.giriseematechme.bankingforms.R;
import com.giriseematechme.bankingforms.RoomDatabase.Form;
import com.giriseematechme.bankingforms.ViewModel.FormViewModel;
import com.giriseematechme.bankingforms.adapter.UserFormsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ViewForms extends AppCompatActivity {
    private AppCompatTextView textViewName;
    private RecyclerView recyclerViewUsers;
    public static final int ADD_NOTE_REQUEST = 1;
    private FormViewModel formViewModel;
    private AppCompatTextView noForms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_forms);
        textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        recyclerViewUsers = (RecyclerView) findViewById(R.id.recyclerViewUserForms);
        noForms=(AppCompatTextView)findViewById(R.id.no_forms);
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        FloatingActionButton buttonAddNote = findViewById(R.id.add_forms);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewForms.this, AddForms.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setHasFixedSize(true);
        final UserFormsAdapter adapter = new UserFormsAdapter();
        recyclerViewUsers.setAdapter(adapter);
        formViewModel = ViewModelProviders.of(this).get(FormViewModel.class);
        formViewModel.getForms().observe(this, new Observer<List<Form>>() {
            @Override
            public void onChanged(@Nullable List<Form> notes) {
                if(notes.size()>0)
                    noForms.setVisibility(View.GONE);
                else
                    noForms.setVisibility(View.VISIBLE);

                adapter.setForms(notes);


            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                formViewModel.delete(adapter.getFormAt(viewHolder.getAdapterPosition()));
                Toast.makeText(ViewForms.this, "Form deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewUsers);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddForms.EXTRA_NAME);
            String address = data.getStringExtra(AddForms.EXTRA_ADDRESS);
            String email = data.getStringExtra(AddForms.EXTRA_EMAIL);
            String phoneNumber = data.getStringExtra(AddForms.EXTRA_PHONE);
            int age = data.getIntExtra(AddForms.EXTRA_AGE, 1);
            Form form = new Form(name, address, email, phoneNumber, age);
            formViewModel.insert(form);
            Toast.makeText(this, "Form saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Form not saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                formViewModel.deleteAllForms();
                noForms.setVisibility(View.VISIBLE);
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}