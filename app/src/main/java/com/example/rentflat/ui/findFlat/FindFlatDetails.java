package com.example.rentflat.ui.findFlat;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentflat.ui.flat.Flat;
import com.example.rentflat.ui.imageDisplay.ImageAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import com.example.rentflat.R;

import java.util.ArrayList;

public class FindFlatDetails extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final Flat selectedFlat = (Flat) intent.getParcelableExtra("selected flat");
        ArrayList<String> photos = selectedFlat.generatePhotosToDisplay();

        recyclerView = findViewById(R.id.findFlatImagesRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ImageAdapter(this, photos);
        recyclerView.setAdapter(adapter);


    }
}
