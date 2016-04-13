package com.example.admin.animewatchmaster.databaseUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by admin on 11/6/2015.
 */
public class jsonDataImport {
    private static InputStream is;
    public static JSONArray getAllanimeData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/drawallanime.php");
    }
    public static JSONArray getAnimeinfoData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/drawanimeinfo.php");
    }
    public static JSONArray getAnimeultimaData(String base_db_url){
        return getData(base_db_url+"/animedraw/drawclasses/animeultima.php");
    }

    private static JSONArray getData(String db_url){
        JSONArray jarr=null;
        //i palia methodos sto api 23 exei katargithei alla den epsaksa na vrw pws na to kanw
        try {
            URL url = new URL(db_url);
            URI uri = url.toURI();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
            System.out.println("BufferedReader   "+e.toString());
        }

        try {
            jarr = new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Jsonparser    "+e.toString());
        }


        return jarr;
    }

    public static JSONObject getVData(String db_url){
        JSONObject jarr=null;
        db_url+="/animedraw/drawclasses/drawversion.php";
        try {
            URL url = new URL(db_url);
            URI uri = url.toURI();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(uri);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
            System.out.println("BufferedReader   "+e.toString());
        }

        try {
            jarr = new JSONObject(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Jsonparser    "+e.toString());
        }


        return jarr;
    }
}