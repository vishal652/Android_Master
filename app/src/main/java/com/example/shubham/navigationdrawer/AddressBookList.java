package com.example.shubham.navigationdrawer;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;


public class AddressBookList extends ArrayAdapter<String> {

    private String[] uid;
    private String[] city;
    private String[] location;
    private String[] company;
    private String[] flat_no;
    private String[] apartment;
    private String[] postcode;
    private String[] other_address;
    private Activity context;

    AllSharedPref ses;



    public AddressBookList(Activity context,String[] uid ,String[] city, String[] location, String[] company, String[] flat_no, String[] apartment, String[] postcode, String[] other_address) {
        super(context, R.layout.address_book_list, uid);
        this.context = context;
        this.uid = uid;
        this.city = city;
        this.location = location;
        this.company = company;
        this.flat_no = flat_no;
        this.apartment = apartment;
        this.postcode = postcode;
        this.other_address = other_address;


        ses = new AllSharedPref(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.address_book_list, null, true);

        TextView uidn = (TextView) listViewItem.findViewById(R.id.uid);
        TextView company_add = (TextView) listViewItem.findViewById(R.id.company_add);
        TextView flatno_add = (TextView) listViewItem.findViewById(R.id.flatno_add);
        TextView apartment_add = (TextView) listViewItem.findViewById(R.id.apartment_add);
        TextView location_add = (TextView)listViewItem.findViewById(R.id.location_add);
        TextView other_add = (TextView)listViewItem.findViewById(R.id.other_add);
        TextView city_add = (TextView)listViewItem.findViewById(R.id.city_add);
        TextView post_add = (TextView)listViewItem.findViewById(R.id.postcode_add);


        uidn.setText(uid[position]);
        company_add.setText(company[position]);
        flatno_add.setText(flat_no[position]);
        apartment_add.setText(apartment[position]);
        location_add.setText(location[position]);
        other_add.setText(other_address[position]);
        post_add.setText(postcode[position]);
        city_add.setText(city[position]);



        return listViewItem;
    }
}