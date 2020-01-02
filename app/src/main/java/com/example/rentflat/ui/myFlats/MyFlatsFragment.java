package com.example.rentflat.ui.myFlats;

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
import com.example.rentflat.ui.flat.AddFlat;
import com.example.rentflat.ui.flat.Flat;

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

public class MyFlatsFragment extends Fragment {

    private static String URL_GET_MY_FLATS = serverIp + "/get_my_flats.php";
    RecyclerView recyclerView;
    MyFlatsAdapter adapter;
    ArrayList<Flat> flats;
    private TextView myFlatsText;
    private Button addFlat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_flats, container, false);
        String id = userId;
        recyclerView = root.findViewById(R.id.myFlatsRecycler);
        flats = new ArrayList<>();
        myFlatsText = root.findViewById(R.id.text_my_flat);
        addFlat = root.findViewById(R.id.addFlatButton);

        if (sessionManager.isLogged()) {
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

        String url = String.format(URL_GET_MY_FLATS + "?userId=%s", userId);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
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

                                    int strFlatId = Integer.parseInt(object.getString("id").trim());
                                    int strFlatUserId = Integer.parseInt(object.getString("userid").trim());
                                    int strPrice = Integer.parseInt(object.getString("price").trim());
                                    int strSurface = Integer.parseInt(object.getString("surface").trim());
                                    int strRoom = Integer.parseInt(object.getString("room").trim());
                                    String strProvince = object.getString("province").trim();
                                    String strType = object.getString("type").trim();
                                    String strLocality = object.getString("locality").trim();
                                    String strStreet = object.getString("street").trim();
                                    String strDescription = object.getString("description").trim();
                                    String strStudents = object.getString("students").trim();
                                    String strPhoto = object.getString("photo").trim();
                                    String strDate = object.getString("date").trim();

                                    flats.add(new Flat(strFlatId, strFlatUserId, strPrice, strSurface, strRoom, strProvince, strType, strLocality, strStreet, strDescription, strStudents, strPhoto, strDate));


                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new MyFlatsAdapter(getActivity(), flats);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast.makeText(getActivity(), "Nie masz żadnych ogłoszeń.", Toast.LENGTH_SHORT).show();
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