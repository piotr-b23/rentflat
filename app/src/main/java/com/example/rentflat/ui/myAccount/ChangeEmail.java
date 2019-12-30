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
import com.example.rentflat.ui.register.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.TOKEN;
import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;
import static com.example.rentflat.ui.register.Register.isEmailValid;


public class ChangeEmail extends AppCompatActivity {
    private EditText newEmail, password;
    private Button changeMailButton;
    private static String URL_CHANGE_MAIL = serverIp + "/edit_mail.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);
        newEmail = findViewById(R.id.newUserEmail);
        password = findViewById(R.id.oldPassword);
        changeMailButton = findViewById(R.id.confirmEmailChange);

        changeMailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPassword = password.getText().toString().trim();
                String upEmail = newEmail.getText().toString().trim();
                String id = userId;

                if (!upPassword.isEmpty() && !upEmail.isEmpty()) {
                    if (isEmailValid(upEmail)) {
                        UpdateMail(upPassword, upEmail, id);

                    } else {
                        newEmail.setError("Podaj poprawny email");
                    }


                } else {
                    if (upPassword.isEmpty()) password.setError("Podaj hasło");
                    if (upEmail.isEmpty()) newEmail.setError("Podaj email użytkownika");
                }

            }
        });


    }

    private void UpdateMail(final String password, final String email, final String userId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1")) {
                                Toast.makeText(ChangeEmail.this, "Zaktualizowano email", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                switch (message){
                                    case "error":
                                        Toast.makeText(ChangeEmail.this, "Problem przy zmianie maila", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "password":
                                        Toast.makeText(ChangeEmail.this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "mail":
                                        Toast.makeText(ChangeEmail.this, "Podany mail jest już wykorzystywany", Toast.LENGTH_SHORT).show();
                                        break;
                                        default:
                                            Toast.makeText(ChangeEmail.this, "Błąd", Toast.LENGTH_SHORT).show();
                                            break;
                                }



                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangeEmail.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeEmail.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", userId);
                params.put("password", password);
                params.put("email", email);

                return params;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("Authorization-token",TOKEN);

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
