package com.example.rentflat.ui.myFlats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.MainActivity;
import com.example.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;

public class ChangePrice extends AppCompatActivity {

    private EditText price;
    private Button changePrice;
    private String flatId;
    private static String URL_CHANGE_PRICE = serverIp + "/change_price.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_price);

        Intent intent = getIntent();
        String oldPrice = (String) intent.getStringExtra("old price");
        flatId = (String) intent.getStringExtra("flat id");

        price = findViewById(R.id.newPrice);
        price.setText(oldPrice, TextView.BufferType.EDITABLE);

        changePrice = findViewById(R.id.confirmPriceChange);

        changePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPrice = price.getText().toString().trim();

                if (!upPrice.isEmpty()) {
                    if (Integer.parseInt(upPrice) > 500000 || Integer.parseInt(upPrice) < 50) {

                        price.setError("Podaj poprawną cenę za wynajem.");

                    } else {
                        UpdatePrice(flatId, upPrice);
                    }


                } else {
                    if (upPrice.isEmpty()) price.setError("Podaj poprawną cenę za wynajem.");
                }

            }
        });

    }

    private void UpdatePrice(final String flatId, final String updatedPrice) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_PRICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(ChangePrice.this, "Zaktualizowano cenę oferty.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePrice.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePrice.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePrice.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatid", flatId);
                params.put("price", updatedPrice);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
