package com.finals.pdfier.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.finals.pdfier.R;
import com.finals.pdfier.data.local.SqlConnector;
import com.finals.pdfier.data.models.PdfMeta;
import com.finals.pdfier.databinding.ActivityListBinding;
import com.finals.pdfier.utils.BottomNavigationUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private static final String TAG = "ListActivity";
    private static final int ACTIVITY_NUM = 2;
    private ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<PdfMeta> pdfMetas;
        try {
            pdfMetas = SqlConnector.getInstance(this).getAllPdfMeta();
            setupRV(pdfMetas);
        }catch (Exception e) {
            Snackbar.make(binding.getRoot(), e.getMessage(), Snackbar.LENGTH_LONG).show();
        }

        binding.noItemInclude.noItemMsg.setText(getString(R.string.pdfs_history_would_appear_here));
        setupBottomNavigation();
    }

    private void setupRV(List<PdfMeta> pdfMetas) {
        if (pdfMetas.size() != 0) {
            binding.noItemInclude.getRoot().setVisibility(View.GONE);

            PdfMetaRVAdapter pdfMetaRVAdapter = new PdfMetaRVAdapter(pdfMetas);
            binding.recyclerview.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            binding.recyclerview.setLayoutManager(layoutManager);
            binding.recyclerview.setAdapter(pdfMetaRVAdapter);
        }else {
            binding.noItemInclude.getRoot().setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationUtils.enableBottomNavigation(this, binding.navigation);
        Menu menu = binding.navigation.getMenu();
        MenuItem item = menu.getItem(ACTIVITY_NUM);
        item.setChecked(true);
    }
}