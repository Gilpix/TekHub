package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.kulartist.tekhubandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.kulartist.tekhubandroid.SplashScreen.currentIP;
public class StudentList extends AppCompatActivity {

    private  ListView studentList;
    private ProgressDialog progressDialog;
    private JSONArray studentListArray;
    private String userStatus;
    private SearchView simpleSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        studentList=(ListView)findViewById(R.id.studentListListView);
        simpleSearchView = (SearchView) findViewById(R.id.studentListSearchBar);


        progressDialog = new ProgressDialog(this);

        getSupportActionBar().hide();

        new getStudentList().execute();


        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    new StudentList.getStudentList().execute();                }
                else
                    callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                //Do searching
                new StudentList.getStudentBySearch().execute();
            }

        });
    }

    public void backdashh(View view) {
        Intent i=new Intent(StudentList.this,Dashboard.class);
        startActivity(i);
        finish();
    }


    private class getStudentList extends AsyncTask<Void, Void, Void> {

        String userStatus;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {

                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/listStudents");

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj = new JSONObject(response.toString());
                userStatus = "" + obj.getString("Status");
                studentListArray = obj.getJSONArray("studentList");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            if(userStatus.equals("Ok")){
                try {
                    getRecyclerData(studentListArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else
                Toast.makeText(StudentList.this,"Unable to fetch",Toast.LENGTH_LONG).show();
            super.onPostExecute(result);
        }
    }

    private void getRecyclerData(final JSONArray mainArray) throws JSONException {
        final ArrayList<String> studentId = new ArrayList<String>(mainArray.length());
        final ArrayList<String> studentName = new ArrayList<String>(mainArray.length());
        final ArrayList<String> studentEmail = new ArrayList<String>(mainArray.length());

        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            studentId.add(a.getString("StudentId"));
            studentName.add(a.getString("Name"));
            studentEmail.add(a.getString("email"));

        }

        studentList = (ListView)findViewById(R.id.studentListListView);
        final StudentListAdapter studentListAdapter=new StudentListAdapter(StudentList.this,studentId,studentName,studentEmail);
        studentList.setAdapter(studentListAdapter);

        studentList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int which_position=position;
                new AlertDialog.Builder(StudentList.this).setIcon(R.drawable.ic_delete).
                        setTitle("Are you sure?").setMessage("Do you want to remove this student").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number=""+studentId.get(which_position);
                                new StudentList.studentDelete(number).execute();
                                studentId.remove(which_position);
                                studentName.remove(which_position);
                                studentEmail.remove(which_position);
                                studentListAdapter.notifyDataSetChanged();

                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });

    }


    private class studentDelete extends AsyncTask<Void, Void, Void> {

        String studentIdToDelete;
        public studentDelete(String studentIdToDelete) {
            this.studentIdToDelete = studentIdToDelete;
        }

        @Override
        protected void onPreExecute() {
            System.out.println(studentIdToDelete);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {


                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/deleteStudent&"+studentIdToDelete);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj = new JSONObject(response.toString());
                userStatus = "" + obj.getString("Status");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if(userStatus.equals("Ok")) {
                Toast.makeText(StudentList.this,"User Removed from database",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(StudentList.this,"Removel failed",Toast.LENGTH_SHORT).show();

            super.onPostExecute(result);
        }
    }


    private class getStudentBySearch extends AsyncTask<Void, Void, Void> {
        String searchKey=simpleSearchView.getQuery().toString();

        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {

                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/searchStudent&"+searchKey);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput = new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());
                JSONObject obj = new JSONObject(response.toString());
                userStatus = "" + obj.getString("Status");
                studentListArray = obj.getJSONArray("studentList");


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result) {
            if(userStatus.equals("Ok")) {
                try {
                    getRecyclerData(studentListArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else{
                progressDialog.hide();
            Toast.makeText(StudentList.this,"Unable to fetch",Toast.LENGTH_LONG).show();
            }


            super.onPostExecute(result);

        }
    }
}
