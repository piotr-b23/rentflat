package com.example.rentflat.ui.myAccount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;
import static com.example.rentflat.ui.register.Register.isEmailValid;

public class ChangePhone extends AppCompatActivity {

    private EditText newPhone;
    private Button changePhoneButton;
    private static String URL_CHANGE_MAIL = serverIp + "/edit_phone.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        newPhone = findViewById(R.id.newPhone);
        changePhoneButton = findViewById(R.id.confirmPhoneChange);

        changePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPhone = newPhone.getText().toString().trim();
                String id = userId;

                if (!upPhone.isEmpty()) {
                    if (isPhoneValid(upPhone)) {
                        UpdatePhone(upPhone, id);

                    } else {
                        newPhone.setError("Podaj poprawny numer telefonu");
                    }


                } else {
                    if (upPhone.isEmpty()) newPhone.setError("Podaj nowy numer telefonu.");
                }

            }
        });


    }

    private void UpdatePhone(final String phone, final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(ChangePhone.this, "Zaktualizowano numer telefonu", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ChangePhone.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePhone.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePhone.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("phone", phone);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private boolean isPhoneValid(String phone) {
        Pattern p = Pattern.compile("[0-9]{9}");
        Matcher m = p.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }
}

