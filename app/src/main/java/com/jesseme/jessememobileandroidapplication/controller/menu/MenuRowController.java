package com.jesseme.jessememobileandroidapplication.controller.menu;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jesseme.jessememobileandroidapplication.R;
import com.jesseme.jessememobileandroidapplication.model.ItemModel;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuRowView;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuView;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuRowController{

    private MenuRowView row;
    private Context context;
    private int numItems = 0;

    public MenuRowController(Context context){
        row = new MenuRowView(context);
        this.context = context;
    }

    public LinearLayout getView(){
        //test
        Log.d("MenuRowController", "calling get view for row");
//        return new Button(context);
        return row;
    }

    public void addItem(Context context, ItemModel itemModel, MenuView menuView){
        MenuItemController item = new MenuItemController(context);
        numItems++;
        item.populate(context, numItems, itemModel, row, menuView);
        row.addViewToItems(item.getView());
    }

    public void formatViews(){
        row.formatViews();
    }


}
