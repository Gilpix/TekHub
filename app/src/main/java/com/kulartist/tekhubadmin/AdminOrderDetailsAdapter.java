package com.kulartist.tekhubadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;

import java.util.ArrayList;

public class AdminOrderDetailsAdapter extends BaseAdapter {
    private ArrayList<String> orderId;
    private ArrayList<String> itemId;
    private ArrayList<String> itemName;
    private ArrayList<String> userId;
    private ArrayList<String> name;
    private ArrayList<String> orderDate;
    private ArrayList<String> pickupDate;
    private ArrayList<String> returnDate;

    private LayoutInflater inflter;


    public AdminOrderDetailsAdapter(Context context,ArrayList<String> orderId, ArrayList<String> itemId, ArrayList<String> itemName,
                                    ArrayList<String> userId, ArrayList<String> name, ArrayList<String> orderDate,
                                    ArrayList<String> pickupDate, ArrayList<String> returnDate) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.userId = userId;
        this.name = name;
        this.orderDate = orderDate;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        inflter = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return orderId.size();
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

        view = inflter.inflate(R.layout.admin_order_details, null);
        TextView orderid = (TextView) view.findViewById(R.id.adminOrderOrderId);
        TextView itemid = (TextView) view.findViewById(R.id.adminOrderItemId);
        TextView itemname = (TextView) view.findViewById(R.id.adminOrderItemName);
        TextView userid = (TextView) view.findViewById(R.id.adminOrderUserId);
        TextView studentname = (TextView) view.findViewById(R.id.adminOrderStudentName);
        TextView orderdate = (TextView) view.findViewById(R.id.adminOrderDate);
        TextView pickupdate = (TextView) view.findViewById(R.id.adminOrderPickupDate);
        TextView returndate = (TextView) view.findViewById(R.id.adminOrderRetrunDate);






        orderid.setText(orderId.get(i));
        itemid.setText(itemId.get(i));
        itemname.setText(itemName.get(i));
        userid.setText(userId.get(i));
        studentname.setText(name.get(i));
        orderdate.setText(orderDate.get(i));
        pickupdate.setText(pickupDate.get(i));
        returndate.setText(returnDate.get(i));





        return view;
    }
}
