package com.example.rentflat.ui.register;

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
import com.example.rentflat.ui.login.Login;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;

public class Register extends AppCompatActivity {

    private EditText name, username, email, password, confirmedPassword;
    private Button registerButton;
    private static String URL_REGIST = serverIp + "/regist.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        username = findViewById(R.id.nickname);
        email = findViewById(R.id.userEmail);
        password = findViewById(R.id.password);
        confirmedPassword = findViewById(R.id.confirmedPassword);
        registerButton = findViewById(R.id.confirmRegistrationButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String regName = name.getText().toString().trim();
                String regUsername = username.getText().toString().trim();
                String regPassword = password.getText().toString().trim();
                String regConfirmedPassword = confirmedPassword.getText().toString().trim();
                String regEmail = email.getText().toString().trim();

                if (!regName.isEmpty() && !regUsername.isEmpty() && !regPassword.isEmpty() && !regConfirmedPassword.isEmpty() && !regEmail.isEmpty()) {
                    if (regPassword.equals(regConfirmedPassword)) {
                        if (isEmailValid(regEmail)) {
                            Regist(regName, regUsername, regPassword, regEmail);

                        } else {
                            email.setError("Podaj poprawny email");
                        }

                    } else {
                        confirmedPassword.setError("Podane hasła różnią się");
                    }

                } else {
                    if (regName.isEmpty()) name.setError("Podaj imię użytkownika");
                    if (regUsername.isEmpty()) username.setError("Podaj nazwę użytkownika");
                    if (regPassword.isEmpty()) password.setError("Podaj hasło");
                    if (regConfirmedPassword.isEmpty()) confirmedPassword.setError("Podaj hasło");
                    if (regEmail.isEmpty()) email.setError("Podaj email użytkownika");
                }


            }
        });
    }

    private void Regist(final String name, final String username, final String password, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(Register.this, "Zarejestrowano", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Register.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Register.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        if (email.matches(emailRegex)) {
            return true;
        }
        return false;
    }
}
