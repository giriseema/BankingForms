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
import android.widget.Toast;

import com.giriseematechme.bankingforms.RoomDatabase.LoginTable;
import com.giriseematechme.bankingforms.ViewModel.LoginViewModel;
import com.giriseematechme.bankingforms.utils.AESUtils;
import com.giriseematechme.bankingforms.utils.InputValidation;
import com.giriseematechme.bankingforms.view.ViewForms;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private NestedScrollView nestedScrollView;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;
    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    private InputValidation inputValidation;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        loginViewModel = ViewModelProviders.of(SignupActivity.this).get(LoginViewModel.class);

        initViews();
        inputValidation = new InputValidation(this);
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }
    /**
     * This method is to initialize views
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);
        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);
        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.appCompatButtonRegister:
                postDataDB();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    private void postDataDB() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail,
                textInputLayoutEmail, getString(R.string.error_message_email_or_phone))) {
            return;
        }
        if(textInputEditTextEmail.getText().toString().trim().contains("@")) {
            if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail,
                    textInputLayoutEmail, getString(R.string.error_message_email))) {
                return;
            }
        }else{
            if (!inputValidation.isValidPhoneNumber(textInputEditTextEmail,
                    textInputLayoutEmail, getString(R.string.error_message_phone))) {
                return;
            }
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword,
                textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }

        if (!inputValidation.isValidPassword(textInputEditTextPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_validation))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword,
                textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        new UserAsyncTask(SignupActivity.this,
                textInputEditTextEmail.getText().toString().trim(),
                textInputEditTextPassword.getText().toString().trim(),
                loginViewModel,nestedScrollView).execute();
    }
    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
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
            return loginViewModel.checkUserAlreadyExist(email);
        }

        @Override
        protected void onPostExecute(Integer userCount) {
            Activity activity = weakActivity.get();
            if(progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();

            if(activity == null) {
                return;
            }

            LoginTable data = new LoginTable();
            if (userCount==0) {
                data.setEmail(email);
                // data.setPassword(textInputEditTextPassword.getText().toString().trim());
                try {
                    password = AESUtils.encrypt(password);
                    Log.d("TEST", "encrypted:" + password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                data.setPassword(password);

                loginViewModel.insert(data);
                // Snack Bar to show success message that record saved successfully
                Snackbar.make(nestedScrollView, activity.getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            } else {
                // Snack Bar to show error message that record already exists
                Snackbar.make(nestedScrollView, activity.getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
            }
        }
    }
}