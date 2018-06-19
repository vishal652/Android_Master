package com.example.shubham.navigationdrawer;


import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;


public class RestList extends ArrayAdapter<String> {
    private String[] restname;
    private String[] location;
    private String[] image;
    private String[] type;
    private String[] deliverytime;
    private Activity context;
    private ImageLoader imageLoader;


    public RestList(Activity context, String[] restname, String[] location,String[] image,String[] type, String[] deliverytime) {
        super(context, R.layout.rest_list_new, restname);
        this.context = context;
        this.restname = restname;
       // this.city = city;
        this.image = image;
        this.location = location;
        this.type= type;
        this.deliverytime= deliverytime;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.rest_list_new, null, true);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();

        TextView textViewRestname = (TextView) listViewItem.findViewById(R.id.idrestname);
      //  TextView textViewCity = (TextView) listViewItem.findViewById(R.id.restcityname);
        TextView textViewLocation = (TextView) listViewItem.findViewById(R.id.restcitylocation);
        NetworkImageView thumbNail = (NetworkImageView) listViewItem.findViewById(R.id.restimage);
        TextView textViewType = (TextView) listViewItem.findViewById(R.id.type);
        TextView textViewTime = (TextView)listViewItem.findViewById(R.id.deliverytime);


        textViewRestname.setText(restname[position]);
      //  textViewCity.setText(city[position]);
        textViewLocation.setText(" ("+location[position]+")");
        thumbNail.setImageUrl(image[position], imageLoader);
        textViewTime.setText("delivers in " + deliverytime[position]);
        textViewType.setText(type[position]);


        return listViewItem;
    }
}