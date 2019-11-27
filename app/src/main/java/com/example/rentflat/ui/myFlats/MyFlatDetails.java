package com.example.rentflat.ui.myFlats;

import android.content.Intent;
import android.os.Bundle;

import com.example.rentflat.ui.flat.Flat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rentflat.R;

public class MyFlatDetails extends AppCompatActivity {

    private TextView price, surface, room,type, province, locality, street, students, description;
    private Button editPrice, editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flat_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        Flat selectedFlat = (Flat) i.getParcelableExtra("selected flat");

        price = findViewById(R.id.mySelectedFlatPrice);
        surface = findViewById(R.id.mySelectedFlatSurface);
        room = findViewById(R.id.mySelectedFlatRooms);
        type = findViewById(R.id.mySelectedFlatType);
        province = findViewById(R.id.mySelectedFlatProvince);
        locality = findViewById(R.id.mySelectedFlatLocality);
        street = findViewById(R.id.mySelectedFlatStreet);
        description = findViewById(R.id.mySelectedFlatDescription);
        students = findViewById(R.id.mySelectedFlatStudents);

        editPrice = findViewById(R.id.changePriceButton);
        editDescription = findViewById(R.id.changeDescriptionButton);

        price.setText(selectedFlat.getPrice());
        surface.setText(selectedFlat.getSurface());
        room.setText(selectedFlat.getRoom());
        type.setText(selectedFlat.getType());
        province.setText(selectedFlat.getProvince());
        locality.setText(selectedFlat.getLocality());
        street.setText(selectedFlat.getStreet());
        description.setText(selectedFlat.getDescription());

        if(selectedFlat.getStudents().equals("1")) students.setText("tak");
        else students.setText("nie");


    }
}
