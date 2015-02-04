package com.jesseme.jessememobileandroidapplication.controller.menu;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jesseme.jessememobileandroidapplication.model.ItemModel;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuItemDetailsView;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuItemView;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuRowView;
import com.jesseme.jessememobileandroidapplication.view.menu.MenuView;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuItemController {

    private MenuItemView item;
    private MenuRowView parentRow;
    private MenuItemDetailsView expandedViewInsert;
    private int colNum;

    public MenuItemController(final Context context){
        item = new MenuItemView(context);
    }

    public Button getView(){
        return item;
    }

    /**
     * this function must be called when instantiating a menu item. this would be called in populate,
     * but the constructor can't handle the necessary parameters
     */
    public void populate(Context context, final int colNum, ItemModel itemModel, final MenuRowView parentRow, final MenuView menuView){
        this.colNum = colNum;
        this.parentRow = parentRow;
        item.populate(itemModel);
        expandedViewInsert = new MenuItemDetailsView(context);
        expandedViewInsert.populate(context, itemModel);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuView.forceCloseItemDisplayExcept(parentRow);
                parentRow.toggleExpandableDescriptionView(colNum, expandedViewInsert);
            }
        });
    }


}
