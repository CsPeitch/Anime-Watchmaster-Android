package com.example.admin.animewatchmaster.databaseUtils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 11/6/2015.
 */
public class jsonDataImport {


    private static final String CLASS_TAG = "jsonDataImport - ";

    public static JSONArray getAllanimeData(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawallanime.php",version);
    }

    public static JSONArray getAnimeinfoData(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfo.php",version);
    }

    public static JSONArray getAnimeultimaData(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/animeultima.php",version);
    }

    public static JSONArray getAnimeinfoDataIncludingLinksByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfoandlinksbyversion.php",version);
    }

    public static JSONArray getAllanimeDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawallanimebyversion.php",version);
    }

    public static JSONArray getAnimeinfoDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfobyversion.php",version);
    }

    public static JSONArray getAnimeultimaDataByVersion(String base_db_url, int version){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimultimabyversion.php",version);
    }

    private static JSONArray getData(String db_url, int version){
        final String TAG = CLASS_TAG+"getData";
        JSONArray jarr=null;
        //i palia methodos sto api 23 exei katargithei alla den epsaksa na vrw pws na to kanw
        try {
            JSONObject obj = new JSONObject();
            obj.put("usr","gd4#DpxKli");
            obj.put("pss","pw2hT#S%g#");
            obj.put("vrs",version);

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("sinfo",obj.toString())
                    .build();

            Request request = new Request.Builder()
                    .url(db_url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            jarr = new JSONArray(result);

            /*
            URL url = new URL(db_url);
            URI uri = url.toURI();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            */

        }  catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
        StringBuilder sb = new StringBuilder();
        String line =null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            while((line=br.readLine())!=null){
                sb.append(line);
            }
            br.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException on read");
        }
        */

        return jarr;
    }

    public static JSONObject getVData(String db_url){
        final String TAG = CLASS_TAG+"getVData";
        JSONObject job=null;
        db_url+="/animedraw/drawclasses/drawversion.php";
        try {

            JSONObject obj = new JSONObject();
            obj.put("usr","gd4#DpxKli");
            obj.put("pss","pw2hT#S%g#");

            OkHttpClient client = new OkHttpClient();

            RequestBody formBody = new FormBody.Builder()
                    .add("sinfo",obj.toString())
                    .build();

            Request request = new Request.Builder()
                    .url(db_url)
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            String result = response.body().string();

            job = new JSONObject(result);


        }  catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return job;
    }
}
