package com.kulartist.tekhubadmin;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.kulartist.tekhubandroid.R;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class addItem extends AppCompatActivity {
    EditText addName, addDesc, addCond;
    ImageView addImage;
    TextView imagePath;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    String encodedImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addImage = findViewById(R.id.add_item_image);
        addName = findViewById(R.id.add_item_name);
        addDesc = findViewById(R.id.add_item_desc);
        addCond = findViewById(R.id.add_item_cond);
        imagePath = findViewById(R.id.add_item_path);
    }


    public void chooseImage(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                addImage.setImageBitmap(bitmap);
                addImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void getImageEncodedString() {
        BitmapDrawable drawable = (BitmapDrawable) addImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }



    public void submitAddItem(View view) {
        if(encodedImage.length()>=10000)
            Toast.makeText(this,"Image size is too large",Toast.LENGTH_LONG).show();
        else {
            if (addName.getText().toString().isEmpty() || addDesc.getText().toString().isEmpty() || addCond.getText().toString().isEmpty())
                Toast.makeText(this, "Please Enter all fields", Toast.LENGTH_LONG).show();
            else {
                getImageEncodedString();
                AddItems();
            }
        }
    }


    public void AddItems()  {

        String name = addName.getText().toString(), desc = addDesc.getText().toString(), cond = addCond.getText().toString();

         try {
            Connection conn = null;
            conn = DatabaseConnection.CONN();
            if (conn == null) {
                System.out.println("Please check your internet connection");
            } else {
                System.out.println("internet connection successful");
                int itemIdFromDb = 0;

                String sql;
                sql = "INSERT INTO Item VALUES(?,?,?,?,?,?,?,?,?)";

                String sql2 = "SELECT itemId FROM Item ORDER BY itemId DESC LIMIT 1";
                PreparedStatement stm1 = null;

                    stm1 = conn.prepareStatement(sql2);

                ResultSet rs = stm1.executeQuery();
                while (rs.next()) itemIdFromDb = rs.getInt("itemId");
                if (itemIdFromDb > 1) {
                    PreparedStatement stm = conn.prepareStatement(sql);
                    stm.setInt(1, itemIdFromDb + 1);
                    stm.setString(2, name);
                    stm.setString(3, desc);
                    stm.setString(4, "1");
                    stm.setDate(5, getCurrentDate());
                    stm.setString(6, cond);
                    stm.setInt(7, 0);
                    stm.setDate(8, getCurrentDate());
                    stm.setString(9,encodedImage);

                    stm.executeUpdate();
                }

                DatabaseConnection.closeConnection(conn, null, stm1);
            }

    }  catch (SQLException e) {
        e.printStackTrace();
    }}


    static java.sql.Date getCurrentDate() {

        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());

    }


    public static class DatabaseConnection {


        static String classs = "com.mysql.jdbc.Driver";
        static String url = "jdbc:mysql://tekhubinstance.co3rvmyoxbtb.ca-central-1.rds.amazonaws.com:3306/TEKHUB?autoReconnect=true&useSSL=false";
        static String un = "kuldeep";
        static String password = "Sonu1993";


        @SuppressLint("NewApi")
        public static Connection CONN() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL = null;
            try {

                Class.forName(classs);

                conn = DriverManager.getConnection(url, un, password);


                conn = DriverManager.getConnection(ConnURL);
            } catch (SQLException se) {
                Log.e("ERRO", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("ERRO", e.getMessage());
            } catch (Exception e) {
                Log.e("ERRO", e.getMessage());
            }
            return conn;
        }


        public static void closeConnection(Connection conn, ResultSet rs, PreparedStatement ps)  {


            try {
                if (rs != null)
                    rs.close();
                if (ps != null)
                    ps.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
