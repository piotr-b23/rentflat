package com.example.rentflat.ui.addFlat;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.rentflat.R;

public class AddFlat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner buildingType = findViewById(R.id.buildingTypeSpinner);
        ArrayAdapter<CharSequence> buildingTypeAdapter = ArrayAdapter.createFromResource(this,R.array.building_type,android.R.layout.simple_spinner_item);
        buildingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingType.setAdapter(buildingTypeAdapter);

        Spinner province = findViewById(R.id.provinceSpinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,R.array.province,android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

    }
}
