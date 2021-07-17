package com.example.beemathon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    Button button;
    TextView amount;
    String cost, mobile , refNum;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    //url to connect my phone/emulator to the server
    private String BASE_URL = "http://192.168.1.53:7000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navigationView = findViewById(R.id.bottom_navigatio);
        navigationView.setOnNavigationItemSelectedListener(this);

        amount = findViewById(R.id.amount);


        //instantiating a retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


         button = findViewById(R.id.checkout);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 //PROCESSING PAYMENT

                 //hardcoding amount for testing purposes
                 cost = "100";
                 mobile = "255653448900";
                 refNum = "SAID-2244";
                 /*
                  * The following method we are using to generate a random string everytime
                  * As we need a unique transaction id
                  * */
                 String uuid = UUID.randomUUID().toString();

                 //putting details together
                 HashMap<String , String> map = new HashMap<>();
                 map.put("mobile" , mobile);
                 map.put("amount" , cost);
                 map.put("ref_number" , refNum);
                 map.put("transactionId" , uuid);

                 //Parsing to json for sending to server vis /payment route
                 Call<Void> call = retrofitInterface.executePayment(map);

                 call.enqueue(new Callback<Void>() {
                     @Override
                     public void onResponse(Call<Void> call, Response<Void> response) {
                         //Code to execute if successful
                         if (response.code() == 200) {

                             Toast.makeText(Home.this,
                                     "Payment was successful! \n Your order is on the way!", Toast.LENGTH_LONG).show();

                             Intent intent = new Intent(Home.this, ThankYou.class);
                             startActivity(intent);
                             finish();

                         } else if (response.code() == 400) {
                             Toast.makeText(Home.this,
                                     "Payment Request Failed", Toast.LENGTH_LONG).show();
                         }
                     }

                     @Override
                     public void onFailure(Call<Void> call, Throwable t) {
                         Toast.makeText(Home.this, t.getMessage(),
                                 Toast.LENGTH_LONG).show();
                     }
                 });

             }
         });

    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.cart:
                fragment = new cartFragment();
                break;

            case R.id.Profile:
                fragment = new profileFragment();
                break;



        }

        return loadFragment(fragment);

    }

}
