package com.project.rentflat.ui.searchForFlat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;
import com.project.rentflat.models.Flat;
import com.project.rentflat.models.Rate;
import com.project.rentflat.ui.imageDisplay.ImageAdapter;
import com.project.rentflat.ui.message.SendMessage;
import com.project.rentflat.ui.rate.RateResults;
import com.project.rentflat.ui.rate.RateUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.sessionManager;
import static com.project.rentflat.ui.MainActivity.userId;

public class SpecificFlatDetails extends AppCompatActivity {

    private static String URL_GET_PHONE = serverIp + "/get_phone.php";
    private static String URL_GET_RATES = serverIp + "/get_rates.php";
    private static String URL_CHECK_IF_RATED = serverIp + "/check_if_rated.php";
    RecyclerView recyclerView;
    ImageAdapter adapter;
    ArrayList<Rate> rates;
    private TextView price, surface, room, type, province, locality, street, students, description;
    private TextView reportFlat, userRates;
    private Button call, sendSMS, rateUser, sendMessage;
    private Flat selectedFlat;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        selectedFlat = intent.getParcelableExtra("selected flat");
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
        sendMessage = findViewById(R.id.sendMessageButton);

        reportFlat = findViewById(R.id.reportFlatClick);
        userRates = findViewById(R.id.viewRates);

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


        if (sessionManager.isLogged()) {


            sendMessage.setVisibility(View.VISIBLE);
            rateUser.setVisibility(View.VISIBLE);
            reportFlat.setVisibility(View.VISIBLE);
            rateUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String id = userId;
                    if (id.equals(selectedFlat.getUserId())) {
                        Toast.makeText(SpecificFlatDetails.this, "Nie możesz ocenić siebie samego.", Toast.LENGTH_SHORT).show();
                    } else {
                        checkIfRated(selectedFlat.getUserId(), id);
                    }

                }
            });

            reportFlat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SpecificFlatDetails.this, ReportFlat.class);
                    intent.putExtra("reported flat", selectedFlat);
                    startActivity(intent);
                }
            });

            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = userId;
                    if (id.equals(selectedFlat.getUserId())) {
                        Toast.makeText(SpecificFlatDetails.this, "Nie możesz wysłać wiadomości do siebie samego.", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(SpecificFlatDetails.this, SendMessage.class);
                        intent.putExtra("recipientId", selectedFlat.getUserId());
                        intent.putExtra("is replay", "0");
                        startActivity(intent);
                    }


                }
            });


        } else {
            rateUser.setVisibility(View.INVISIBLE);
            reportFlat.setVisibility(View.INVISIBLE);
            sendMessage.setVisibility(View.INVISIBLE);
        }
        getPhone(selectedFlat.getUserId());

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phone));
                startActivity(intent);


            }
        });

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("smsto:" + phone));
                startActivity(intent);
            }
        });

        userRates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rates = new ArrayList<>();

                getUserRates(selectedFlat.getUserId());

            }
        });


    }

    private void getPhone(final String userId) {
        String url = String.format(URL_GET_PHONE + "?userId=%s", userId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String phoneResponse = jsonObject.getString("phone");
                            if (success.equals("1")) {

                                phone = phoneResponse;

                                if (phone.equals("null")) {
                                    sendSMS.setVisibility(View.INVISIBLE);
                                    call.setVisibility(View.INVISIBLE);
                                } else {
                                    sendSMS.setVisibility(View.VISIBLE);
                                    call.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem w trakcie próby kontaktu", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem. Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem.", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void getUserRates(final String userId) {
        String url = String.format(URL_GET_RATES + "?userId=%s", userId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("rate");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    int strRateId = Integer.parseInt(object.getString("rateId").trim());
                                    String strUserId = object.getString("userId").trim();
                                    String strContactRate = object.getString("contactRate").trim();
                                    String strDescriptionRate = object.getString("descriptionRate");
                                    String strDescription = object.getString("comment");
                                    String strDate = object.getString("date").trim();

                                    rates.add(new Rate(strRateId, strUserId, strDescription, strDate, Float.valueOf(strDescriptionRate), Float.valueOf(strContactRate)));


                                }
                                Intent intent = new Intent(SpecificFlatDetails.this, RateResults.class);
                                intent.putParcelableArrayListExtra("found rates", rates);
                                startActivity(intent);


                            } else {
                                Toast.makeText(SpecificFlatDetails.this, "Użytkownik nie otrzymał ocen", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem. Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem.", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void checkIfRated(final String userId, final String raterId) {
        String url = String.format(URL_CHECK_IF_RATED + "?userId=%s&raterId=%s", userId, raterId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1")) {
                                Intent intent = new Intent(SpecificFlatDetails.this, RateUser.class);
                                intent.putExtra("rated user", selectedFlat.getUserId());
                                startActivity(intent);

                            } else {
                                if (message.equals("same user")) {
                                    Toast.makeText(SpecificFlatDetails.this, "Nie możesz ocenić siebie samego.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(SpecificFlatDetails.this, "Już oceniłeś tego użytkownika.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem. Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(SpecificFlatDetails.this, "Wystąpił problem.", Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}

