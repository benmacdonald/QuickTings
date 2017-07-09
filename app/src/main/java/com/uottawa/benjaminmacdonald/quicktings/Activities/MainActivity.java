package com.uottawa.benjaminmacdonald.quicktings.Activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.benjaminmacdonald.quicktings.Classes.User;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.MainFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.SearchFragment;
import com.uottawa.benjaminmacdonald.quicktings.Fragments.SuggestionFragment;
import com.uottawa.benjaminmacdonald.quicktings.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener, SuggestionFragment.OnFragmentInteractionListener {

    private SearchView searchView;
    private SearchFragment searchFragment;


    private TextView profileName;
    private TextView profileEmail;
    private ImageView profileImage;

    private  ArrayList<String> suggestions;
    private SuggestionFragment suggestionFragment;
    private Toolbar toolbar;
    private DrawerLayout drawer;

    private ActionBarDrawerToggle toggle;

    private MenuItem cartItem;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createSuggestions();

        // hack to make maps load faster
        // Fixing Later Map loading Delay
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MapView mv = new MapView(getApplicationContext());
                    mv.onCreate(null);
                    mv.onPause();
                    mv.onDestroy();
                }catch (Exception ignored){

                }
            }
        }).start();


        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            MainFragment mainFragment = new MainFragment();
            // Intent, pass the Intent's extras to the fragment as arguments
            mainFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, mainFragment, "MAIN_FRAG");
            transaction.addToBackStack("main");
            transaction.commit();
        }
    }

    protected void onCreateDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchView != null && !searchView.isIconified()) {
                    searchView.setIconified(true);
                    if (searchView != null && !searchView.isIconified()) {
                        searchView.setIconified(true);
                    }
                    //closeSearchView();
                } else {
                    if (drawer.isDrawerOpen(Gravity.START)) {
                        drawer.closeDrawer(Gravity.START);
                    } else {
                        drawer.openDrawer(Gravity.START);
                    }
                }
            }
        });


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View header =  navigationView.getHeaderView(0);
        profileName = (TextView) header.findViewById(R.id.profileName);
        profileEmail = (TextView) header.findViewById(R.id.profileEmail);
        profileImage = (ImageView) header.findViewById(R.id.profileImage);

        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/"+user_id);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    profileName.setText(user.getFirstName() + " " + user.getLastName());
                    profileEmail.setText(user.getEmail());
                    Glide.with(getBaseContext()).load(user.getPhoto()).into(profileImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("NOT WORKING :" + databaseError);
            }
        });

    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        onCreateDrawer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        cartItem =(MenuItem) menu.findItem(R.id.action_cart);

        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Change Actionbar colour to WHITE-ish
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F5F5F5")));

                //Edit close button and only show if there is text
                final String query = searchView.getQuery().toString();
                ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                searchClose.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                searchClose.setVisibility(query.isEmpty() || query.equals("") ? View.GONE : View.VISIBLE);

                searchClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
                        txtSearch.setText("");
                        searchView.setQuery("", false);
                        ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                        searchClose.setVisibility(View.GONE);
                        searchView.setIconified(false);

                    }
                });

                // Changing Drawer Icon
                toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
                toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY);


                // change textview colours
                EditText txtSearch = ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text));
                txtSearch.setTextColor(Color.BLACK);
                txtSearch.setHintTextColor(Color.GRAY);

                //remove cart icon
                cartItem.setVisible(false);

                // add suggestion view
                addOrUpdateSuggestionView(suggestions);

            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                closeSearchView();
                return false;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transitionToSearchView(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<String> newSuggestions = filterSuggestions(newText);
                addOrUpdateSuggestionView(newSuggestions);
                ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                searchClose.setVisibility((newText.isEmpty() || newText.equals("")) ? View.GONE : View.VISIBLE);
                return true;
            }
        });

        cartItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(MainActivity.this, CartActivity.class));
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println(item.getItemId());
        super.onOptionsItemSelected(item);
        return false;
    }
    /*
    Navigation for the shelf.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            startActivity(new Intent(this, YourOrdersActivity.class));
        } else if (id == R.id.nav_cart) {
            startActivity(new Intent(this, CartActivity.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else if (id == R.id.nav_help) {
            startActivity(new Intent(this, HelpActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return false; //returning false instead of true so that it does not highlight selection
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    private void closeSearchView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MainFragment mainFragment = new MainFragment();
        transaction.replace(R.id.fragment_container, mainFragment, "MAIN_FRAG");
        transaction.commit();


        // Change action bar colour
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        // Changing Drawer Icon
        toggle.syncState();
        toolbar.getNavigationIcon().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        cartItem.setVisible(true);
    }

    public void addOrUpdateSuggestionView(ArrayList<String> newSuggestions) {
        suggestionFragment = (SuggestionFragment) getSupportFragmentManager().findFragmentByTag("SUGGESTION_FRAG");
        if(suggestionFragment == null) {
            suggestionFragment = new SuggestionFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("SUGGESTION_ARRAY", newSuggestions);
            suggestionFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, suggestionFragment, "SUGGESTION_FRAG");
            // Commit the transaction
            transaction.commit();
        } else if (suggestionFragment != null && !suggestionFragment.isVisible()) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, suggestionFragment, "SUGGESTION_FRAG");
            // Commit the transaction
            transaction.commit();
            suggestionFragment.updateSuggestions(createSuggestions());

        } else {
            suggestionFragment.updateSuggestions(newSuggestions);
        }
    }

    public void clickOnSuggested(String query) {
        searchView.setQuery(query, true);
    }

    private void transitionToSearchView(String query) {

        searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("SEARCH_FRAG");
        if(searchFragment == null || !searchFragment.isVisible()) {

            searchFragment = new SearchFragment();
            Bundle args = new Bundle();
            args.putString("SEARCH_PARAM", query);
            searchFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, searchFragment, "SEARCH_FRAG");
            transaction.addToBackStack("search");
            // Commit the transaction
            transaction.commit();
        } else {
            searchFragment.updateSearchResults(query);
        }
    }

    private ArrayList<String> createSuggestions() {
        suggestions = new ArrayList<String>();
        suggestions.add("tequila");
        suggestions.add("rum");
        suggestions.add("vodka");
        return suggestions;
    }

    private ArrayList<String> filterSuggestions(String query) {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (int i=0; i<suggestions.size(); i++) {
            if (suggestions.get(i).toLowerCase().startsWith(query.toLowerCase()))
                arrayList.add(suggestions.get(i));
        }
        return arrayList;
    }

}
