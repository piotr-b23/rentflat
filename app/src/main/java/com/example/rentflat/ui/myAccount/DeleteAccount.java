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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;
import static com.example.rentflat.MainActivity.userId;

public class DeleteAccount extends AppCompatActivity {

    private Button confimDelete;
    private EditText username, password;

    private static String URL_DELETE_USER_ACCOUNT = serverIp + "/delete_account.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        confimDelete = findViewById(R.id.confirmAccountDelete);
        username = findViewById(R.id.deleteUsername);
        password = findViewById(R.id.deletePassword);


        confimDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String delUsername = username.getText().toString().trim();
                String delPass = password.getText().toString().trim();
                deleteUserAccount(delUsername,delPass);
            }
        });

    }

    private void deleteUserAccount(final String username, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DELETE_USER_ACCOUNT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {

                                sessionMenager.logout();
                                userId = null;
                                Intent intent = new Intent(DeleteAccount.this, MainActivity.class);
                                startActivity(intent);

                            }
                            else {
                                Toast.makeText(DeleteAccount.this, "Podany login lub hasło jest nieprawidłowe.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DeleteAccount.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DeleteAccount.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

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
