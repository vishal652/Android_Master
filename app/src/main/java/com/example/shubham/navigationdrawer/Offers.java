package com.example.shubham.navigationdrawer;

/**
 * Created by Shubham on 24-06-2016.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Offers extends Open implements RecyclerView.OnScrollChangeListener {
    Toolbar toolbar;
    private List<OffersList> listOfferLists;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RequestQueue requestQueue;
    private int requestCount = 1;
    RestrauntLocationManagerPref session;
    UserSessionManager sess;
    String city , location,activ;
    AllSharedPref all;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.offers, null, false);
        mDrawerLayout.addView(contentView, 0);
        navigationView.getMenu().getItem(6).setChecked(true);
     //   navigationView.getMenu().setGroupVisible(R.id.g1, true);

        session= new RestrauntLocationManagerPref(getApplicationContext());
        sess = new UserSessionManager(getApplicationContext());
        all = new AllSharedPref(getApplicationContext());


        toolbar = (Toolbar) findViewById(R.id.toolbar_offers);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_menu);

        SharedPreferences myPrefs = this.getSharedPreferences("Loc", MODE_WORLD_READABLE);
        city = myPrefs.getString("city1",null);
        location = myPrefs.getString("location1",null);

        if (location != null && city != null )
        {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
        }
        else {
            navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
        }

        if(sess.isUserLoggedIn()){
            navigationView.getMenu().findItem(R.id.address_book).setVisible(true);
        }
        else{
            navigationView.getMenu().findItem(R.id.address_book).setVisible(false);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        listOfferLists = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new CardAdapter(listOfferLists, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.DATA_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(Offers.this, "Fetching Exciting Offers", Toast.LENGTH_SHORT).show();
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(Offers.this, "No More Offers Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    public void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            //Creating the superhero object
            OffersList offers = new OffersList();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                offers.setImageUrl(json.getString(Config.TAG_IMAGE_URL));
                offers.setFlat(json.getString(Config.TAG_FLAT));
                offers.setAmount(json.getString(Config.TAG_AMOUNT));
            } catch (JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            listOfferLists.add(offers);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                // session.createRestSession(city1,location1);
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        HashMap<String, String> loc = session.getLocaDetails();
        HashMap<String, String> act = all.getActivityName();
        activ = act.get(AllSharedPref.KEY_ACTIVITYNAME);
        if (activ.equals("Address")||activ.equals("Login_Form")||activ.equals("Account")||activ.equals("Address_Book")) {

            if (location != null && city != null )
            {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(true);
                all.ActName("Address");
                String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                Intent intent = new Intent(Offers.this, Base.class);
                intent.putExtra("city", sessioncityname);
                intent.putExtra("location", sessionlocation);
                startActivity(intent);
            }
            else {
                navigationView.getMenu().findItem(R.id.restaraunts).setVisible(false);
                Intent intent = new Intent(Offers.this,Address.class);
                startActivity(intent);
            }


        }

    }
}