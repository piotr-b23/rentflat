package com.example.rentflat.ui.login;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.rentflat.R;
import com.example.rentflat.ui.SessionMenager;
import com.example.rentflat.ui.register.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;

public class Login extends AppCompatActivity {

    private EditText username, password;
    private Button loginButton;
    private static String URL_LOGIN = serverIp + "/login.php";
    SessionMenager sessionMenager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionMenager = new SessionMenager(this);

        username = findViewById(R.id.nickname);
        password = findViewById(R.id.password);

        loginButton = findViewById(R.id.tryLoginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logUsername = username.getText().toString().trim();
                String logPass = password.getText().toString().trim();

                if (!logUsername.isEmpty() && !logPass.isEmpty()) {
                    Login(logUsername, logPass);
                } else {
                    if (logUsername.isEmpty()) username.setError("Podaj nazwę użytkownika");
                    if (logPass.isEmpty()) password.setError("Podaj hasło użytkownika");
                }
            }
        });
    }

    private void Login(final String username, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (succes.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String username = object.getString("username").trim();
                                    String id = object.getString("id").trim();
                                    Toast.makeText(Login.this, "Zalogowano. \nWitaj " + name, Toast.LENGTH_SHORT).show();

                                    sessionMenager.createSession(name, username, id);
                                }
                                sessionMenager.checkIfLogged();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
