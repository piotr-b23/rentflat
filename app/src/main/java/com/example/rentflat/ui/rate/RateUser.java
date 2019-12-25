package com.example.rentflat.ui.rate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class RateUser extends AppCompatActivity {

    private EditText rateDescription;
    private Button confirmRate;
    private RatingBar contactBar, descriptionBar;

    private static String URL_REPORT = serverIp + "/add_rate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_user);

        rateDescription = findViewById(R.id.rateComment);
        confirmRate = findViewById(R.id.confirmRateButton);

        contactBar = findViewById(R.id.ratingBarContact);
        descriptionBar = findViewById(R.id.ratingBarDescription);

        Intent intent = getIntent();
        final String ratedUserId = (String) intent.getStringExtra("rated user");

        confirmRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String raterId = userId;
                String descriptionRate = rateDescription.getText().toString();
                String contactRateString = Float.toString(contactBar.getRating());
                String descriptionRateString = Float.toString(descriptionBar.getRating());

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(Calendar.getInstance().getTime());


                GiveRate(ratedUserId, raterId, contactRateString, descriptionRateString, descriptionRate, date);
            }
        });

    }

    private void GiveRate(final String userId, final String raterId, final String contactRate, final String descriptionRate, final String comment, final String date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(RateUser.this, "Wystawiono ocenę", Toast.LENGTH_SHORT).show();
                                finish();
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
                params.put("date", date);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
