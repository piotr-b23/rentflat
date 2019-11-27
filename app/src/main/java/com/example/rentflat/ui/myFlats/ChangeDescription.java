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

public class ChangeDescription extends AppCompatActivity {

    private EditText description;
    private Button changeDescription;
    private String flatId;
    private static String URL_CHANGE_DESCRIPTION = serverIp + "/change_description.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_description);

        Intent intent = getIntent();
        String oldDescription = (String) intent.getStringExtra("old description");

        description = findViewById(R.id.newDescription);
        description.setText(oldDescription, TextView.BufferType.EDITABLE);
        changeDescription = findViewById(R.id.confirmDescriptionChange);

        flatId = (String) intent.getStringExtra("flat id");

        changeDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upDescription = description.getText().toString().trim();

                if (!upDescription.isEmpty()) {
                    if (upDescription.length() < 20) {

                        description.setError("Za krótki opis.");

                    } else {
                        UpdateDescription(flatId, upDescription);
                    }


                } else {
                    if (upDescription.isEmpty()) description.setError("Podaj poprawny opis.");
                }

            }
        });

    }

    private void UpdateDescription(final String flatId, final String updatedDescription) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_DESCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(ChangeDescription.this, "Zaktualizowano opis oferty.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangeDescription.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeDescription.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeDescription.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatid", flatId);
                params.put("description", updatedDescription);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}