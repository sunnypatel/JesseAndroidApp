package com.jesseme.jessememobileandroidapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

import com.jesseme.jessememobileandroidapplication.controller.menu.MenuFragment;
import com.jesseme.jessememobileandroidapplication.model.RestaurantModel;
import com.jesseme.jessememobileandroidapplication.services.APIService;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class MenuActivity extends Activity {

    private GetRestaurantNearbyTask mRestaurantTask;
    private MenuFragment menuFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        fragmentManager = getFragmentManager();
        menuFragment = (MenuFragment) fragmentManager.findFragmentById(R.id.menu_fragment);
    }

    protected void onResume(){
        super.onResume();
        Log.d("MenuActivity", "resumed");
        //this task gets the restaurant in the area
        //then it calls another task to pull that restaurants information
        mRestaurantTask = new GetRestaurantNearbyTask("1234", "5678");
        mRestaurantTask.execute((Void) null);
    }


    /**
     * Represents an asynchronous get restaurants near me task used to populate menu
     */
    public class GetRestaurantNearbyTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLongitude;
        private final String mLatitude;

        GetRestaurantNearbyTask(String longitude, String latitude) {
            mLongitude = longitude;
            mLatitude = latitude;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        JSONArray json = new JSONArray(intent.getStringExtra("restaurants"));
                        JSONObject restaurant = (JSONObject)(json.get(0));

                        menuFragment.populate(restaurant.getString("id"));

                    }catch(Exception e){
                        Log.e("MenuActivity",e.getLocalizedMessage());
                    }
                }
            };
            IntentFilter filter = new IntentFilter(APIService.ACTION_RESTAURANT_BY_LOCATION);
            registerReceiver(receiver, filter);

            BroadcastReceiver receiver2 = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        RestaurantModel restaurant = new RestaurantModel(intent.getStringExtra("restaurant"));
                    }catch(Exception e){}
                }
            };
            IntentFilter filter2 = new IntentFilter(APIService.ACTION_RESTAURANT_BY_ID);
            registerReceiver(receiver2, filter2);


            //TODO: replace this line with the one below it
            APIService.getInstance().getRestaurantByLocation(mLongitude, mLatitude);



            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }
}



