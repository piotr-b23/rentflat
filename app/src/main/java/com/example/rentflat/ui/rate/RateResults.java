package com.example.rentflat.ui.rate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;

import com.example.rentflat.R;

import java.util.ArrayList;
import java.util.Iterator;

public class RateResults extends AppCompatActivity {

    RecyclerView recyclerView;
    RateAdapter adapter;
    ArrayList<Rate> rates;

    private RatingBar contactRateAVG, descriptionRateAVG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_results);

        contactRateAVG = findViewById(R.id.ratingBarContactAVG);
        descriptionRateAVG = findViewById(R.id.ratingBarDescriptionAVG);

        recyclerView = findViewById(R.id.ratesRecycler);


        Intent intent = this.getIntent();
        rates = new ArrayList<>();
        rates = intent.getParcelableArrayListExtra("found rates");

        float avgContactRate = 0.0f;
        float avgDescriptionRate = 0.0f;

        for (int i = 0; i < rates.size(); i++) {
            avgContactRate += rates.get(i).getContactRate();
            avgDescriptionRate += rates.get(i).getDescriptionRate();
        }
        avgContactRate /= rates.size();
        avgDescriptionRate /= rates.size();

        contactRateAVG.setRating(avgContactRate);
        descriptionRateAVG.setRating(avgDescriptionRate);


        recyclerView = findViewById(R.id.ratesRecycler);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RateAdapter(this, rates);
        recyclerView.setAdapter(adapter);
    }
}
