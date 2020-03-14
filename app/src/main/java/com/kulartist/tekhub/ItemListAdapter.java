package com.kulartist.tekhub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.kulartist.tekhubandroid.R;


public class ItemListAdapter extends BaseAdapter {
    Context context;
    String itemNames[],itemAvable[],itemImage[];

    LayoutInflater inflter;

    public ItemListAdapter(Context applicationContext, String[] categoryList,String[] itemAvable,String[] itmImage) {
        this.context = context;
        this.itemNames = categoryList;
        this.itemAvable=itemAvable;
        this.itemImage=itmImage;

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
        ImageView item_image=(ImageView) view.findViewById(R.id.item_image);

        name.setText(""+itemNames[i]);

        byte[] decodedString = Base64.decode(itemImage[i], Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        item_image.setImageBitmap(decodedByte);

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
