package com.example.rentflat.ui.myFlats;

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
import com.example.rentflat.ui.addFlat.AddFlat;
import com.example.rentflat.ui.flat.Flat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.sessionMenager;
import static com.example.rentflat.MainActivity.userId;

public class MyFlatsFragment extends Fragment {

    private static String URL_GET_MY_FLATS = serverIp + "/get_my_flats.php";
    private TextView myFlatsText;
    private Button addFlat;
    RecyclerView recyclerView;
    MyFlatsAdapter adapter;
    ArrayList<Flat> flats;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_flats, container, false);
        String id = userId;
        recyclerView = root.findViewById(R.id.myFlatsRecycler);
        flats = new ArrayList<>();
        myFlatsText = root.findViewById(R.id.text_my_flat);
        addFlat = root.findViewById(R.id.addFlatButton);

        if (sessionMenager.isLogged()) {
            myFlatsText.setText("Przeglądaj swoje ogłoszenia");
            addFlat.setVisibility(View.VISIBLE);
            getMyFlats(id);
            addFlat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddFlat.class);
                    startActivity(intent);
                }
            });
        } else {
            myFlatsText.setText("Zaloguj się by móc dodawać ogłoszenia");
            addFlat.setVisibility(View.GONE);
        }


        return root;
    }

    private void getMyFlats(final String userId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_MY_FLATS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("flat");

                            if (success.equals("1")) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String strFlatId = object.getString("id").trim();
                                    String strFlatUserId = object.getString("userid").trim();
                                    String strPrice = object.getString("price").trim();
                                    String strSurface = object.getString("surface").trim();
                                    String strRoom = object.getString("room").trim();
                                    String strProvince = object.getString("province").trim();
                                    String strType = object.getString("type").trim();
                                    String strLocality = object.getString("locality").trim();
                                    String strStreet = object.getString("street").trim();
                                    String strDescription = object.getString("description").trim();
                                    String strStudents = object.getString("students").trim();
                                    String strPhoto = object.getString("photo").trim();
                                    String strDate = object.getString("date").trim();

                                    flats.add(new Flat(strFlatId, strFlatUserId, strPrice, strSurface, strRoom, strProvince, strType, strLocality, strStreet, strDescription, strStudents, strPhoto,strDate));


                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new MyFlatsAdapter(getActivity(), flats);
                                recyclerView.setAdapter(adapter);

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
                params.put("userid", userId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}