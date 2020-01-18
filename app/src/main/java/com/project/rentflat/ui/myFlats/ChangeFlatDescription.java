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
import com.project.rentflat.R;
import com.project.rentflat.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class ChangeFlatDescription extends AppCompatActivity {

    private static String URL_CHANGE_DESCRIPTION = serverIp + "/change_description.php";
    private EditText description;
    private Button changeDescription;
    private int flatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_description);

        Intent intent = getIntent();
        String oldDescription = intent.getStringExtra("old description");

        description = findViewById(R.id.newDescription);
        description.setText(oldDescription, TextView.BufferType.EDITABLE);
        changeDescription = findViewById(R.id.confirmDescriptionChange);

        flatId = intent.getIntExtra("flat id", 0);

        changeDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upDescription = description.getText().toString().trim();

                if (!upDescription.isEmpty()) {
                    if (upDescription.length() < 20) {

                        description.setError("Za krótki opis.");

                    } else {
                        UpdateDescription(Integer.toString(flatId), upDescription, userId);
                    }


                } else {
                    if (upDescription.isEmpty()) description.setError("Podaj poprawny opis.");
                }

            }
        });

    }

    private void UpdateDescription(final String flatId, final String updatedDescription, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_DESCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(ChangeFlatDescription.this, "Zaktualizowano opis oferty.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangeFlatDescription.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ChangeFlatDescription.this, "Wystąpił błąd w trakcie zmiany opisu oferty.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeFlatDescription.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeFlatDescription.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatId", flatId);
                params.put("description", updatedDescription);
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