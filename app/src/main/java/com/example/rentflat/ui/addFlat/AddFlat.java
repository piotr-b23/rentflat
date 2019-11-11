package com.example.rentflat.ui.addFlat;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.MainActivity;
import com.example.rentflat.ui.register.Register;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class AddFlat extends AppCompatActivity {

    private EditText price, surface, room, locality, street, description;
    private Button addFlatButton;
    private CheckBox studentsCheckBox;
    private static String URL_REGIST = serverIp + "/add_flat.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        price = findViewById(R.id.flatPrice);
        surface = findViewById(R.id.flatSurface);
        room = findViewById(R.id.flatRooms);
        locality = findViewById(R.id.flatLocality);
        street = findViewById(R.id.flatStreet);
        description = findViewById(R.id.flatDescription);
        studentsCheckBox = findViewById(R.id.checkBoxForStudents);

        addFlatButton = findViewById(R.id.addFlatButton);


        final Spinner buildingType = findViewById(R.id.buildingTypeSpinner);
        ArrayAdapter<CharSequence> buildingTypeAdapter = ArrayAdapter.createFromResource(this,R.array.building_type,android.R.layout.simple_spinner_item);
        buildingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingType.setAdapter(buildingTypeAdapter);

        final Spinner province = findViewById(R.id.provinceSpinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,R.array.province,android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        addFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String crePrice = price.getText().toString().trim();
                String creSurface = surface.getText().toString().trim();
                String creRoom = room.getText().toString().trim();
                String creLocality = locality.getText().toString().trim();
                String creStreet = street.getText().toString().trim();
                String creDescription = description.getText().toString().trim();
                String creStudentsCheckBox;
                if(studentsCheckBox.isChecked())
                {
                    creStudentsCheckBox = "1";
                }
                else creStudentsCheckBox = "0";

                String creBuildingType = buildingType.getSelectedItem().toString();
                String creProvince = province.getSelectedItem().toString();
                String id =userId;

                if (!crePrice.isEmpty() && !creSurface.isEmpty() && !creRoom.isEmpty() && !creLocality.isEmpty() && !creStreet.isEmpty()&& !creDescription.isEmpty()){

                            CreateFlat(id,crePrice,creSurface,creRoom,creLocality,creStreet,creDescription,creStudentsCheckBox,creBuildingType,creProvince);




                }
                else {
                    if(crePrice.isEmpty()) price.setError("Wprowadź cenę za wynajem");
                    if(creSurface.isEmpty()) surface.setError("Wprowadź metraż wynajmowanego obiektu");
                    if(creRoom.isEmpty()) room.setError("Podaj ilość pokoi");
                    if(creLocality.isEmpty()) locality.setError("Podaj miejscowość");
                    if(creStreet.isEmpty()) street.setError("Podaj ulicę");
                    if(creDescription.isEmpty()) description.setError("Napisz krótki opis");
                }




            }
        });
    }

    private void CreateFlat(final String id,final String price,final String surface,final String room,final String locality,final String street,final String description,final String students,final String buildingType,final String province){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")){
                                Toast.makeText(AddFlat.this,"Utworzono ogłoszenie",Toast.LENGTH_SHORT).show();
                                Intent intent = new  Intent(AddFlat.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(AddFlat.this,"Błąd" + e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddFlat.this,"Błąd" + error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("userid",id);
                params.put("price",price);
                params.put("surface",surface);
                params.put("room",room);
                params.put("locality",locality);
                params.put("street",street);
                params.put("description",description);
                params.put("students",students);
                params.put("type",buildingType);
                params.put("province",province);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    }

