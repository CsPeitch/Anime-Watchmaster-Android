package com.example.admin.animewatchmaster.activities.animeinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.example.admin.animewatchmaster.R;
import com.example.admin.animewatchmaster.utils.databaseUtils.DBHelper;
import com.example.admin.animewatchmaster.model.Anime;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by abraham on 9/6/2016.
 */
public class AnimeInfo extends AppCompatActivity {

    private Anime anime;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_animeinfo);

        anime = (Anime) getIntent().getSerializableExtra("anime");

        if(anime != null) {

            TextView title = (TextView)findViewById(R.id.title);
            title.setText(anime.getTitle());

            ImageView imageView = (ImageView)findViewById(R.id.image);

            try {

                Picasso.with(getApplicationContext())
                        .load(anime.getImgurl())
                        .into(imageView);

            } catch (Exception ex) {

            }

            TextView desc = (TextView)findViewById(R.id.desc);
            desc.setText(anime.getDescription());


        } else {
            finish();
        }

    }



    public void addToWatchlist(final View v) {

        v.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });

            }
        },500);

        DBHelper dbinstance = DBHelper.getInstance(getApplicationContext());

        if(!dbinstance.checkIfExistsInWatchlist(anime.getId())) {

            String episodes = anime.getEpisodes();
            int ep = 0;
            boolean doUpdateFlag = false;

            if(episodes!=null && episodes.trim().equals("Ongoing")){
                doUpdateFlag = true;
            }else if(episodes.trim().isEmpty()){
                //do nothing
            }else{
                double d = Double.valueOf(anime.getEpisodes().trim());
                ep = (int) d;
            }

            dbinstance.insertIntoWatchlist(anime.getId(), 0, ep, "");
            dbinstance.deleteWatchlaterlistAnime(anime.getId());

            if(doUpdateFlag)
                new WatchlistUpdater(getApplicationContext()).execute("");

        } else {
            Toast.makeText(getApplicationContext(),"anime already in watchlist",Toast.LENGTH_SHORT).show();
        }

    }


    public void addToWatchlaterlist(final View v) {

        v.setEnabled(false);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });

            }
        },500);

        DBHelper dbinstance = DBHelper.getInstance(getApplicationContext());

        if(!dbinstance.checkIfExistsInWatchLaterList(anime.getId())) {

            dbinstance.insertIntoWatchlaterlist(anime.getId());

        } else {
            Toast.makeText(getApplicationContext(),"anime already in watch later list",Toast.LENGTH_SHORT).show();
        }


    }





}