package com.project.rentflat.ui.myFlats;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.ui.MainActivity;
import com.project.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class ChangeFlatPrice extends AppCompatActivity {

    private static String URL_CHANGE_PRICE = serverIp + "/change_price.php";
    private EditText price;
    private Button changePrice;
    private int flatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_price);

        Intent intent = getIntent();
        int oldPrice = intent.getIntExtra("old price", 0);
        flatId = intent.getIntExtra("flat id", 0);

        price = findViewById(R.id.newPrice);
        price.setText(Integer.toString(oldPrice), TextView.BufferType.EDITABLE);

        changePrice = findViewById(R.id.confirmPriceChange);

        changePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPrice = price.getText().toString().trim();

                if (!upPrice.isEmpty()) {
                    if (Integer.parseInt(upPrice) > 500000 || Integer.parseInt(upPrice) < 50) {

                        price.setError("Podaj poprawną cenę za wynajem.");

                    } else {
                        UpdatePrice(Integer.toString(flatId), upPrice, userId);
                    }


                } else {
                    if (upPrice.isEmpty()) price.setError("Podaj poprawną cenę za wynajem.");
                }

            }
        });

    }

    private void UpdatePrice(final String flatId, final String updatedPrice, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_PRICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(ChangeFlatPrice.this, "Zaktualizowano cenę oferty.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangeFlatPrice.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ChangeFlatPrice.this, "Wystąpił błąd w trakcie zmiany ceny oferty.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeFlatPrice.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeFlatPrice.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatId", flatId);
                params.put("price", updatedPrice);
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
