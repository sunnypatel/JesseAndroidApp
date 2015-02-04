package com.jesseme.jessememobileandroidapplication.services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.util.Log;

import com.jesseme.jessememobileandroidapplication.model.ItemModel;

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
import java.util.ArrayList;
import java.util.Scanner;

public class OrderingService extends IntentService {

    public final static String ACTION_ORDER_ADDED = "com.jessememobileandroidapplication.service.ACTION_ORDER_ADDED";
    private static OrderingService self;
    //this contains the item ids of the current order
    private ArrayList<String> items = new ArrayList<>();
    private BroadcastReceiver receiver;

    public OrderingService() {
        super("OrderingService");
        Log.d("OrderingService","OrderingService's constructor has been called");
    }

    public static OrderingService getInstance(){
        if(self == null){
            Log.d("OrderingService", "calling getInstance before instance is created. returning null");
            return null;
        }
        return self;
    }

    private void registerReceivers(){
        //TODO are there any more?
        Log.d("OrderingService","receivers registered");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String item = intent.getStringExtra("ItemID");
                items.add(item);
                for(String i : items){
                    Log.d("OrderingService","Order: "+i);
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ORDER_ADDED);
        registerReceiver(receiver, filter);
    }

    public class OrderingBinder extends Binder {

        public OrderingService getService(){
            return OrderingService.this;
        }
    }

//    public void addOrder(ItemModel itemModel){d
//
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //prep for singleton
        if(self == null) {
            registerReceivers();
            self = this;
        }

        Log.d("OrderingService","OrderingService's onHandleIntent called");
        String action = intent.getAction();
        if(action == null) {
            Log.d("OrderingService", "intent action is null, skipping.");
            return;
        }else if(action.equals(OrderingService.ACTION_ORDER_ADDED)){
            items.add(intent.getStringExtra("itemID"));
            Log.d("OrderingService","current orders:");
            for(String item : items){
                Log.d("OrderingService",item);
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
    }
}
