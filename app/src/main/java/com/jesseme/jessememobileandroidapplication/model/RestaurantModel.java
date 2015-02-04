package com.jesseme.jessememobileandroidapplication.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Daniel on 1/24/2015.
 */
public class RestaurantModel {

    private JSONObject restaurant;
    private String name;
    private JSONArray items;

    public RestaurantModel(JSONObject restaurant){
        init(restaurant);
    }

    public RestaurantModel(String restaurant){
        try {
            init(new JSONObject(restaurant));
        }catch(Exception e){
            Log.e("RestaurantModel","improper json: "+e.getLocalizedMessage());
        }
    }

    private void init(JSONObject restaurant){
        try {
            this.restaurant = restaurant;
            this.name = restaurant.getString("name");
            this.items = restaurant.getJSONArray("items");
        }catch(Exception e){
            Log.e("RestaurantModel",e.getLocalizedMessage());
        }
    }

    public JSONObject getRestaurant(){
        return restaurant;
    }

    public String getName(){
        return name;
    }

    public JSONArray getItems(){
        try{
            return items;
        }catch(Exception e){
            Log.e("RestaurantModel",e.getLocalizedMessage());
            return null;
        }
    }

    public JSONObject getItem(int i){
        try {
            return items.getJSONObject(i);
        }catch(Exception e){
            Log.e("RestaurantModel",e.getLocalizedMessage()+" if this is out of bounds, then it could have been expected");
            return null;
        }
    }

    public String toString(){
        return "";
//        return restaurant.toString();
    }

}
