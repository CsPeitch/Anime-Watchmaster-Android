package com.peitch.animewatchmaster.utils.Asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.peitch.animewatchmaster.utils.NetworkUtils;
import com.peitch.animewatchmaster.utils.databaseUtils.DBHelper;
import com.peitch.animewatchmaster.utils.databaseUtils.jsonDataImport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 4/12/2016.
 */
//json import
public class databaseUpdater extends AsyncTask<String,Void,Void> {
    public Context mainContext;

    public databaseUpdater(Context context){
        mainContext=context;
    }


    //ProgressDialog dialog;
    @Override
    protected void onPreExecute(){
        //dialog = ProgressDialog.show(mainContext,"Database Update","Updating database",true);
    }



    @Override
    protected Void doInBackground(String... databaseurl) {

        if(!NetworkUtils.isInternetConnectionActive(mainContext.getSystemService(Context.CONNECTIVITY_SERVICE))){
            Log.i("databaseUpdater"," No internet connection or cannot connect to database server");
            return null;
        }

        DBHelper dbinstance = DBHelper.getInstance(mainContext);
        JSONObject versionjob = jsonDataImport.getVData(databaseurl[0]);
        if(versionjob != null) {

            int localversion = dbinstance.getVersion();
            int onlineversion = -1;
            try {
                onlineversion = versionjob.getInt("version");
            } catch (JSONException e) {
                Log.e("dbupdater - background", "Cannot read version from json object");
                e.printStackTrace();
            }
            Log.d("dbupdater - background", "local version: " + localversion + " online version: " + onlineversion);
            JSONArray jarr = new JSONArray();

            if (onlineversion > localversion) {

                jarr = jsonDataImport.getAnimeinfoDataIncludingLinksByVersion(databaseurl[0], localversion);

                if(jarr != null && jarr.length() > 0) {

                    JSONObject job = null;
                    try {
                        int lastVersion = ((JSONObject)jarr.get(0)).getInt("version");
                        int currentVersion = 0;


                        for (int i = 0; i < jarr.length(); i++) {

                            job = (JSONObject) jarr.get(i);
                            int id;

                            currentVersion=job.getInt("version");
                            if(currentVersion>lastVersion) {
                                dbinstance.updateVersion(lastVersion);
                                lastVersion=currentVersion;
                            }


                            if (!dbinstance.checkIfExistsInAnimeInfo(job.getString("title"))) {
                                boolean s = dbinstance.insertIntoAnimeinfo(job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"),job.getString("annimgurl"));
                                id = dbinstance.getAnimeID(job.getString("title"));
                                s = dbinstance.insertIntoAnimelinks(id,job.getString("frlink"),job.getString("ultimalink"),job.getString("MALlink"));
                                //System.out.println(s);
                            } else {
                                id = dbinstance.getAnimeID(job.getString("title"));
                                boolean s = dbinstance.updateAnimeinfo(id, job.getString("title"), job.getString("imgurl"), job.getString("genre"), job.getString("episodes"), job.getString("animetype"), job.getString("agerating"), job.getString("description"),job.getString("annimgurl"));
                                if(dbinstance.checkIfExistsInAnimelinks(id)) {
                                    s = dbinstance.updateAnimelinks(id, job.getString("frlink"), job.getString("ultimalink"),job.getString("MALlink"));
                                }else{
                                    s = dbinstance.insertIntoAnimelinks(id,job.getString("frlink"),job.getString("ultimalink"),job.getString("MALlink"));
                                }
                            }
                        }

                        dbinstance.updateVersion(onlineversion);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                }



            } else {
                Log.i("dbupdater - background", "Up to date update not needed");
            }

        }

        //dialog.dismiss();
        return null;

    }


}
