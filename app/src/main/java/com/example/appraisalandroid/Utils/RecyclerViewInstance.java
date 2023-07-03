package com.example.appraisalandroid.Utils;

import android.content.Context;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewInstance {

    public static RecyclerView getRecyclerViewVertical(RecyclerView recyclerView, Context context ){
        if (recyclerView != null){
            RecyclerView.LayoutManager layoutManagerVertical = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManagerVertical);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setNestedScrollingEnabled(false);
        }
        return recyclerView;
    }

    public static RecyclerView getRecyclerViewHorizontal(RecyclerView recyclerView, Context context){
        if (recyclerView != null){
            RecyclerView.LayoutManager layoutManagerVertical = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManagerVertical);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
//            recyclerView.setNestedScrollingEnabled(false);
        }
        return recyclerView;
    }

}
