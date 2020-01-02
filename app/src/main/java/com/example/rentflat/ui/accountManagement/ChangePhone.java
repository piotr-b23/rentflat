package com.example.rentflat.ui.accountManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.rentflat.MainActivity.TOKEN;
import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class ChangePhone extends AppCompatActivity {

    private static String URL_CHANGE_MAIL = serverIp + "/change_phone.php";
    private EditText newPhone;
    private Button changePhoneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        newPhone = findViewById(R.id.newPhone);
        changePhoneButton = findViewById(R.id.confirmPhoneChange);

        changePhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPhone = newPhone.getText().toString();
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

    private void UpdatePhone(final String phone, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(ChangePhone.this, "Zaktualizowano numer telefonu", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ChangePhone.this, "Wystąpił problem przy zmianie numeru telefonu", Toast.LENGTH_SHORT).show();
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
                params.put("userId", userId);
                params.put("phone", phone);

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

    private boolean isPhoneValid(String phone) {
        Pattern p = Pattern.compile("[0-9]{9}");
        Matcher m = p.matcher(phone);
        return (m.find() && m.group().equals(phone));
    }
}

