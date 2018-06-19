package com.example.shubham.navigationdrawer;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shubham.navigationdrawer.CallFragment;
import com.example.shubham.navigationdrawer.GroupFragment;
import com.example.shubham.navigationdrawer.PeopleFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Open extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
   // private TabLayout tabLayout;
  //  private ViewPager viewPager;
    public NavigationView navigationView;
    UserSessionManager session;
    private ListView listView;
    public static final String DATA_URL = "http://tiredbuzz.16mb.com/restraunts/restraunt.php?city=";
    public static final String URL_LEFT = "&location=";
    String url,city1,location1;
    TextView  count;
    String restname,cityloc,type,image;
    RestrauntLocationManagerPref ses;
    TextView signIn,user_name;
    String sessionemail,fullName;
    AllSharedPref sess;

 //   private CollapsingToolbarLayout collapsingToolbarLayout = null;


    private int[] tabIcons = {
            R.drawable.photoblack,
            R.drawable.videosblack,
            R.drawable.songblack
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open);





//        count = (TextView)findViewById(R.id.count);
//
      session = new UserSessionManager(getApplicationContext());
//        toolbar = (Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.openOptionsMenu();
//
//
//        listView = (ListView) findViewById(R.id.listView);
//        Intent intent = getIntent();
//        city1 = intent.getStringExtra("city");
//        location1 = intent.getStringExtra("location");
//        actionBar.setTitle(city1);
//        getData();


          ses= new RestrauntLocationManagerPref(getApplicationContext());




      //  collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctoolbar);


          mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
          navigationView = (NavigationView) findViewById(R.id.navigation_view);
          View header = navigationView.getHeaderView(0);
        signIn = (TextView) header.findViewById(R.id.txtSignin);
        user_name = (TextView) header.findViewById(R.id.user_name);

        if(session.isUserLoggedIn()){
            HashMap<String, String> user = session.getUserDetails();
            sessionemail = user.get(UserSessionManager.KEY_ONLYMAIL);
            fullName = user.get(UserSessionManager.KEY_NAME);
            signIn.setText(sessionemail);
            signIn.setTextColor(getResources().getColor(R.color.dark_black));
            user_name.setText(fullName);
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent  = new Intent(Open.this,Account.class);
                    startActivity(intent);
                }
            });
        }
        else{
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Open.this,LoginFrom.class);
                    startActivity(i);
                }
            });
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                mDrawerLayout.closeDrawers();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {


                    case R.id.adress:
                        Intent adrs = new Intent(Open.this,Address.class);
                        startActivity(adrs);
                        break;

                    case R.id.restaraunts:
                        HashMap<String, String> loc = ses.getLocaDetails();
                        String sessioncityname = loc.get(RestrauntLocationManagerPref.KEY_CITY);
                        String sessionlocation = loc.get(RestrauntLocationManagerPref.KEY_LOCATION);
                        Intent i = new Intent(Open.this,Base.class);
                        i.putExtra("city",sessioncityname);
                        i.putExtra("location",sessionlocation);
                        startActivity(i);
                        break;

                    case R.id.payments:

                        break;

                    case R.id.orders:

                        break;

                    case R.id.account:

                        if(session.isUserLoggedIn()){
                            Intent ac = new Intent(Open.this,Account.class);
                            startActivity(ac);
                        }else {
                            Intent account = new Intent(Open.this, LoginFrom.class);
                            startActivity(account);
                        }
                        break;

                    case R.id.address_book:
                            Intent intent = new Intent(Open.this,Address_Book.class);
                            startActivity(intent);
                            break;

                    case R.id.offers:
                        Intent offer = new Intent(Open.this,Offers.class);
                        startActivity(offer);
                        break;

                    case R.id.invite:
                        Intent invite = new Intent(Open.this,Invite_Friends.class);
                        startActivity(invite);
                        break;

                    case R.id.chat:
                        Toast.makeText(Open.this, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.feedback:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto: sa159871@gmail.com"));
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FoodBck LInk");
                        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                        break;
                }

                return true;
            }
        });

       // viewPager = (ViewPager) findViewById(R.id.viewpager);
       // setupViewPager(viewPager);

       // tabLayout = (TabLayout) findViewById(R.id.tablayout);
       // tabLayout.setupWithViewPager(viewPager);

        /* To set up tab icons */
       // setupTabIcons();

    }

//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new PeopleFragment(), "People");
//        adapter.addFragment(new GroupFragment(), "Group");
//        adapter.addFragment(new CallFragment(), "Calls");
//        viewPager.setAdapter(adapter);
//    }


//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//     }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
       SearchManager searchManager =
               (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       SearchView searchView =
               (SearchView) menu.findItem(R.id.search).getActionView();
//       searchView.setSearchableInfo(
//               searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search:
                return true;
            case R.id.settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

