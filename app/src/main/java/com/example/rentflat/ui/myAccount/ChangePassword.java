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

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;
import static com.example.rentflat.ui.register.Register.isEmailValid;

public class ChangePassword extends AppCompatActivity {

    private EditText password, newPassword, newConfPassword;
    private Button changePasswordButton;
    private static String URL_CHANGE_MAIL = serverIp + "/edit_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        password = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        newConfPassword = findViewById(R.id.confirmedNewPassword);
        changePasswordButton = findViewById(R.id.confirmPassChangeButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upPassword = password.getText().toString().trim();
                String upNewPassword = newPassword.getText().toString().trim();
                String upNewConfPassword = newConfPassword.getText().toString().trim();
                String id = userId;

                if (!upPassword.isEmpty() && !upNewPassword.isEmpty() && !upNewConfPassword.isEmpty()) {
                    if (upNewPassword.equals(upNewConfPassword)) {

                        ChangePass(upPassword, upNewPassword, id);


                    } else {
                        newConfPassword.setError("Podane hasła różnią się");
                    }

                } else {
                    if (upPassword.isEmpty()) password.setError("Podaj hasło użytkownika");
                    if (upNewPassword.isEmpty()) newPassword.setError("Podaj nowe hasło");
                    if (upNewConfPassword.isEmpty())
                        newConfPassword.setError("Potwierdź nowe hasło");
                }

            }
        });


    }

    private void ChangePass(final String password, final String newPassword, final String id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHANGE_MAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            if (success.equals("1")) {
                                Toast.makeText(ChangePassword.this, "Zmieniono hasło", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else{
                                switch (message){
                                    case "error":
                                        Toast.makeText(ChangePassword.this, "Problem przy zmianie hasła", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "password":
                                        Toast.makeText(ChangePassword.this, "Niepoprawne hasło", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(ChangePassword.this, "Błąd", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ChangePassword.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangePassword.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                params.put("password", password);
                params.put("newpassword", newPassword);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
