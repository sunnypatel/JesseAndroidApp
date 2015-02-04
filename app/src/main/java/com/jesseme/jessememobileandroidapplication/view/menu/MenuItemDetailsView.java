package com.jesseme.jessememobileandroidapplication.view.menu;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jesseme.jessememobileandroidapplication.model.ItemModel;
import com.jesseme.jessememobileandroidapplication.services.OrderingService;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuItemDetailsView extends Button {

    public MenuItemDetailsView(Context context){
        super(context);


//        android.view.Display display = ((android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //this is hacky, must be based on parent size



//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

//        this.setLayoutParams(params);
    }

    public void populate(final Context context, final ItemModel itemModel){
        Log.d("MenuItemView", "item populated for item: "+itemModel.getName());
        this.setText("Order "+itemModel.getName());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(params);
        //TODO: remove the background color set. this is just to differentiate
        this.setBackgroundColor(Color.BLUE);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MenuItemDetailsView", "adding order: "+itemModel.getID());
                Intent addOrderIntent = new Intent(OrderingService.ACTION_ORDER_ADDED);
                addOrderIntent.putExtra("itemID",itemModel.getID());
                context.sendBroadcast(addOrderIntent);
            }
        });
    }

}
