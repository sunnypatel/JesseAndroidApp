package com.jesseme.jessememobileandroidapplication.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
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

public class LocationService extends IntentService {

    public final static String ACTION_LOGIN = "com.jessememobileandroidapplication.service.ACTION_LOGIN";
    private static LocationService self;

    private String token;

    public LocationService() {
        super("APIService");
        Log.d("APIService","APIService's constructor has been called");
    }

    public static LocationService getInstance(){
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

    //api calling functions below
}
