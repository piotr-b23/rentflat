package com.example.rentflat.ui.findFlat;

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
import com.example.rentflat.ui.flat.Flat;
import com.example.rentflat.ui.myFlats.MyFlatDetails;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;

public class FindFlat extends AppCompatActivity {

    private static String URL_FIND_FLAT = serverIp + "/find_flat.php";
    private EditText priceMin,priceMax, surfaceMin,surfaceMax, roomMin,roomMax, locality, street;
    private Button findFlatButton;
    private CheckBox studentsCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        ArrayAdapter<CharSequence> buildingTypeAdapter = ArrayAdapter.createFromResource(this,R.array.building_type,android.R.layout.simple_spinner_item);
        buildingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingType.setAdapter(buildingTypeAdapter);

        final Spinner province = findViewById(R.id.provinceSearchSpinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this,R.array.province,android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        findFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sePriceMin = priceMin.getText().toString().trim();
                String sePriceMax = priceMax.getText().toString().trim();
                String seSurfaceMin = surfaceMin.getText().toString().trim();
                String seSurfaceMax = surfaceMax.getText().toString().trim();
                String seRoomMin = roomMin.getText().toString().trim();
                String seRoomMax = roomMax.getText().toString().trim();
                String seLocality = locality.getText().toString().trim();
                String seStreet = street.getText().toString().trim();
                String seStudentsCheckBox;
                if(studentsCheckBox.isChecked())
                {
                    seStudentsCheckBox = "1";
                }
                else seStudentsCheckBox = "0";

                String seBuildingType = buildingType.getSelectedItem().toString();
                String seProvince = province.getSelectedItem().toString();

                getFlats(sePriceMin,sePriceMax,seSurfaceMin,seSurfaceMax,seRoomMin,seRoomMax,seBuildingType,seProvince,seLocality,seStreet,seStudentsCheckBox);

            }
        });

    }

    private void getFlats(final String priceMin,final String priceMax, final String surfaceMin,final String surfaceMax, final String roomMin,final String roomMax,final String type, final String province, final String locality, final String street,final String students) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FIND_FLAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<Flat> flats = new ArrayList<>();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("flat");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strFlatId = object.getString("id").trim();
                                    String strFlatUserId = object.getString("userid").trim();
                                    String strPrice = object.getString("price").trim();
                                    String strSurface = object.getString("surface").trim();
                                    String strRoom= object.getString("room").trim();
                                    String strProvince = object.getString("province").trim();
                                    String strType = object.getString("type").trim();
                                    String strLocality = object.getString("locality").trim();
                                    String strStreet = object.getString("street").trim();
                                    String strDescription = object.getString("description").trim();
                                    String strStudents = object.getString("students").trim();
                                    String strPhoto = object.getString("photo").trim();

                                    flats.add(new Flat(strFlatId,strFlatUserId,strPrice,strSurface,strRoom,strProvince,strType,strLocality,strStreet,strDescription,strStudents,strPhoto));


                                }


                                Intent intent = new Intent(FindFlat.this, FindFlatResults.class);
                                intent.putParcelableArrayListExtra("found flats", flats);
                                startActivity(intent);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FindFlat.this,"Błąd" + e.toString(),Toast.LENGTH_SHORT).show();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FindFlat.this,"Błąd" + error.toString(),Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pricemin",priceMin);
                params.put("pricemax",priceMax);
                params.put("surfacemin",surfaceMin);
                params.put("surfacemax",surfaceMax);
                params.put("roommin",roomMin);
                params.put("roommax",roomMax);
                params.put("type",type);
                params.put("province",province);
                params.put("locality",locality);
                params.put("street",street);
                params.put("students",students);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
