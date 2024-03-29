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
import com.project.rentflat.models.Rate;
import com.project.rentflat.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class EditRate extends AppCompatActivity {

    private static String URL_UPDATE_RATE = serverIp + "/update_rate.php";
    private EditText rateDescription;
    private Button confirmRateUpdate;
    private RatingBar contactBar, descriptionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rate);

        rateDescription = findViewById(R.id.rateUpdatedComment);
        confirmRateUpdate = findViewById(R.id.confirmUpdateRate);

        contactBar = findViewById(R.id.ratingBarUpdatedContact);
        descriptionBar = findViewById(R.id.ratingBarUpdatedDescription);

        Intent intent = getIntent();
        final Rate editedRate = intent.getParcelableExtra("edited rate");

        rateDescription.setText(editedRate.getRateDescription());
        contactBar.setRating(editedRate.getContactRate());
        descriptionBar.setRating(editedRate.getDescriptionRate());

        confirmRateUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String descriptionRate = rateDescription.getText().toString();
                String contactRateString = Float.toString(contactBar.getRating());
                String descriptionRateString = Float.toString(descriptionBar.getRating());


                UpdateRate(Integer.toString(editedRate.getRateId()), contactRateString, descriptionRateString, descriptionRate, userId);
            }
        });
    }

    private void UpdateRate(final String rateId, final String contactRate, final String descriptionRate, final String comment, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(EditRate.this, "Zaktualizowano ocenę", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(EditRate.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(EditRate.this, "Wystąpił problem przy edycji oceny", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditRate.this, "Wystąpił problem. Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(EditRate.this, "Wystąpił problem.", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rateId", rateId);
                params.put("contactRate", contactRate);
                params.put("descriptionRate", descriptionRate);
                params.put("comment", comment);
                params.put("userId", userId);

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
