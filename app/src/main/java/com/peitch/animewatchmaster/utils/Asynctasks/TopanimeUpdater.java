package com.peitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.peitch.animewatchmaster.R;
import com.peitch.animewatchmaster.utils.NetworkUtils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.peitch.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by admin on 7/1/2016.
 */
public class TopanimeUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public TopanimeUpdater(Context context){
        mainContext=context;
    }

    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }

    protected Void doInBackground(String... databaseurl) {
        if (!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))) {
            Log.i("TopanimeUpdater -", " No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONObject versionjob = jsonDataImport.getTOPVData(databaseurl[0]);

        if(versionjob != null) {
            int localversion = dbinstance.getTOPVersion();
            int onlineversion = -1;
            try {
                onlineversion = versionjob.getInt("TOPversion");
            } catch (JSONException e) {
                Log.e("APdbupdater - bkground", "Cannot read version from json object");
                e.printStackTrace();
            }
            Log.d("TOPupdater - bkground", "local version: " + localversion + " online version: " + onlineversion);
            JSONArray jarr = new JSONArray();

            if (onlineversion > localversion) {
                jarr = jsonDataImport.getMALtopanimeData(mainContext.getString(R.string.base_db_url));
                if (jarr.length() == 0) {
                    Log.i("TopanimeUpdater", "Empty array");
                    return null;
                }

                int spot = 1;
                final int topAnimeNumber = dbinstance.getNumberOfAnime(6);
                for (int i = 0; i < jarr.length(); i++) {
                    try {
                        JSONObject job = jarr.getJSONObject(i);
                        int id = dbinstance.getAnimeID(job.getString("title"));
                        if (id != -1) {
                            if (spot <= topAnimeNumber) {
                                dbinstance.updateMALtopanime(spot, id, job.getDouble("score"));
                            } else {
                                dbinstance.insertIntoMALtopanime(id, spot, job.getDouble("score"));
                            }
                            spot++;
                        } else {
                            Log.i("TopanimeUpdater", "Cannot find id for anime with title: " + job.getString("title"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("TopanimeUpdater", "Json exception in loop");
                    }

                }
                dbinstance.deleteMALtopanimeAfterSpot(--spot);

                dbinstance.updateTOPVersion(onlineversion);
            }else {
                Log.i("TOPupdater - backgrnd", "Up to date update not needed");
            }
        }

        //testing on create
        /*
        ArrayList<TopanimeModel> anime = (ArrayList<TopanimeModel>) dbinstance.getTopAnimeData();
        for(TopanimeModel anim : anime){
            Log.d("in loop",anim.getTitle());
        }*/

        return null;
    }
}
