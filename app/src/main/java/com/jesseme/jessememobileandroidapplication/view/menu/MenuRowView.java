package com.jesseme.jessememobileandroidapplication.view.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jesseme.jessememobileandroidapplication.model.ItemModel;

/**
 * Created by Daniel on 1/25/2015.
 */
public class MenuRowView extends LinearLayout {

    private LinearLayout itemListArea;
    private LinearLayout descriptionView;
    private LayoutParams item_params;
    private Context context;
    //this is just to keep track of how many invisible fields to add
    private int numItems = 0;
    private final static int itemsPerRow = 3;
    //this holds the particular item # that is being displayed. -1 means the view is closed
    private int expandedView = -1;


    public MenuRowView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context);
    }

    public MenuRowView(Context context){
        super(context);
        init(context);
    }

    private void init(Context context){
        this.context = context;
        //master orientation containing the item list view, and the expandable description view
        this.setOrientation(LinearLayout.VERTICAL);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);

        //setup item view layout params
        item_params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        item_params.weight = .3f;

//        all item views are add to this linear layout
        itemListArea = createItemListAreaView(context);
        descriptionView = createExpandableDescriptionView(context);

        this.addView(itemListArea);
        this.addView(descriptionView);
    }

    /**
        this is a factory method for creating the itemListArea LinearLayout containing ItemView's
        returns customized LinearLayout
     */
    private LinearLayout createItemListAreaView(Context context){
        LinearLayout listArea = new LinearLayout(context);
        listArea.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        listArea.setLayoutParams(params);
        return listArea;
    }

    /**
     this is a factory method for creating the expandable description LinearLayout containing ItemView's name, price, etc.
     returns customized LinearLayout
     */
    private LinearLayout createExpandableDescriptionView(Context context){
        LinearLayout descriptionView = new LinearLayout(context);
        descriptionView.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        descriptionView.setLayoutParams(params);
        descriptionView.setVisibility(LinearLayout.GONE);

        return descriptionView;
    }

    public void addViewToItems(View view){
        itemListArea.addView(view, item_params);
        numItems++;
    }

    public void formatViews(){
        for(int i = 0; i < itemsPerRow - numItems; i++){
            Button blank = new Button(context);
            blank.setVisibility(Button.INVISIBLE);
            blank.setFocusable(false);
            blank.setClickable(false);
            itemListArea.addView(blank, item_params);
        }
    }

    public boolean isExpanded(){
        boolean response = descriptionView.getVisibility() == LinearLayout.VISIBLE;
        Log.d("MenuRowView","is already expanded: "+response);
        return response;
    }

    public void displayExpandableDescriptionView(int col, View containedView){
        try {
            expandedView = col;
            descriptionView.addView(containedView);
            descriptionView.setVisibility(LinearLayout.VISIBLE);
            descriptionView.invalidate();
            Log.d("MenuRowView","Expanding view now");
        }catch(Exception e){
            Log.e("MenuRowView",e.getMessage(),e.fillInStackTrace());
        }
    }

    public void hideExpandableDescriptionView(){
        expandedView = -1;
        descriptionView.setVisibility(LinearLayout.GONE);
        descriptionView.removeAllViews();
        descriptionView.invalidate();
    }

    public void toggleExpandableDescriptionView(int col, View containedView){
        //this is used so that later we can add animating opening and closings
        //if view is already expanded then close it
        if(expandedView == col){
            hideExpandableDescriptionView();
        }
        //if no view is opened then open it
        else if(expandedView == -1){
            displayExpandableDescriptionView(col, containedView);
        }
        //if a different view is opened, close it, then open the new one
        else{
            hideExpandableDescriptionView();
            displayExpandableDescriptionView(col, containedView);
        }
    }
}
