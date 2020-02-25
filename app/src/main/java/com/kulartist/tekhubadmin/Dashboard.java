package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

        
    }
}
