package com.jesseme.jessememobileandroidapplication.controller.menu;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewSwitcher;

import com.jesseme.jessememobileandroidapplication.R;
import com.jesseme.jessememobileandroidapplication.model.ItemModel;
import com.jesseme.jessememobileandroidapplication.model.RestaurantModel;
import com.jesseme.jessememobileandroidapplication.services.APIService;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuRowView;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuFragment extends Fragment {

//    private LayoutInflater inflater;
//    private ViewGroup container;
    private MenuView menu;

    public MenuFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        this.inflater = inflater;
//        this.container = container;
        // Inflate the layout for this fragment
        return (LinearLayout) inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void onResume(){
        super.onResume();
        menu = (MenuView) getActivity().findViewById(R.id.menu_container);
    }

    public void populate(String restaurantID){
        Log.d("MenuFragment","populate called with ID: "+restaurantID);
        new GetRestaurantTask(restaurantID).execute((Void) null);
    }

    public void populateContainer(RestaurantModel restaurantModel){
        JSONArray items = restaurantModel.getItems();
        Log.d("MenuFragment", "populateContainer called with: "+restaurantModel.toString());
        Context context = getActivity();
        int temp_length = items.length();
        for(int i = 0; i < temp_length; i+=3) {
            MenuRowController row = new MenuRowController(getActivity());
            for (int j = 0; j < 3; j++) {
                try{
                    row.addItem(context, new ItemModel(restaurantModel.getItem(i + j)), this.menu);
                }catch(Exception e){
                    //do nothing. this is expected there is an out of bounds
                }
            }
            row.formatViews();
            this.menu.addView(row.getView());
        }
        menu.postInvalidate();
    }


    /**
     * Represents an asynchronous getRestaurantByID task used to populate the menu
     * the user.
     */
    public class GetRestaurantTask extends AsyncTask<Void, Void, Boolean> {

        private final String mId;

        GetRestaurantTask(String id) {
            mId = id;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        JSONObject json = new JSONObject(intent.getStringExtra("restaurant"));
                        RestaurantModel restaurantModel = new RestaurantModel(json);

                        populateContainer(restaurantModel);

                    }catch(Exception e){
                        Log.e("MenuActivity", "error: "+e.getMessage());
                    }
                }
            };
            IntentFilter filter = new IntentFilter(APIService.ACTION_RESTAURANT_BY_ID);
            getActivity().registerReceiver(receiver, filter);

            APIService.getInstance().getRestaurantById(mId);

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
