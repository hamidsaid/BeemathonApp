package com.example.beemathon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {


    TextInputLayout userNameField;
    TextInputLayout passwordField;
    TextInputLayout confirmPassField;
    TextInputLayout phoneField;
    Button signUp;

    String UserName;
    String Password;
    String ConfirmPass;
    String mobile;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    //url to connect my phone/emulator to the server
    private String BASE_URL = "http://192.168.1.53:3000";

    //192.168.1.52
    //10.0.2.2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        //instantiating a retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        userNameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        confirmPassField = findViewById(R.id.confirm_password);
        phoneField = findViewById(R.id.signup_phone);
        signUp = findViewById(R.id.signup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UserName = userNameField.getEditText().getText().toString().trim();
                Password = passwordField.getEditText().getText().toString().trim();
                ConfirmPass = confirmPassField.getEditText().getText().toString().trim();
                mobile = phoneField.getEditText().getText().toString().trim();

                if (isValid()) {

                    //A dialog for registering
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registering Please wait...");
                    mDialog.show();

                    HashMap<String, String> map = new HashMap<>();

                    final String mobileNo = "255" + mobile;

                    map.put("name", UserName);
                    map.put("mobile", mobileNo);
                    map.put("password", Password);

                    Call<Void> call = retrofitInterface.executeSignup(map);

                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.code() == 200) {
                                   mDialog.dismiss();
                                Toast.makeText(SignUp.this,
                                        "Signed up successfully", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(SignUp.this, VerifyPhone.class);
                                startActivity(intent);
                                finish();

                            } else if (response.code() == 400) {
                                Toast.makeText(SignUp.this,
                                        "Already registered", Toast.LENGTH_LONG).show();
                                mDialog.dismiss();
                            }

                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(SignUp.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    });
                }
            }
        });

    }

    public boolean isValid() {

        userNameField.setErrorEnabled(false);
        userNameField.setError("");
        passwordField.setErrorEnabled(false);
        passwordField.setError("");
        confirmPassField.setErrorEnabled(false);
        confirmPassField.setError("");
        phoneField.setErrorEnabled(false);
        phoneField.setError("");

        boolean isValidUsername = false, isValidEmail = false, isValidPassword = false, isValid = false, isValidConfirmPass = false, isValidMobile = false;

        if (TextUtils.isEmpty(UserName)) {
            userNameField.setErrorEnabled(true);
            userNameField.setError("UserName is required");
        } else {
            isValidUsername = true;
        }

        if (TextUtils.isEmpty(Password)) {
            passwordField.setErrorEnabled(true);
            passwordField.setError("A password is required");
        } else {
            if (Password.length() < 6) {
                passwordField.setErrorEnabled(true);
                passwordField.setError("password too weak");
            } else {
                isValidPassword = true;
            }
        }

        if (TextUtils.isEmpty(ConfirmPass)) {

            confirmPassField.setErrorEnabled(true);
            confirmPassField.setError("Confirm Password is required");
        } else {
            if (!Password.equals(ConfirmPass)) {
                confirmPassField.setErrorEnabled(true);
                confirmPassField.setError("Password does not match");
            } else {
                isValidConfirmPass = true;
            }
        }

        if (TextUtils.isEmpty(mobile)) {

            phoneField.setErrorEnabled(true);
            phoneField.setError("Mobile Number is required");
        } else {
            if (mobile.length() < 9 || mobile.length() > 9) {
                phoneField.setErrorEnabled(true);
                phoneField.setError("Invalid Mobile Number");
            } else {
                isValidMobile = true;
            }
        }

        isValid = (isValidConfirmPass && isValidUsername && isValidPassword && isValidMobile);
        return isValid;

    }

}