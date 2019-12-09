package com.example.rentflat.rate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentflat.R;

import java.util.ArrayList;

public class RateResults extends AppCompatActivity {

    RecyclerView recyclerView;
    RateAdapter adapter;
    ArrayList<Rate> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_results);

        Intent intent = this.getIntent();
        rates = new ArrayList<>();
        rates = intent.getParcelableArrayListExtra("found rates");

        recyclerView = findViewById(R.id.ratesRecycler);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RateAdapter(this, rates);
        recyclerView.setAdapter(adapter);
    }
}
