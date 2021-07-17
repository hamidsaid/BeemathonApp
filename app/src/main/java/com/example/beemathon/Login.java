package com.example.beemathon;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    TextView createAccountView;
    TextInputLayout password_login;
    EditText phone_login;
    Button login;
    String pass_word;
    String number, Phonenumber;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    //url to connect my phone/emulator to the server
    private String BASE_URL = "http://192.168.1.53:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
       //instantiating a retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

       //getting the textviews
        createAccountView = findViewById(R.id.createAccount);
        phone_login = findViewById(R.id.editTextPhone);
        password_login = findViewById(R.id.passwordInput);
        login = findViewById(R.id.login_phone);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pass_word = password_login.getEditText().getText().toString().trim();
                number = phone_login.getText().toString().trim();

              //isValid is function to validate the users input
                if (isValid()){

                final ProgressDialog mDialog = new ProgressDialog(Login.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Logging in...");
                mDialog.show();


                    HashMap<String, String> map = new HashMap<>();

                    //testing with my mobile No 0653448900
                    map.put("mobile", number);
                    map.put("password", pass_word);

                 //Retrofit object to send data to the node server
                    Call<LoginResult> call = retrofitInterface.executeLogin(map);

                    //execute the http request to the server
                    call.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                            if (response.code() == 200) {
                                mDialog.dismiss();
                                //Welcome
                                Intent intent = new Intent(Login.this,VerifyPhone.class);
                                startActivity(intent);
                                finish();

                            } else if (response.code() == 404) {
                                mDialog.dismiss();
                                Toast.makeText(Login.this, "Wrong Credentials",
                                        Toast.LENGTH_LONG).show();

                            }

                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            mDialog.dismiss();
                            Toast.makeText(Login.this, t.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    });


                }

            }
        });



        createAccountView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }


        });


    }


    public boolean isValid() {

        boolean isValid;
        boolean isValidNumber = false;
        boolean isValidPass = false;

        if (number.isEmpty()) {
            Toast.makeText(Login.this, "Enter your phone number", Toast.LENGTH_SHORT).show();

        } else {

            if (number.length() < 9) {
                Toast.makeText(Login.this, "Enter a valid Phone number. Ex: 65344xxxx", Toast.LENGTH_SHORT).show();
            } else {
                isValidNumber = true;
            }
        }

        if (pass_word.isEmpty()) {
            Toast.makeText(Login.this, "Enter a password", Toast.LENGTH_SHORT).show();

        }else {
            isValidPass = true;
        }
        isValid = isValidNumber && isValidPass;
        return isValid;

    }
}