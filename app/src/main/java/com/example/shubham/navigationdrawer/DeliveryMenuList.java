package com.example.shubham.navigationdrawer;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



public class DeliveryMenuList extends ArrayAdapter<String> {
    private String[] deliverymenu;
    private Activity context;




    public DeliveryMenuList(Activity context, String[] deliverymenu) {
        super(context, R.layout.deliverymenulist, deliverymenu);
        this.context = context;
        this.deliverymenu = deliverymenu;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.deliverymenulist, null, true);

        TextView textViewDeliveryMenu = (TextView) listViewItem.findViewById(R.id.deliverymenu);
        textViewDeliveryMenu.setText(deliverymenu[position]);
        return listViewItem;
    }
}