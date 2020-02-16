package com.kulartist.tekhub;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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



public class ItemList extends BottomMenu {

    private FloatingActionButton fab_main, fab1_mail, fab2_share;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;
    TextView textview_mail, textview_share,filter_available_fab;

    Boolean isOpen = false;
    ProgressDialog progressDialog;

    SearchView searchView;



    TextView itmName;





    ListView   itemListView;;
    JSONArray itemListArray= new JSONArray();
    JSONObject itemListObject=new JSONObject();







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        getLayout(R.layout.activity_item_list);
        getMenuIcon(R.drawable.home,R.id.itemlist);
        // setActionBarTitle("My Profile");


        itmName=findViewById(R.id.item_name);
        searchView=findViewById(R.id.search_bar);



        fab_main = findViewById(R.id.fab);
//        fab1_mail = findViewById(R.id.fab1);
//        fab2_share = findViewById(R.id.fab2);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);

        textview_mail = (TextView) findViewById(R.id.textview_mail);
        textview_share = (TextView) findViewById(R.id.textview_share);
        filter_available_fab=findViewById(R.id.filter_available_fab);


        progressDialog=new ProgressDialog(this);

        new GetItemList().execute();









        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);


                return true;
            }

            public void callSearch(String query) {
                //Do searching
                new getItemListBySearch().execute();
            }

        });






    }

    public void filterByAvailablity(View view) {
        Toast.makeText(getApplicationContext(), "filterByAvailablity", Toast.LENGTH_SHORT).show();
        new getItemListByAvailablity().execute();
    }

    public void filterByRatings(View view) {
        Toast.makeText(getApplicationContext(), "filterByRatings", Toast.LENGTH_SHORT).show();
        new GetItemListByPopularity().execute();

    }

    public void filterByNewestItemsAdded(View view) {
        Toast.makeText(getApplicationContext(), "filterByNewestItemsAdded", Toast.LENGTH_SHORT).show();
        new getItemListByNewestItem().execute();

    }

    public void fliterOptions(View view) {
        if (isOpen) {

            textview_mail.setVisibility(View.INVISIBLE);
            filter_available_fab.setVisibility(View.INVISIBLE);
            textview_share.setVisibility(View.INVISIBLE);
            textview_share.startAnimation(fab_close);
            filter_available_fab.startAnimation(fab_close);
            textview_mail.startAnimation(fab_close);
            fab_main.startAnimation(fab_anticlock);
            textview_share.setClickable(false);
            filter_available_fab.setClickable(false);
            textview_mail.setClickable(false);
            isOpen = false;
        } else {
            textview_mail.setVisibility(View.VISIBLE);
            filter_available_fab.setVisibility(View.VISIBLE);
            textview_share.setVisibility(View.VISIBLE);
            textview_share.startAnimation(fab_open);
            filter_available_fab.startAnimation(fab_open);
            textview_mail.startAnimation(fab_open);
            fab_main.startAnimation(fab_clock);
            textview_share.setClickable(true);
            filter_available_fab.setClickable(true);
            textview_mail.setClickable(true);
            isOpen = true;
        }
    }

    public void checkItemDetails(View view) {
        Intent in = new Intent(getBaseContext(), Item.class);//Restaurant
        startActivity(in);
    }








    public void getRecyclerData(final JSONArray mainArray) throws JSONException {

        final String[] itemName=new String[mainArray.length()];
        final String[] itemAvailable=new String[mainArray.length()];
       final String[] itemID=new String[mainArray.length()];

        final String[] itemDesc=new String[mainArray.length()];
        final String[] availableDate=new String[mainArray.length()];
        final String[] itemCondition=new String[mainArray.length()];
        final String[] borrowNum=new String[mainArray.length()];
        final String[] addedDate=new String[mainArray.length()];





        for(int j=0;j<mainArray.length();j++)
        {
            JSONObject a =new JSONObject();
            a=mainArray.getJSONObject(j);
            itemName[j]=a.getString("itemname");
            itemAvailable[j]=a.getString("isAvailable");
            itemID[j]=a.getString("itemId");

            itemDesc[j]=a.getString("itemDesc");
            availableDate[j]=a.getString("availableDate");
            itemCondition[j]=a.getString("itemCondition");
            borrowNum[j]=a.getString("borrowNum");
            addedDate[j]=a.getString("addedDate");
        }


        itemListView = (ListView)findViewById(R.id.simpleListView);
        ItemListAdapter customAdapter = new ItemListAdapter(getApplicationContext(), itemName,itemAvailable);
        itemListView.setAdapter(customAdapter);

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                {
                    Intent in = new Intent(ItemList.this, Item.class);
                    //in.putExtra("ITEMID",itemName[position]);
                    try {
                        System.out.println("^^^^^^^^^^^^^^@@@@@^^^^^^^ "+mainArray.getJSONObject(position).toString());
                        in.putExtra("ItemDetailsObject",mainArray.getJSONObject(position).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(in);
                    //finish();

                }

            }
        });



    }




    private class GetItemList extends AsyncTask<Void, Void, Void> {

        String userStatus;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://192.168.0.102:8080/TekHubWebCalls/webcall/item/getItemList");
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();



                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                itemListArray=obj.getJSONArray("itemLists");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }





    private class GetItemListByPopularity extends AsyncTask<Void, Void, Void> {

        String userStatus;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://192.168.0.102:8080/TekHubWebCalls/webcall/item/getItemListByPopularity");
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();



                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                itemListArray=obj.getJSONArray("itemLists");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }






    private class getItemListByNewestItem extends AsyncTask<Void, Void, Void> {

        String userStatus;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://192.168.0.102:8080/TekHubWebCalls/webcall/item/getItemListByNewestItem");
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();



                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                itemListArray=obj.getJSONArray("itemLists");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }







    private class getItemListByAvailablity extends AsyncTask<Void, Void, Void> {

        String userStatus;


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://192.168.0.102:8080/TekHubWebCalls/webcall/item/getItemListByAvailablity");
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();



                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                itemListArray=obj.getJSONArray("itemLists");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }







    private class getItemListBySearch extends AsyncTask<Void, Void, Void> {

        String userStatus,searchKey=searchView.getQuery().toString();


        @Override
        protected void onPreExecute() {


            progressDialog.setMessage("Loading...");
            progressDialog.show();

            super.onPreExecute();
        }


        @Override
        protected Void doInBackground(Void... params){

            URL url = null;

            try {

                url = new URL("http://192.168.0.102:8080/TekHubWebCalls/webcall/item/getItemListBySearch&"+searchKey);
                // url = new URL("http://192.168.2.250:8080/OnlineQuiz/mad312group2/quizuser/userLogin&"+mailAdd+"&"+passwrd);

                HttpURLConnection client = null;

                client = (HttpURLConnection) url.openConnection();

                client.setRequestMethod("GET");

                int responseCode = client.getResponseCode();


                System.out.println("\n Sending 'GET' request to URL : " + url);

                System.out.println("Response Code : " + responseCode);
                InputStreamReader myInput= new InputStreamReader(client.getInputStream());

                BufferedReader in = new BufferedReader(myInput);
                String inputLine;
                StringBuffer response = new StringBuffer();



                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //print result
                System.out.println(response.toString());

                JSONObject obj =new JSONObject(response.toString());
                userStatus=""+obj.getString("Status");
                itemListArray=obj.getJSONArray("itemLists");


            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void result){

            try {
                getRecyclerData(itemListArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Toast.makeText(ScoreCard.this,"Login Successful - " +userStatus,Toast.LENGTH_SHORT).show();
            progressDialog.hide();
            super.onPostExecute(result);
        }
    }






}







