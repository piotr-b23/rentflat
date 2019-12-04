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
import android.widget.Button;
import android.widget.TextView;

import com.example.rentflat.R;

import java.util.ArrayList;

import static com.example.rentflat.MainActivity.sessionMenager;

public class FindFlatDetails extends AppCompatActivity {

    private TextView price, surface, room,type, province, locality, street, students, description;
    private Button call, sendSMS, rateUser;

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

        price = findViewById(R.id.findFlatDetailPrice);
        surface = findViewById(R.id.findFlatDetailSurface);
        room = findViewById(R.id.findFlatDetailRoom);
        type = findViewById(R.id.findFlatDetailType);
        province = findViewById(R.id.findFlatDetailProvince);
        locality = findViewById(R.id.findFlatDetailLocality);
        street = findViewById(R.id.findFlatDetailStreet);
        description = findViewById(R.id.findFlatDetailDescription);
        students = findViewById(R.id.findFlatDetailStudents);

        call = findViewById(R.id.callButton);
        sendSMS = findViewById(R.id.smsButton);
        rateUser = findViewById(R.id.rateButton);

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


        if(sessionMenager.isLogged()) {



        }
        else{
            rateUser.setVisibility(View.INVISIBLE);
        }


    }
}
