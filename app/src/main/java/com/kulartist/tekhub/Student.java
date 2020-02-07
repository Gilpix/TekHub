package com.kulartist.tekhub;




import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;


import android.widget.TextView;
import android.widget.Toast;

import com.kulartist.tekhubandroid.LoginActivity;
import com.kulartist.tekhubandroid.R;


public class Student extends BottomMenu {
    TextView std_name, std_id;
    EditText email, phone, gender, age;
    TextView edit_save;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_student);


        getLayout(R.layout.activity_student);
        getMenuIcon(R.drawable.profile,R.id.my_profile);
       // setActionBarTitle("My Profile");



        std_name = findViewById(R.id.student_name);
        std_id = findViewById(R.id.student_id);

        email = findViewById(R.id.student_email_text);
        phone = findViewById(R.id.student_phone_text);
        age = findViewById(R.id.student_age_text);
        gender = findViewById(R.id.student_gender_text);


        edit_save = findViewById(R.id.edit_profile_text);


        setProfileDisabled();


    }


    public void editProfileEnabled() {


        email.setEnabled(true);
        email.setClickable(true);
        email.setFocusable(true);
        email.setFocusableInTouchMode(true);


        phone.setEnabled(true);
        phone.setClickable(true);
        phone.setFocusable(true);
        phone.setFocusableInTouchMode(true);


        gender.setEnabled(true);
        gender.setClickable(true);
        gender.setFocusable(true);
        gender.setFocusableInTouchMode(true);


        age.setEnabled(true);
        age.setClickable(true);
        age.setFocusable(true);
        age.setFocusableInTouchMode(true);
        age.setBackgroundResource(android.R.color.transparent);


    }


    public void setProfileDisabled() {


        gender.setEnabled(false);
        gender.setClickable(false);
        gender.setFocusable(false);
        gender.setFocusableInTouchMode(false);
        gender.setBackgroundResource(android.R.color.transparent);


        email.setEnabled(false);
        email.setClickable(false);
        email.setFocusable(false);
        email.setFocusableInTouchMode(false);
        email.setBackgroundResource(android.R.color.transparent);


        phone.setEnabled(false);
        phone.setClickable(false);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        phone.setBackgroundResource(android.R.color.transparent);

        age.setEnabled(false);
        age.setClickable(false);
        age.setFocusable(false);
        age.setFocusableInTouchMode(false);
        age.setBackgroundResource(android.R.color.transparent);


    }


    public void editSaveProfile(View view) {


        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (email.isFocusable() == true) {
                    edit_save.setText("edit");

                    setProfileDisabled();
                    Toast.makeText(Student.this, " Profile Saved ", Toast.LENGTH_LONG).show();
                } else if (email.isFocusable() == false) {
                    edit_save.setText("save");
                    editProfileEnabled();
                }
            }
        });

    }

    public void signOut(View view) {


        Toast.makeText(Student.this, " Sign Out ", Toast.LENGTH_LONG).show();

//        Intent in = new Intent(Student.this, LoginActivity.class);
//        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(in);
//        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Student.this, Student.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    public void logOut(View view) {
        Intent i = new Intent(Student.this, LoginActivity.class);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}