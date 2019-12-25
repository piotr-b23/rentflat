package com.example.rentflat.ui.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;
import com.example.rentflat.ui.rate.Rate;
import com.example.rentflat.ui.register.Register;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;
import static com.example.rentflat.MainActivity.userId;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;
    private Button updateEmail, updatePhone, changePassword, givenRates, register, deleteAccount;

    private static String URL_GET_GIVEN_RATES = serverIp + "/get_given_rates.php";

    ArrayList<Rate> rates;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        final TextView textView = root.findViewById(R.id.text_my_account);

        updateEmail = root.findViewById(R.id.updateEmail);
        updatePhone = root.findViewById(R.id.updatePhone);
        changePassword = root.findViewById(R.id.changePassword);
        givenRates = root.findViewById(R.id.givenRatesButton);
        register = root.findViewById(R.id.registerMyAccount);
        deleteAccount = root.findViewById(R.id.deleteAccount);

        if (sessionMenager.isLogged()) {

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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_GIVEN_RATES,
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
                                    String strRaterId = object.getString("raterId").trim();
                                    String strContactRate = object.getString("contactRate").trim();
                                    String strDescriptionRate = object.getString("descriptionRate");
                                    String strDescription = object.getString("comment");
                                    String strDate = object.getString("date").trim();

                                    rates.add(new Rate(strRateId, strUserId, strRaterId, strDescription, strDate, Float.valueOf(strDescriptionRate), Float.valueOf(strContactRate)));


                                }
                                Intent intent = new Intent(getActivity(), GivenRates.class);
                                intent.putParcelableArrayListExtra("given rates", rates);
                                startActivity(intent);


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
                params.put("raterId", raterId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }
}