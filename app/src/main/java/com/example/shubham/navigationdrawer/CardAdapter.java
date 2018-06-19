package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 12-07-2016.
 */

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    //Imageloader to load image
    private ImageLoader imageLoader;
    private Context context;

    //List to store all superheroes
    List<OffersList> offersLists;

    //Constructor of this class
    public CardAdapter(List<OffersList> offersLists, Context context){
        super();
        //Getting all superheroes
        this.offersLists = offersLists;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Getting the particular item from the list
        OffersList offersList =  offersLists.get(position);

        //Loading image from url
        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(offersList.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.offerback, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(offersList.getImageUrl(), imageLoader);
        holder.textViewFlat.setText(offersList.getFlat());
        holder.textViewAmount.setText(offersList.getAmount());

    }

    @Override
    public int getItemCount() {
        return offersLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        public TextView textViewFlat;
        public TextView textViewAmount;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageViewOffer);
            textViewFlat = (TextView) itemView.findViewById(R.id.textViewFlat);
            textViewAmount = (TextView) itemView.findViewById(R.id.textViewAmount);
        }
    }
}