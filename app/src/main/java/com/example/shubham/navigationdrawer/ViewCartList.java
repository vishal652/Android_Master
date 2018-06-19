package com.example.shubham.navigationdrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Shubham on 02-09-2016.
 */
public class ViewCartList extends ArrayAdapter<String> {

    private String[] cart_item_name;
    private String[] cart_item_quan;
    private String[] cart_item_price;
    private Activity context;
    Basket bskt;

    public ViewCartList(Activity context,  String[] cartitemquan, String[] cartitemname,String[] cartitemprice){
        super(context,R.layout.viewcartlist,cartitemquan);
        this.context = context;
        this.cart_item_quan = cartitemquan;
        this.cart_item_name = cartitemname;
        this.cart_item_price = cartitemprice;

        bskt = new Basket();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.viewcartlist, null, true);

        TextView textViewItemQuan = (TextView) listViewItem.findViewById(R.id.cart_quan);
        TextView textViewItemName = (TextView) listViewItem.findViewById(R.id.cart_item_name);
        TextView textViewItemPrice = (TextView) listViewItem.findViewById(R.id.cart_item_price);


        textViewItemQuan.setText(cart_item_quan[position]);
        textViewItemName.setText(cart_item_name[position]);
        textViewItemPrice.setText(cart_item_price[position]);


//        imcut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // I want to delete that particular row
//                cart_item_name[position]=null;
//                cart_item_quan[position]=null;
//                cart_item_price[position]=null;
//                notifyDataSetChanged();
//            }
//        });

        return listViewItem;
    }
}
