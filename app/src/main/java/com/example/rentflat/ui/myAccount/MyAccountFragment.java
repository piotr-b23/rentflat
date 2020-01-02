package com.example.rentflat.ui.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;
import com.example.rentflat.ui.accountManagement.ChangeEmail;
import com.example.rentflat.ui.accountManagement.ChangePassword;
import com.example.rentflat.ui.accountManagement.ChangePhone;
import com.example.rentflat.ui.accountManagement.DeleteAccount;
import com.example.rentflat.ui.rate.GetGivenRates;
import com.example.rentflat.ui.rate.Rate;
import com.example.rentflat.ui.session.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.TOKEN;
import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionManager;
import static com.example.rentflat.MainActivity.userId;

public class MyAccountFragment extends Fragment {

    private static String URL_GET_GIVEN_RATES = serverIp + "/get_given_rates.php";
    ArrayList<Rate> rates;
    private MyAccountViewModel myAccountViewModel;
    private Button updateEmail, updatePhone, changePassword, givenRates, register, deleteAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_account, container, false);

        updateEmail = root.findViewById(R.id.updateEmail);
        updatePhone = root.findViewById(R.id.updatePhone);
        changePassword = root.findViewById(R.id.changePassword);
        givenRates = root.findViewById(R.id.givenRatesButton);
        register = root.findViewById(R.id.registerMyAccount);
        deleteAccount = root.findViewById(R.id.deleteAccount);

        if (sessionManager.isLogged()) {

            updateEmail.setVisibility(View.VISIBLE);
            updatePhone.setVisibility(View.VISIBLE);
            changePassword.setVisibility(View.VISIBLE);
            givenRates.setVisibility(View.VISIBLE);
            deleteAccount.setVisibility(View.VISIBLE);
            register.setVisibility(View.GONE);

            updateEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChangeEmail.class);
                    startActivity(intent);
                }
            });

            updatePhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChangePhone.class);
                    startActivity(intent);
                }
            });

            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ChangePassword.class);
                    startActivity(intent);
                }
            });

            givenRates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rates = new ArrayList<>();
                    String id = userId;
                    getGivenRates(id);
                }
            });

            deleteAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), DeleteAccount.class);
                    startActivity(intent);
                }
            });


        } else {
            updateEmail.setVisibility(View.GONE);
            updatePhone.setVisibility(View.GONE);
            changePassword.setVisibility(View.GONE);
            givenRates.setVisibility(View.GONE);
            deleteAccount.setVisibility(View.GONE);
            register.setVisibility(View.VISIBLE);

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Register.class);
                    startActivity(intent);
                }
            });
        }


        return root;
    }

    private void getGivenRates(final String raterId) {
        String url = String.format(URL_GET_GIVEN_RATES + "?raterId=%s", raterId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("rate");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strRateId = object.getString("rateId").trim();
                                    String strUserId = object.getString("userId").trim();
                                    String strContactRate = object.getString("contactRate").trim();
                                    String strDescriptionRate = object.getString("descriptionRate");
                                    String strDescription = object.getString("comment");
                                    String strDate = object.getString("date").trim();

                                    rates.add(new Rate(strRateId, strUserId, strDescription, strDate, Float.valueOf(strDescriptionRate), Float.valueOf(strContactRate)));


                                }
                                Intent intent = new Intent(getActivity(), GetGivenRates.class);
                                intent.putParcelableArrayListExtra("given rates", rates);
                                startActivity(intent);


                            } else {
                                Toast.makeText(getActivity(), "Nie wystawiłeś żadnych ocen", Toast.LENGTH_SHORT).show();
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