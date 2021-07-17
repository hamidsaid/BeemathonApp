package com.example.beemathon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VerifyPhone extends AppCompatActivity {


    Button verify, Resend;
    TextView txt;
    EditText entercode;
    String pinCode;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;

    //url to connect my phone/emulator to the server
    private String BASE_URL = "http://192.168.1.53:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_verify_phone);

        //instantiating a retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);

        entercode = findViewById(R.id.phoneno);
        txt = findViewById(R.id.text);
        Resend = findViewById(R.id.Resendotp);
        verify = findViewById(R.id.Verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog = new ProgressDialog(VerifyPhone.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Verifying Please wait...");
                mDialog.show();

                pinCode = entercode.getText().toString().trim();


                HashMap<String, String> map = new HashMap<>();

                map.put("pinCode", pinCode);
                //Retrofit object to send data to the node server
                Call<Void> call = retrofitInterface.executeVerify(map);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {

                        if (response.code() == 200) {
                            mDialog.dismiss();
                            //Welcome
                            Toast.makeText(VerifyPhone.this, "Welcome! We await your order",
                                    Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(VerifyPhone.this, Home.class);
                            startActivity(intent);
                            finish();


                        } else if (response.code() == 404) {
                            Toast.makeText(VerifyPhone.this, "Wrong OTP entered. Try Again!",
                                    Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        } else{
                            Toast.makeText(VerifyPhone.this, "Something went wrong. Try again Later!",
                                    Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }

                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(VerifyPhone.this,"onFailure executed"+ t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });



            }
        });

    }
}