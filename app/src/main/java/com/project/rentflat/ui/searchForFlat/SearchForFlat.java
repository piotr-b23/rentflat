package com.project.rentflat.ui.searchForFlat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.project.rentflat.R;
import com.project.rentflat.models.FlatSearch;

public class SearchForFlat extends AppCompatActivity {

    private EditText priceMin, priceMax, surfaceMin, surfaceMax, roomMin, roomMax, locality, street;
    private Button findFlatButton;
    private CheckBox studentsCheckBox;
    private FlatSearch query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        priceMin = findViewById(R.id.priceMin);
        priceMax = findViewById(R.id.priceMax);
        surfaceMin = findViewById(R.id.surfaceMin);
        surfaceMax = findViewById(R.id.surfaceMax);
        roomMin = findViewById(R.id.roomMin);
        roomMax = findViewById(R.id.roomMax);
        locality = findViewById(R.id.localitySearch);
        street = findViewById(R.id.streetSearch);

        studentsCheckBox = findViewById(R.id.checkBoxStudentsSearch);
        findFlatButton = findViewById(R.id.findFlatButton);


        final Spinner buildingType = findViewById(R.id.buildingTypeSearchSpinner);
        ArrayAdapter<CharSequence> buildingTypeAdapter = ArrayAdapter.createFromResource(this, R.array.building_type, android.R.layout.simple_spinner_item);
        buildingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingType.setAdapter(buildingTypeAdapter);

        final Spinner province = findViewById(R.id.provinceSearchSpinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this, R.array.province, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        findFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    int sePriceMin = Integer.parseInt(priceMin.getText().toString().trim());
                    int sePriceMax = Integer.parseInt(priceMax.getText().toString().trim());
                    int seSurfaceMin = Integer.parseInt(surfaceMin.getText().toString().trim());
                    int seSurfaceMax = Integer.parseInt(surfaceMax.getText().toString().trim());
                    int seRoomMin = Integer.parseInt(roomMin.getText().toString().trim());
                    int seRoomMax = Integer.parseInt(roomMax.getText().toString().trim());

                    String seLocality = locality.getText().toString().trim();
                    String seStreet = street.getText().toString().trim();
                    String seStudentsCheckBox;
                    if (studentsCheckBox.isChecked()) {
                        seStudentsCheckBox = "1";
                    } else seStudentsCheckBox = "0";


                    String seBuildingType = buildingType.getSelectedItem().toString();
                    String seProvince = province.getSelectedItem().toString();

                    query = new FlatSearch(sePriceMin, sePriceMax, seSurfaceMin, seSurfaceMax, seRoomMin, seRoomMax, seBuildingType, seProvince, seLocality, seStreet, seStudentsCheckBox);

                    if (query.checkPrice() && query.checkRoom() && query.checkSurface()) {
                        Intent intent = new Intent(SearchForFlat.this, SearchForFlatResults.class);
                        intent.putExtra("query", query);
                        startActivity(intent);
                    } else {
                        if (!query.checkPrice()) {
                            priceMax.setError("Cena mininimalna nie może być większa od maksymalnej");
                        }
                        if (!query.checkRoom()) {
                            roomMax.setError("Mininimalna ilość pokoi nie może być większa od maksymalnej");
                        }
                        if (!query.checkSurface()) {
                            surfaceMax.setError("Powierzchnia mininimalna nie może być większa od maksymalnej");
                        }
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(SearchForFlat.this, "Wprowadź zakresy wyszkiwanych ogłoszeń", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

}
