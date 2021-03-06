package com.peitch.animewatchmaster.activities.watchlist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.dd.CircularProgressButton;
import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.model.WatchListModel;
import com.peitch.animewatchmaster.utils.Asynctasks.WatchlistUpdater;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.facebook.appevents.AppEventsLogger;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by abraham on 10/6/2016.
 */
public class WatchList extends AppCompatActivity {

    private ListView listView;
    private static Thread updateThread;
    private CircularProgressButton circularProgressButton;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.layout_watchlist);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.ButtonUpdate);

        List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();
        listView = (ListView)findViewById(R.id.watchlist);

        WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(),modelList);
        listView.setAdapter(watchListAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
        if(circularProgressButton != null) {
            circularProgressButton.setProgress(0);
        }
    }

    public void goback(View v) {
        finish();
    }


    public void updateWatchlist(View v){

        if(updateThread == null || !updateThread.isAlive()) {

            circularProgressButton.setProgress(0);
            circularProgressButton.setIndeterminateProgressMode(true);
            circularProgressButton.setProgress(50);


            updateThread = new Thread(new Runnable() {
                @Override
                public void run() {

                    Log.d("updateWatchlist", "Starting watchlist update");
                    try {
                        new WatchlistUpdater(getApplicationContext()).execute("").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            List<WatchListModel> modelList = DBHelper.getInstance(getApplicationContext()).getWatchlistData();

                            WatchListAdapter watchListAdapter = new WatchListAdapter(getApplicationContext(), modelList);
                            listView.setAdapter(watchListAdapter);

                            circularProgressButton.setProgress(100);
                        }
                    });
                }
            });
            updateThread.start();

        }
    }


}
