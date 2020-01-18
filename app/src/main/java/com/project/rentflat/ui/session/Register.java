package com.project.rentflat.ui.session;

import android.content.Intent;
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
import com.project.rentflat.R;
import com.project.rentflat.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.serverIp;

public class Register extends AppCompatActivity {

    private static String URL_REGISTER = serverIp + "/register.php";
    private EditText name, username, email, password, confirmedPassword;
    private Button registerButton;

    public static boolean isEmailValid(String email) {
        String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return email.matches(emailRegex);
    }

    public static boolean isUsernameValid(String username) {
        String usernameRegex = "^[a-zA-Z0-9]{3,16}$";
        return username.matches(usernameRegex);
    }

    public static boolean isNameValid(String name) {
        String nameRegex = "^[a-zA-Z]{3,16}$";
        return name.matches(nameRegex);
    }

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
                        if (regPassword.length() >= 3 && regPassword.length() <= 24) {
                            if (isEmailValid(regEmail) && isNameValid(regName) && isUsernameValid(regUsername)) {
                                Register(regName, regUsername, regPassword, regEmail);

                            } else {
                                if (!isEmailValid(regEmail)) {
                                    email.setError("Podaj poprawny email");
                                }
                                if (!isNameValid(regName)) {
                                    name.setError("Imię powinno składać się z od 3 - 16 znaków, oraz powinno zawierać tylko litery");
                                }
                                if (!isUsernameValid(regUsername)) {
                                    username.setError("Nazwa użytkownika powinna składać się z od 3 - 16 znaków, oraz powinna zawierać tylko litery i liczby");
                                }

                            }
                        } else {
                            password.setError("Hasło powinno składać się z od 3 - 24 znaków");
                            confirmedPassword.setError("Hasło powinno składać się z od 3 - 24 znaków");
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

    private void Register(final String name, final String username, final String password, final String email) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(Register.this, "Zarejestrowano", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Register.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Register.this, "Wystąpił problem z rejestracją", Toast.LENGTH_SHORT).show();
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
}
