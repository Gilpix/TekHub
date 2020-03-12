package com.kulartist.tekhubadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;

import java.util.ArrayList;

public class ItemListAdapter extends BaseAdapter {

    private ArrayList<String> itemId;
    private ArrayList<String> itemName;
    private ArrayList<String> itemDesc;
    private ArrayList<String> isAvailable;
    private ArrayList<String> availableDate;
    private ArrayList<String> itemCondition;
    private ArrayList<String> borrowNumber;
    private ArrayList<String> itemAddedDate;

    private LayoutInflater inflter;

    public ItemListAdapter(Context context,ArrayList<String> itemId, ArrayList<String> itemName, ArrayList<String> itemDesc, ArrayList<String> isAvailable, ArrayList<String> availableDate, ArrayList<String> itemCondi, ArrayList<String> borrowNum, ArrayList<String> addedDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDesc = itemDesc;
        this.isAvailable = isAvailable;
        this.availableDate = availableDate;
        this.itemCondition = itemCondi;
        this.borrowNumber = borrowNum;
        this.itemAddedDate = addedDate;
        inflter = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return itemId.size();
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

        view = inflter.inflate(R.layout.admin_item_list, null);


        TextView id = (TextView) view.findViewById(R.id.adminItemId);
        TextView name = (TextView) view.findViewById(R.id.adminItemName);
        TextView desc = (TextView) view.findViewById(R.id.adminItemDesc);
        TextView isAvail = (TextView) view.findViewById(R.id.adminItemIsAvail);
        TextView availDate = (TextView) view.findViewById(R.id.adminItemAvailDate);
        TextView itemCondi = (TextView) view.findViewById(R.id.adminItemCondition);
        TextView borrowNum = (TextView) view.findViewById(R.id.adminItemBorrowNum);
        TextView addedDate = (TextView) view.findViewById(R.id.adminItemAddedDate);

        id.setText(itemId.get(i));
        name.setText(itemName.get(i));
        desc.setText(itemDesc.get(i));
        isAvail.setText(isAvailable.get(i));
        availDate.setText(availableDate.get(i));
        itemCondi.setText(itemCondition.get(i));
        borrowNum.setText(borrowNumber.get(i));
        addedDate.setText(itemAddedDate.get(i));
        return view;
    }
}
