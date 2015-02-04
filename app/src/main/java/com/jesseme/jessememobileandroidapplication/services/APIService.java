package com.jesseme.jessememobileandroidapplication.services;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Scanner;

public class APIService extends IntentService {

    public final static String ACTION_LOGIN = "com.jessememobileandroidapplication.service.ACTION_LOGIN";
    public final static String ACTION_RESTAURANT_BY_LOCATION = "com.jessememobileandroidapplication.service.ACTION_RESTAURANT_BY_LOCATION";
    public final static String ACTION_RESTAURANT_BY_ID = "com.jessememobileandroidapplication.service.ACTION_RESTAURANT_BY_ID";
    private static APIService self;

    private String token;
    private BroadcastReceiver loginReceiver;

    public APIService() {
        super("APIService");
        Log.d("APIService","APIService's constructor has been called");
    }

    public static APIService getInstance(){
        if(self == null){
            Log.d("APIService", "calling getInstance before instance is created. returning null");
            return null;
        }
        return self;
    }

    public void registerReceivers(){
        //register broadcast receiver to receive login requests
        loginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("APIService", "Login broadcast received!!!!");
            }
        };
        IntentFilter loginFilter = new IntentFilter();
        loginFilter.addAction(ACTION_LOGIN);
        registerReceiver(loginReceiver, loginFilter);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //prep for singleton
        if(self == null){
            registerReceivers();
            self = this;
        }

        Log.d("APIService","APIService's onHandleIntent called");
        if(intent.getAction() == null) {
            Log.d("APIService", "intent action is null, skipping.");
            return;
        }
        Log.d("APIService","action: "+intent.getAction());
        Log.d("APIService","data: "+intent.getDataString());
        Log.d("APIService","type: "+intent.getType());
        Log.d("APIService","tostring: "+intent.toString());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loginReceiver);
    }

    public class APIBinder extends Binder {

        public APIService getService(){
            return APIService.this;
        }
    }

    //api calling functions below
    public void sendLogin(String phone, String pass){
        try {
            JSONObject json = new JSONObject();
            json.put("phone",phone);
            json.put("password",pass);

            StringEntity postMessage = new StringEntity( json.toString());

            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost request = new HttpPost("http://178.18.16.226:2730/user/login");
            request.setEntity(new ByteArrayEntity(
                    json.toString().getBytes("UTF8")));

            HttpResponse response = client.execute(request);

            JSONObject logJson = new JSONObject();
            logJson.put("level","6");
            logJson.put("short_message","logging user in: "+phone);
            logJson.put("host","JessemeAndroid");
            logJson.put("version","1");

            HttpClient client2 = new DefaultHttpClient(httpParams);
            HttpPost request2 = new HttpPost("http://graylog2.jesseme.com:9002/gelf");
            request2.setEntity(new ByteArrayEntity(
                    logJson.toString().getBytes("UTF8")));

            HttpResponse response2 = client2.execute(request);
            Log.d("APIService","logged: "+logJson.toString());

            if(response!=null){
                final InputStream in = response.getEntity().getContent(); //Get the data in the entity
                new Thread(new Runnable(){
                    public void run(){
                        Scanner sc = new Scanner(in);
                        String resp = "";
                        while(sc.hasNext())
                            resp+=sc.nextLine()+"\n";

                        try {
                            JSONObject respObject = new JSONObject(resp);
                            token = (String) respObject.get("apiToken");
                            Intent i = new Intent(ACTION_LOGIN);
                            i.putExtra("success",true);
                            i.putExtra("token",token);
                            sendBroadcast(i);
                        }catch(Exception e){
                            Intent i = new Intent(ACTION_LOGIN);
                            i.putExtra("success",false);
                            sendBroadcast(i);
                        }
                    }
                }).start();
            }

        } catch (Exception e) {
            Log.d("LoginActivity", e.getLocalizedMessage());
            Intent i = new Intent(ACTION_LOGIN);
            i.putExtra("success",false);
            i.putExtra("description","Connection timed out");
            sendBroadcast(i);
        }
    }

    public void getRestaurantByLocation(String longitude, String latitude){
        try {
            JSONObject json = new JSONObject();
            json.put("latitude", latitude);
            json.put("longitude", longitude);

            StringEntity postMessage = new StringEntity(json.toString());
            Log.d("APIService", "post message: " + json.toString());

            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpPost request = new HttpPost("http://178.18.16.226:2730/location/near");
            request.setEntity(new ByteArrayEntity(
                    json.toString().getBytes("UTF8")));

            HttpResponse response = client.execute(request);

            if(response!=null){
                final InputStream in = response.getEntity().getContent(); //Get the data in the entity
                new Thread(new Runnable(){
                    public void run(){
                        Scanner sc = new Scanner(in);
                        String resp = "";
                        while(sc.hasNext())
                            resp+=sc.nextLine()+"\n";
                        Log.d("APIService","resp from get restaurant: "+resp);
                        try {
                            Intent i = new Intent(ACTION_RESTAURANT_BY_LOCATION);
                            i.putExtra("success",true);
                            i.putExtra("restaurants",resp);
                            sendBroadcast(i);
                        }catch(Exception e){
                            Log.e("APIService",e.getLocalizedMessage());
                            Intent i = new Intent(ACTION_RESTAURANT_BY_LOCATION);
                            i.putExtra("success",false);
                            sendBroadcast(i);
                        }
                    }
                }).start();
            }else{
                Log.d("APIService","response from get restaurants was null");
            }

        }catch(Exception e){
            Log.e("APIService",e.getLocalizedMessage());
        }
    }

    public void getRestaurantById(String id){
        try {
//            JSONObject json = new JSONObject();
//            json.put("restaurant", id);

//            StringEntity postMessage = new StringEntity(json.toString());
//            Log.d("APIService", "post message: " + json.toString());

            Log.d("APIService","getting restaurant: "+id);

            int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
            HttpClient client = new DefaultHttpClient(httpParams);

            HttpGet request = new HttpGet("http://178.18.16.226:2730/restaurant/"+id);
//            request.setEntity(new ByteArrayEntity(
//                    json.toString().getBytes("UTF8")));

            HttpResponse response = client.execute(request);

            if(response!=null){
                final InputStream in = response.getEntity().getContent(); //Get the data in the entity
                new Thread(new Runnable(){
                    public void run(){
                        Scanner sc = new Scanner(in);
                        String resp = "";
                        while(sc.hasNext())
                            resp+=sc.nextLine()+"\n";
                        Log.d("APIService","resp from get restaurant: "+resp);
                        try {
                            Intent i = new Intent(ACTION_RESTAURANT_BY_ID);
                            i.putExtra("success",true);
                            i.putExtra("restaurant",resp);
                            sendBroadcast(i);
                        }catch(Exception e){
                            Log.e("APIService",e.getLocalizedMessage());
                            Intent i = new Intent(ACTION_RESTAURANT_BY_ID);
                            i.putExtra("success",false);
                            sendBroadcast(i);
                        }
                    }
                }).start();
            }else{
                Log.d("APIService","response from get restaurants was null");
            }

        }catch(Exception e){
            Log.e("APIService", e.getLocalizedMessage());
        }
    }
}
