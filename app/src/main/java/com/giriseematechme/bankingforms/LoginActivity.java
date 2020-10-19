package com.giriseematechme.bankingforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.giriseematechme.bankingforms.RoomDatabase.LoginTable;
import com.giriseematechme.bankingforms.ViewModel.LoginViewModel;
import com.giriseematechme.bankingforms.utils.AESUtils;
import com.giriseematechme.bankingforms.utils.InputValidation;
import com.giriseematechme.bankingforms.view.ViewForms;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private AppCompatButton appCompatButtonLogin;
    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        loginViewModel = ViewModelProviders.of(LoginActivity.this).get(LoginViewModel.class);

        inputValidation = new InputValidation(this);
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromDB();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void verifyFromDB() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if(textInputEditTextEmail.getText().toString().trim().contains("@")) {
            if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
                return;
            }
        }else{
            if (!inputValidation.isValidPhoneNumber(textInputEditTextEmail,
                    textInputLayoutEmail, getString(R.string.error_message_phone))) {
                return;
            }
        }

        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
      new UserAsyncTask(LoginActivity.this,textInputEditTextEmail.getText().toString().trim(),
              textInputEditTextPassword.getText().toString().trim(),loginViewModel,nestedScrollView).execute();
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
    private static class UserAsyncTask extends AsyncTask<Void, Void, Integer> {

        //Prevent leak
        private WeakReference<Activity> weakActivity;
        private String email;
        private String password;
        private LoginViewModel loginViewModel;
        private NestedScrollView nestedScrollView;
        private ProgressDialog progressDialog;

        public UserAsyncTask(Activity activity,String email, String password,
                             LoginViewModel loginViewModel,
                             NestedScrollView nestedScrollView) {
            weakActivity = new WeakReference<>(activity);
            this.password = password;
            this.email = email;
            this.loginViewModel = loginViewModel;
            this.nestedScrollView = nestedScrollView;
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage("please wait! while loading...");

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return loginViewModel.checkUserExist(email,password);
        }

        @Override
        protected void onPostExecute(Integer userCount) {
            Activity activity = weakActivity.get();
            if(progressDialog!=null&&progressDialog.isShowing())
                progressDialog.dismiss();

            if(activity == null) {
                return;
            }
            if (userCount>0) {
                Intent accountsIntent = new Intent(activity, ViewForms.class);
                accountsIntent.putExtra("EMAIL",email);
                activity.startActivity(accountsIntent);
            } else {
                // Snack Bar to show success message that record is wrong
                Snackbar.make(nestedScrollView, activity.getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
            }
        }
    }

}