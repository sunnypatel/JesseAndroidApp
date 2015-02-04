package com.jesseme.jessememobileandroidapplication.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuView extends LinearLayout {

    public MenuView(Context context){
        super(context);
    }

    public MenuView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public void forceCloseItemDisplayExcept(MenuRowView exception) {
        int numChildren = getChildCount();
        for(int i = 0; i < numChildren; i++){
            MenuRowView view = (MenuRowView) getChildAt(i);
            if(view==exception) {
                Log.d("MenuView","exception skipped");
                continue;
            }
            Log.d("MenuView","item not skipped");
            view.hideExpandableDescriptionView();
        }
    }
}
