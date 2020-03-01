package com.kulartist.tekhubadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;

import java.util.ArrayList;

public class StudentListAdapter extends BaseAdapter {
    private ArrayList<String> studentId;
    private ArrayList<String> studentName;
    private ArrayList<String> studentEmail;

    private LayoutInflater inflter;

    public StudentListAdapter(Context context, ArrayList<String> studentId, ArrayList<String> studentName, ArrayList<String> studentEmail) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        inflter = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return studentId.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.student_list_adapter, null);
        TextView id = (TextView) view.findViewById(R.id.adapterstudentId);
        TextView name = (TextView) view.findViewById(R.id.adapterStudentName);
        TextView email = (TextView) view.findViewById(R.id.adapterStudentEmail);






        id.setText(studentId.get(i));
        name.setText(studentName.get(i));
        email.setText(studentEmail.get(i));





        return view;
    }
}
