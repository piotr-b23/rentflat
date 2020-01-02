package com.example.rentflat.ui.rate;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;

import java.util.ArrayList;

public class GetGivenRates extends AppCompatActivity {

    RecyclerView recyclerView;
    RateGivenAdapter adapter;
    ArrayList<Rate> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_given_rates);
        Intent intent = this.getIntent();
        rates = new ArrayList<>();
        rates = intent.getParcelableArrayListExtra("given rates");

        recyclerView = findViewById(R.id.givenRatesRecycler);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RateGivenAdapter(this, rates);
        recyclerView.setAdapter(adapter);
    }
}