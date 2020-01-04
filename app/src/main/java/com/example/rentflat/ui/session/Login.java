package com.example.rentflat.ui.session;

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
import com.example.rentflat.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.ui.MainActivity.serverIp;

public class Login extends AppCompatActivity {

    private static String URL_LOGIN = serverIp + "/login.php";
    SessionManager sessionManager;
    private EditText username, password;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

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
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");
                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("name").trim();
                                    String token = object.getString("token").trim();
                                    String id = object.getString("id").trim();
                                    Toast.makeText(Login.this, "Zalogowano. \nWitaj " + name, Toast.LENGTH_SHORT).show();

                                    sessionManager.createSession(name, token, id);
                                }
                                sessionManager.checkIfLogged();

                            } else {
                                Toast.makeText(Login.this, "Podany login lub hasło jest nieprawidłowe.", Toast.LENGTH_SHORT).show();
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
