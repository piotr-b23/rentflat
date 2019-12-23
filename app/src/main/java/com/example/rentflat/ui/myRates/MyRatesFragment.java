package com.example.rentflat.ui.myRates;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;
import com.example.rentflat.ui.flat.Flat;
import com.example.rentflat.ui.myFlats.MyFlatsAdapter;
import com.example.rentflat.ui.rate.Rate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;
import static com.example.rentflat.MainActivity.userId;

public class MyRatesFragment extends Fragment {

    private static String URL_GET_MY_RATES = serverIp + "/get_rates.php";
    private RatingBar contactRateAVG, descriptionRateAVG;
    private TextView contact,description, myRatesText;
    RecyclerView recyclerView;
    MyRatesAdapter adapter;
    ArrayList<Rate> rates;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_rates, container, false);

        String id =userId;
        recyclerView = root.findViewById(R.id.myRatesRecycler);
        rates = new ArrayList<>();

        contactRateAVG = root.findViewById(R.id.ratingBarMyContactAVG);
        descriptionRateAVG = root.findViewById(R.id.ratingBarMyDescriptionAVG);

        contact = root.findViewById(R.id.myRatesContact);
        description = root.findViewById(R.id.myRatesDescription);
        myRatesText = root.findViewById(R.id.text_my_rates);



        if(sessionMenager.isLogged()){
            contactRateAVG.setVisibility(View.VISIBLE);
            descriptionRateAVG.setVisibility(View.VISIBLE);
            contact.setVisibility(View.VISIBLE);
            description.setVisibility(View.VISIBLE);
            myRatesText.setText("Otrzymane oceny");
            getMyRates(id);
        }
        else{
            contactRateAVG.setVisibility(View.GONE);
            descriptionRateAVG.setVisibility(View.GONE);
            contact.setVisibility(View.GONE);
            description.setVisibility(View.GONE);
            myRatesText.setText("Zaloguj się by przeglądać otrzymane oceny");
        }
        return root;
    }
    private void getMyRates(final String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_MY_RATES,
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

                                    rates.add(new Rate(strRateId, strUserId, strRaterId, strDescription, strDate,Float.valueOf(strDescriptionRate), Float.valueOf(strContactRate)));


                                }

                                float avgContactRate = 0.0f;
                                float avgDescriptionRate = 0.0f;

                                for (int i=0;i<rates.size();i++){
                                    avgContactRate += rates.get(i).getContactRate();
                                    avgDescriptionRate += rates.get(i).getDescriptionRate();
                                }
                                avgContactRate /=rates.size();
                                avgDescriptionRate /=rates.size();

                                contactRateAVG.setRating(avgContactRate);
                                descriptionRateAVG.setRating(avgDescriptionRate);

                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new MyRatesAdapter(getActivity(), rates);
                                recyclerView.setAdapter(adapter);






                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"Błąd" + e.toString(),Toast.LENGTH_SHORT).show();
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
                params.put("userid", userId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);


    }
}