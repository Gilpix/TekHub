package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kulartist.tekhubandroid.R;

public class Dashboard extends AppCompatActivity {

    private Button item,students,issue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        item=(Button)findViewById(R.id.manageItems);
        students=(Button)findViewById(R.id.manageStudents);
        issue=(Button)findViewById(R.id.resolveIssue);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,StudentList.class);
                startActivity(i);
            }
        });


        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Dashboard.this,ResolveIssue.class);
                startActivity(i);
            }
        });
        
    }
}
