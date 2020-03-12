package com.kulartist.tekhubandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import com.kulartist.tekhub.ItemList;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    Handler handler;
    public static String currentIP="192.168.0.102";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sp = getSharedPreferences("saveUser" ,Context.MODE_PRIVATE);
                String highScore = sp.getString("userSavedId", "");
                LoginActivity.currentUser=highScore;

                System.out.println(LoginActivity.currentUser+"@@@@@@@@@@@@@########"+highScore);


                if(highScore.equals("")) {
                    Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    LoginActivity.currentUser=highScore;
                    Intent intent = new Intent(SplashScreen.this, ItemList.class);
                    startActivity(intent);
                }
            }
        },2500);

    }
}