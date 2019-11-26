package com.example.rentflat.ui.myFlats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.rentflat.ui.findFlat.FindFlat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class MyFlatsFragment extends Fragment {

    private MyFlatsViewModel myFlatsViewModel;
    private static String URL_GET_MY_FLATS = serverIp + "/get_my_flats.php";
    RecyclerView recyclerView;
    MyFlatsAdapter adapter;
    ArrayList<String> flats;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFlatsViewModel =
                ViewModelProviders.of(this).get(MyFlatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_flats, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        myFlatsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        String id =userId;
        recyclerView = root.findViewById(R.id.myFlatsRecycler);
        getMyFlats(id);

        flats = new ArrayList<>();
//        flats.add("First card");
//        flats.add("Second card");
//        flats.add("Third card");
//        flats.add("Fifth card");
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        adapter = new MyFlatsAdapter(getActivity(),flats);
//        recyclerView.setAdapter(adapter);

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

                                    String strName = object.getString("description").trim();
                                    String strUsername = object.getString("locality").trim();

                                    flats.add(strUsername);


                                }
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                adapter = new MyFlatsAdapter(getActivity(),flats);
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
                        Toast.makeText(getActivity(),"Błąd" + error.toString(),Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid",userId);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
}