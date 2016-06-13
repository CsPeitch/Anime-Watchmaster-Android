package com.example.admin.animewatchmaster.activities.animebyletter;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.utils.EffectUtils;
import com.example.admin.animewatchmaster.utils.Utils;
import com.example.admin.animewatchmaster.activities.animeinfo.AnimeInfo;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.activities.drawer.NavDrawerItem;
import com.example.admin.animewatchmaster.activities.drawer.NavDrawerListAdapter;
import com.example.admin.animewatchmaster.model.Anime;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abraham on 10/6/2016.
 */
public class AnimesByLetter extends AppCompatActivity {

    private DBHelper dbHelper;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;
    private TypedArray navMenuIcons;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    //TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;

    private String letter = "";

    private ArrayList<String> genres = new ArrayList<>();


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_anime_by_letter);

        dbHelper = DBHelper.getInstance(getApplicationContext());

        letter = (String)getIntent().getSerializableExtra("letter");

        final List<Anime> animeList = dbHelper.getAllAnime(0,letter,null);
        //Collections.sort(animeList);

        loadGridView(animeList,-1);

        mTitle = mDrawerTitle = getTitle();


        //mDrawerLayout.openDrawer(Gravity.LEFT);
        //clean drawer and show already saved contacts from sqlite while scanning for new
        List<String> genreslist = Utils.getAllGenres();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        for (String gen : genreslist) {
            navDrawerItems.add(new NavDrawerItem(gen, navMenuIcons.getResourceId(0, -1)));
        }

        View header = getLayoutInflater().inflate(R.layout.drawer_drawerheader,null);
        View header2 = getLayoutInflater().inflate(R.layout.drawer_drawerheader_second,null);

        TextView textView = (TextView)header.findViewById(R.id.lowtext);
        textView.setText("Genres");

        mDrawerList.addHeaderView(header);
        mDrawerList.addHeaderView(header2);

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                //meta na mpei to logo tis efarmogeis
                R.drawable.com_facebook_button_icon, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        )

        {
            public void onDrawerClosed(View view) {

                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {

                    finish();

                } else if(position == 1) {

                    genres.clear();

                    for(int i =0; i < navDrawerItems.size(); i++) {
                        navDrawerItems.get(i).setIsChecked(false);
                        navDrawerItems.get(i).setChooseicon(navMenuIcons.getResourceId(0, -1));
                    }

                    adapter = new NavDrawerListAdapter(getApplicationContext(),
                            navDrawerItems);
                    mDrawerList.setAdapter(adapter);

                        List<Anime> animeList = dbHelper.getAllAnime(1,letter,genres);

                        loadGridView(animeList,-1);

                } else if(position >= 1 && position <= navDrawerItems.size()) {

                    NavDrawerItem navDrawerItem = (NavDrawerItem)parent.getItemAtPosition(position);
                    String genre = navDrawerItem.getTitle();

                    if(navDrawerItem.isChecked()) {

                        if(genres.contains(genre)) {
                            genres.remove(genre);
                        }

                        navDrawerItem.setIsChecked(false);
                        navDrawerItem.setChooseicon(navMenuIcons.getResourceId(0, -1));
                        ImageView checkIcon = (ImageView)view.findViewById(R.id.checkicon);
                        checkIcon.setImageResource(navMenuIcons.getResourceId(0,-1));

                    } else {

                        if(!genres.contains(genre)) {
                            genres.add(genre);
                        }

                        navDrawerItem.setIsChecked(true);
                        navDrawerItem.setChooseicon(navMenuIcons.getResourceId(1, -1));
                        ImageView checkIcon = (ImageView)view.findViewById(R.id.checkicon);
                        checkIcon.setImageResource(navMenuIcons.getResourceId(1, -1));

                    }

                    List<Anime> animeList = dbHelper.getAllAnime(1,letter,genres);

                    loadGridView(animeList,-1);

                }

            }
        });

    }

    private void loadGridView(List<Anime> animeList, int colsNum){

        final GridView gridView = (GridView) findViewById(R.id.gridview);

        if(colsNum == 1) {
            gridView.setNumColumns(1);
        } else if(colsNum == 2) {
            gridView.setNumColumns(2);
        } else if(colsNum == 3) {
            gridView.setNumColumns(3);
        }

        gridView.setLayoutAnimation(EffectUtils.getgridlayoutAnim());
        AnimeLetterAdapter animeLetterAdapter = new AnimeLetterAdapter(getApplicationContext(),animeList);

        gridView.setAdapter(animeLetterAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                gridView.setEnabled(false);

                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                gridView.setEnabled(true);
                            }
                        });

                    }
                }, 500);

                Anime anime = (Anime) parent.getItemAtPosition(position);
                Intent intent = new Intent(AnimesByLetter.this, AnimeInfo.class);
                intent.putExtra("anime", anime);
                startActivity(intent);

            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                try {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(imm.isActive()) {
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });


    }

    public void showGenreDrawer(View v) {

        mDrawerLayout.openDrawer(GravityCompat.START);

    }




}