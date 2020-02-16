package com.kulartist.tekhub;





import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kulartist.tekhubandroid.R;


public class ItemListAdapter extends BaseAdapter {
    Context context;
    String itemNames[],itemAvable[];

    LayoutInflater inflter;

    public ItemListAdapter(Context applicationContext, String[] categoryList,String[] itemAvable) {
        this.context = context;
        this.itemNames = categoryList;
        this.itemAvable=itemAvable;

        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return itemNames.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.item_list_adapter_layout, null);
        TextView name = (TextView) view.findViewById(R.id.item_name);
        TextView avable = (TextView) view.findViewById(R.id.item_avaibl);

        name.setText("NAME : "+itemNames[i]);
        if(itemAvable[i].equals("1")) {
            avable.setTextColor(Color.parseColor("#FF00C853"));
            avable.setText("Available");
        }
        else {
            avable.setTextColor(Color.parseColor("#DD0F2D"));
            avable.setText("Not Available");
        }

        return view;
    }
}
