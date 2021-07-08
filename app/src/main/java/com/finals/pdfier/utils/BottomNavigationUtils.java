package com.finals.pdfier.utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.finals.pdfier.R;
import com.finals.pdfier.ui.HomeActivity;
import com.finals.pdfier.ui.ListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

//
// Created by  on 7/6/2021.
//
public class BottomNavigationUtils {

    public static void enableBottomNavigation(Context context,
                                         BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.home) {
                Intent intent = new Intent(context, HomeActivity.class);
                context.startActivity(intent);
                return true;
            }
            else if (id == R.id.list) {
                Intent intent = new Intent(context, ListActivity.class);
                context.startActivity(intent);
                return true;
            }
            return false;
        });
    }
}
