package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import static com.kulartist.tekhubandroid.LoginActivity.currentIP;

public class ResolveIssue extends AppCompatActivity {

    private ListView issueListView;
    private ProgressDialog progressDialog;
    private JSONArray issueListArray;
    private String userStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolve_issue);
        progressDialog = new ProgressDialog(this);
        new getIssues().execute();



    }

    private class getIssues extends AsyncTask<Void, Void, Void> {

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

                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/listIssues");

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
                issueListArray = obj.getJSONArray("issueList");


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
                    System.out.println(issueListArray.toString());
                    getRecyclerData(issueListArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.hide();
            }
            else{
                Toast.makeText(ResolveIssue.this,"Unable to fetch",Toast.LENGTH_LONG).show();
                progressDialog.hide();

            }
            super.onPostExecute(result);
        }
    }



    private void getRecyclerData(final JSONArray mainArray) throws JSONException {
        final ArrayList<String> feedID = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemID = new ArrayList<String>(mainArray.length());
        final ArrayList<String> itemName = new ArrayList<String>(mainArray.length());
        final ArrayList<String> message = new ArrayList<String>(mainArray.length());


        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a;
            a=mainArray.getJSONObject(j);
            feedID.add(a.getString("feedid"));
            itemID.add(a.getString("itemId"));
            itemName.add(a.getString("itemname"));
            message.add(a.getString("message"));

        }

        issueListView=(ListView)findViewById(R.id.adminresolveissue);
        final IssueListAdapter issueListAdapter=new IssueListAdapter(ResolveIssue.this,feedID,itemID,itemName,message);
        issueListView.setAdapter(issueListAdapter);

        issueListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int which_position=position;
                new AlertDialog.Builder(ResolveIssue.this).setIcon(R.drawable.ic_delete).
                        setTitle("Have you solved Issue?").setMessage("Do you want to remove from Issue list").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String number=""+feedID.get(which_position);
                                new ResolveIssue.solveIssue(number).execute();
                                feedID.remove(which_position);
                                itemID.remove(which_position);
                                itemName.remove(which_position);
                                message.remove(which_position);
                                issueListAdapter.notifyDataSetChanged();


                            }
                        }).setNegativeButton("No",null).show();
                return true;
            }
        });
    }


    private class solveIssue extends AsyncTask<Void, Void, Void> {

        String toBeChanged;
        public solveIssue(String studentIdToDelete) {
            this.toBeChanged = studentIdToDelete;
        }

        @Override
        protected void onPreExecute() {
            System.out.println(toBeChanged);
            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params) {

            URL url = null;

            try {


                url = new URL("http://" + currentIP + ":8080/TekHub-WebCalls/webcall/admin/updateFeedback&"+toBeChanged);

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
                Toast.makeText(ResolveIssue.this,"Issue removed from list",Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(ResolveIssue.this,"Issue not-removed from list",Toast.LENGTH_SHORT).show();

            super.onPostExecute(result);
        }
    }
}
