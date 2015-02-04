package com.jesseme.jessememobileandroidapplication.view.menu;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jesseme.jessememobileandroidapplication.model.ItemModel;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuItemView extends Button {

    public MenuItemView(Context context){
        super(context);

//        android.view.Display display = ((android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        //this is hacky, must be based on parent size



//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

//        this.setLayoutParams(params);
    }

    public void populate(ItemModel itemModel){
        Log.d("MenuItemView", "item populated for item: "+itemModel.getName());
        this.setText(itemModel.getName());
        try {

//            LinearLayout test = ((LinearLayout) getParent());
//            Log.d("MenuItemView", "item populated with width: " + test.getWidth());
//            Log.d("MenuItemView", "item populated with height: " + test.getHeight());
//        this.setWidth((int)(test.getWidth()*0.30));
//        this.setHeight((int) (test.getWidth() * 0.30));
        }catch(Exception e){
            Log.e("MenuItemView","ERROR: "+e.getMessage(), e.fillInStackTrace());
        }
    }

}
