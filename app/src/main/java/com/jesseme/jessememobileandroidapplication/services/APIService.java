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
    private static APIService self;

    private String token;

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
        BroadcastReceiver loginReceiver = new BroadcastReceiver() {
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
        self = this;

        Log.d("APIService","APIService's onHandleIntent called");
        if(intent.getAction() == null) {
            Log.d("APIService", "intent action is null, skipping.");
            return;
        }
        registerReceivers();
        Log.d("APIService","action: "+intent.getAction());
        Log.d("APIService","data: "+intent.getDataString());
        Log.d("APIService","type: "+intent.getType());
        Log.d("APIService","tostring: "+intent.toString());
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

            if(response!=null){
                final InputStream in = response.getEntity().getContent(); //Get the data in the entity
                new Thread(new Runnable(){
                    public void run(){
                        Scanner sc = new Scanner(in);
                        String resp = "";
                        while(sc.hasNext())
                            resp+=sc.nextLine();

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
                            resp+=sc.nextLine();
                        Log.d("APIService","resp from get restaurant: "+resp);
                        try {
                            JSONObject respObject = new JSONObject(resp);
//                            JSONObject obj = (JSONObject) respObject.get("restaurants");
                            Intent i = new Intent(ACTION_RESTAURANT_BY_LOCATION);
                            i.putExtra("success",true);
                            i.putExtra("token",token);
                            sendBroadcast(i);
                        }catch(Exception e){
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

        }
    }
}
