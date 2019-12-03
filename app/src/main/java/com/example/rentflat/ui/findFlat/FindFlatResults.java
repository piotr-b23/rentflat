package com.example.rentflat.ui.findFlat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentflat.R;
import com.example.rentflat.ui.flat.Flat;

import java.util.ArrayList;

public class FindFlatResults extends AppCompatActivity {

    RecyclerView recyclerView;
    FindFlatAdapter adapter;
    ArrayList<Flat> flats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat_results);

        Intent intent = this.getIntent();
        flats = intent.getParcelableArrayListExtra("found flats");

        recyclerView = findViewById(R.id.findFlatsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FindFlatAdapter(this, flats);
        recyclerView.setAdapter(adapter);
    }
}
