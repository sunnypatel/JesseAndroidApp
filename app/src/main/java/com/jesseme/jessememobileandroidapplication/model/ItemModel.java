package com.jesseme.jessememobileandroidapplication.model;

import android.content.ClipData;
import android.util.Log;

import com.google.android.gms.fitness.request.e;

import org.json.JSONObject;

/**
 * Created by Daniel on 1/24/2015.
 */
public class ItemModel {

    private String name;
    private String id;

    public ItemModel(JSONObject item){
        init(item);
    }

    public ItemModel(String item){
        try {
            init(new JSONObject(item));
        }catch(Exception e){
            Log.e("ItemModel", e.getLocalizedMessage());
        }
    }

    private void init(JSONObject item){
        try {
            name = item.getString("name");
            id = item.getString("id");
        }catch(Exception e) {
            Log.e("ItemModel", e.getLocalizedMessage());
        }
    }

    public String getName(){
        return name;
    }

    public String getID(){
        return id;
    }

}
