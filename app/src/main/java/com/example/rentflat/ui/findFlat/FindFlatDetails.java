package com.example.rentflat.ui.findFlat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import android.widget.Toast;

import com.example.rentflat.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;

public class FindFlatDetails extends AppCompatActivity {

    private TextView price, surface, room, type, province, locality, street, students, description;
    private TextView reportFlat;
    private Button call, sendSMS, rateUser;
    private static String URL_GET_PHONE = serverIp + "/get_phone.php";

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

        reportFlat = findViewById(R.id.reportFlatClick);

        price.setText(selectedFlat.getPrice());
        surface.setText(selectedFlat.getSurface());
        room.setText(selectedFlat.getRoom());
        type.setText(selectedFlat.getType());
        province.setText(selectedFlat.getProvince());
        locality.setText(selectedFlat.getLocality());
        street.setText(selectedFlat.getStreet());
        description.setText(selectedFlat.getDescription());

        if (selectedFlat.getStudents().equals("1")) students.setText("tak");
        else students.setText("nie");


        if (sessionMenager.isLogged()) {

            rateUser.setVisibility(View.VISIBLE);
            reportFlat.setVisibility(View.VISIBLE);
            rateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(FindFlatDetails.this, RateUser.class);
                    intent.putExtra("rated user", selectedFlat.getUserId());
                    startActivity(intent);
                }
            });

            reportFlat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(FindFlatDetails.this, ReportFlat.class);
                    intent.putExtra("reported flat", selectedFlat);
                    startActivity(intent);
                }
            });


        } else {
            rateUser.setVisibility(View.INVISIBLE);
            reportFlat.setVisibility(View.INVISIBLE);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                callOrText(selectedFlat.getUserId(), "call");


            }
        });

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callOrText(selectedFlat.getUserId(), "text");
            }
        });


    }

    private void callOrText(final String userId, final String callOrText) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_PHONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            String phone = jsonObject.getString("phone");
                            if (succes.equals("1")) {

                                if (callOrText.equals("call")) {
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    intent.setData(Uri.parse("tel:" + phone));
                                    startActivity(intent);
                                } else if (callOrText.equals("text")) {
                                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData(Uri.parse("smsto:" + phone));
                                    startActivity(intent);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(FindFlatDetails.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(FindFlatDetails.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}

