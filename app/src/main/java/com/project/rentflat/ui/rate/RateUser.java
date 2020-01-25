package com.project.rentflat.ui.rate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class RateUser extends AppCompatActivity {

    private static String URL_RATE = serverIp + "/add_rate.php";
    private EditText rateDescription;
    private Button confirmRate;
    private RatingBar contactBar, descriptionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);

        rateDescription = findViewById(R.id.rateComment);
        confirmRate = findViewById(R.id.confirmRateButton);

        contactBar = findViewById(R.id.ratingBarContact);
        descriptionBar = findViewById(R.id.ratingBarDescription);

        Intent intent = getIntent();
        final String ratedUserId = intent.getStringExtra("rated user");

        confirmRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String raterId = userId;
                String descriptionRate = rateDescription.getText().toString();
                String contactRateString = Float.toString(contactBar.getRating());
                String descriptionRateString = Float.toString(descriptionBar.getRating());


                GiveRate(ratedUserId, raterId, contactRateString, descriptionRateString, descriptionRate);
            }
        });

    }

    private void GiveRate(final String userId, final String raterId, final String contactRate, final String descriptionRate, final String comment) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1")) {
                                Toast.makeText(RateUser.this, "Wystawiono ocenę", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                if (message.equals("same user")) {
                                    Toast.makeText(RateUser.this, "Nie możesz ocenić siebie samego", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RateUser.this, "Wystąpił problem w trakcie dodawania oceny", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RateUser.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RateUser.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("raterId", raterId);
                params.put("contactRate", contactRate);
                params.put("descriptionRate", descriptionRate);
                params.put("comment", comment);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("Authorization-token", TOKEN);

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
