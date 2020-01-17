package com.project.rentflat.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;
import com.project.rentflat.ui.MainActivity;
import com.project.rentflat.ui.searchForFlat.SearchForFlat;
import com.project.rentflat.ui.session.Login;
import com.project.rentflat.ui.session.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.sessionManager;
import static com.project.rentflat.ui.MainActivity.userId;
import static com.project.rentflat.ui.MainActivity.userName;
import static com.project.rentflat.ui.MainActivity.TOKEN;

public class HomeFragment extends Fragment {

    public TextView name;
    private Button registerButton, loginButton, findFlatButton;
    private static String URL_CHECK_IF_TOKEN_VALID = serverIp + "/check_if_token_valid.php";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        registerButton = root.findViewById(R.id.registerButton);
        loginButton = root.findViewById(R.id.loginButton);
        findFlatButton = root.findViewById(R.id.addFlatButton);


        if (sessionManager.isLogged()) {
            registerButton.setVisibility(View.INVISIBLE);
            loginButton.setText("wyloguj");
            checkIfTokenValid();
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Register.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogged()) {
                    sessionManager.logout();
                    loginButton.setText("zaloguj");
                    registerButton.setVisibility(View.VISIBLE);
                    userId = null;
                    TOKEN = null;
                    userName = null;

                } else {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            }
        });

        findFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchForFlat.class);
                startActivity(intent);
            }
        });

        return root;
    }

    private void checkIfTokenValid() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CHECK_IF_TOKEN_VALID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("0")) {
                                sessionManager.logout();
                                userId = null;
                                TOKEN = null;
                                userName = null;
                                registerButton.setVisibility(View.VISIBLE);
                                loginButton.setText("zaloguj");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }


}