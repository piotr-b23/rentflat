package com.example.rentflat.ui.myFlats;

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
import com.example.rentflat.ui.flat.ChangeFlatDescription;
import com.example.rentflat.ui.flat.ChangeFlatPrice;
import com.example.rentflat.ui.flat.Flat;
import com.example.rentflat.ui.imageDisplay.ImageAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.TOKEN;
import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;
import static com.example.rentflat.MainActivity.userId;

public class MyFlatDetails extends AppCompatActivity {

    private TextView price, surface, room, type, province, locality, street, students, description;
    private Button editPrice, editDescription, closeOffer;
    private ArrayList<String> photos;
    private static String URL_CLOSE_OFFER = serverIp + "/close_offer.php";
    RecyclerView recyclerView;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_flat_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        final Flat selectedFlat = (Flat) intent.getParcelableExtra("selected flat");

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
        closeOffer = findViewById(R.id.closeOfferButton);

        price.setText(Integer.toString(selectedFlat.getPrice()));
        surface.setText(Integer.toString(selectedFlat.getSurface()));
        room.setText(Integer.toString(selectedFlat.getRoom()));
        type.setText(selectedFlat.getType());
        province.setText(selectedFlat.getProvince());
        locality.setText(selectedFlat.getLocality());
        street.setText(selectedFlat.getStreet());
        description.setText(selectedFlat.getDescription());

        if (selectedFlat.getStudents().equals("1")) students.setText("tak");
        else students.setText("nie");

        photos = new ArrayList<>();
        photos = selectedFlat.generatePhotos();


        ArrayList<String> photos = selectedFlat.generatePhotosToDisplay();

        recyclerView = findViewById(R.id.myFlatImageRecycler);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ImageAdapter(this, photos);
        recyclerView.setAdapter(adapter);

        if (sessionMenager.isLogged()) {

            editPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeFlatPrice.class);
                    intent.putExtra("old price", selectedFlat.getPrice());
                    intent.putExtra("flat id", selectedFlat.getFlatId());
                    startActivity(intent);
                }
            });

            editDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChangeFlatDescription.class);
                    intent.putExtra("old description", selectedFlat.getDescription());
                    intent.putExtra("flat id", selectedFlat.getFlatId());
                    startActivity(intent);
                }
            });

            closeOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CloseOffer(Integer.toString(selectedFlat.getFlatId()),userId);

                }
            });

        }


    }

    private void CloseOffer(final String flatId, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CLOSE_OFFER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(MyFlatDetails.this, "Zakończono ogłoszenie", Toast.LENGTH_SHORT).show();
                                Intent intent = new  Intent(MyFlatDetails.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MyFlatDetails.this, "Wystąpił błąd przy zamykaniu ogłoszenia", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MyFlatDetails.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MyFlatDetails.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatId", flatId);
                params.put("userId", userId);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("Authorization-token",TOKEN);

                return headers;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
