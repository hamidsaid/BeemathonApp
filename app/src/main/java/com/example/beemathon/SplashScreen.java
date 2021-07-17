package com.example.beemathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    private Button button;

    //Variable
    Animation topAnim, bottomAnim;
    ImageView image;
    TextView open_text, open_text2;
    TextView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);


        button = findViewById(R.id.get_started);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashScreen.this, Login.class);
                startActivity(intent);

            }
        });
        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.motor_bike);
        logo = findViewById(R.id.logo);
        open_text = findViewById(R.id.splash_screen);
        open_text2 = findViewById(R.id.splash_screen2);

        image.setAnimation(topAnim);
        logo.setAnimation(topAnim);
        open_text.setAnimation(bottomAnim);
        open_text2.setAnimation(bottomAnim);
    }
}