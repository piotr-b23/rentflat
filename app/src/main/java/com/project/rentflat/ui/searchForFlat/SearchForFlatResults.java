package com.project.rentflat.ui.searchForFlat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;
import com.project.rentflat.models.FlatSearch;
import com.project.rentflat.models.Flat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.serverIp;

public class SearchForFlatResults extends AppCompatActivity {


    private static String URL_FIND_FLAT = serverIp + "/find_flat.php";
    RecyclerView recyclerView;
    SearchForFlatAdapter adapter;
    ArrayList<Flat> flats;
    private FlatSearch query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_flat_results);


        Intent intent = this.getIntent();
        query = intent.getParcelableExtra("query");
        flats = new ArrayList<>();
        recyclerView = findViewById(R.id.findFlatsRecycler);


        getFlats(Integer.toString(query.getPriceMin()), Integer.toString(query.getPriceMax()), Integer.toString(query.getSurfaceMin()),
                Integer.toString(query.getSurfaceMax()), Integer.toString(query.getRoomMin()), Integer.toString(query.getRoomMax()),
                query.getBuildingType(), query.getProvince(), query.getLocality(), query.getStreet(), query.getStudentsCheckBox());


    }


    private void getFlats(final String priceMin, final String priceMax, final String surfaceMin, final String surfaceMax, final String roomMin, final String roomMax, final String type, final String province, final String locality, final String street, final String students) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FIND_FLAT,
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
                                    String strFlatUserId = object.getString("userid").trim();
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

                                    flats.add(new Flat(strFlatId, strPrice, strSurface, strRoom, strFlatUserId, strProvince, strType, strLocality, strStreet, strDescription, strStudents, strPhoto, strDate));


                                }

                                recyclerView.setLayoutManager(new LinearLayoutManager(SearchForFlatResults.this));
                                adapter = new SearchForFlatAdapter(SearchForFlatResults.this, flats);
                                recyclerView.setAdapter(adapter);


                            } else {
                                Toast.makeText(SearchForFlatResults.this, "Wystąpił problem przy wyszukiwaniu ogłoszeń", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SearchForFlatResults.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SearchForFlatResults.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("pricemin", priceMin);
                params.put("pricemax", priceMax);
                params.put("surfacemin", surfaceMin);
                params.put("surfacemax", surfaceMax);
                params.put("roommin", roomMin);
                params.put("roommax", roomMax);
                params.put("type", type);
                params.put("province", province);
                params.put("locality", locality);
                params.put("street", street);
                params.put("students", students);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
