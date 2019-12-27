package com.example.rentflat.ui.addFlat;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.MainActivity;
import com.example.rentflat.R;
import com.example.rentflat.ui.imageDisplay.ImageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class AddFlat extends AppCompatActivity {

    private static String URL_ADD_FLAT = serverIp + "/add_flat.php";
    private static String URL_UPLOAD_PHOTO = serverIp + "/upload_photo.php";
    public String crePhoto = "";
    RecyclerView recyclerView;
    ImageAdapter adapter;
    private EditText price, surface, room, locality, street, description;
    private Button addFlatButton, addPhoto;
    private CheckBox studentsCheckBox;
    private Bitmap bitmap;
    private List<Bitmap> bitmaps;
    private ArrayList<String> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        price = findViewById(R.id.flatPrice);
        surface = findViewById(R.id.flatSurface);
        room = findViewById(R.id.flatRooms);
        locality = findViewById(R.id.flatLocality);
        street = findViewById(R.id.flatStreet);
        description = findViewById(R.id.flatDescription);
        studentsCheckBox = findViewById(R.id.checkBoxForStudents);

        recyclerView = findViewById(R.id.addFlatImageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addFlatButton = findViewById(R.id.addFlatButton);
        addPhoto = findViewById(R.id.addPhotoButton);

        bitmaps = new ArrayList<>();

        final Spinner buildingType = findViewById(R.id.buildingTypeSpinner);
        ArrayAdapter<CharSequence> buildingTypeAdapter = ArrayAdapter.createFromResource(this, R.array.building_type, android.R.layout.simple_spinner_item);
        buildingTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingType.setAdapter(buildingTypeAdapter);

        final Spinner province = findViewById(R.id.provinceSpinner);
        ArrayAdapter<CharSequence> provinceAdapter = ArrayAdapter.createFromResource(this, R.array.province, android.R.layout.simple_spinner_item);
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(provinceAdapter);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        addFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String crePrice = price.getText().toString().trim();
                String creSurface = surface.getText().toString().trim();
                String creRoom = room.getText().toString().trim();
                String creLocality = locality.getText().toString().trim();
                String creStreet = street.getText().toString().trim();
                String creDescription = description.getText().toString().trim();
                String creStudentsCheckBox;
                if (studentsCheckBox.isChecked()) {
                    creStudentsCheckBox = "1";
                } else creStudentsCheckBox = "0";

                String creBuildingType = buildingType.getSelectedItem().toString();
                String creProvince = province.getSelectedItem().toString();
                String id = userId;


                if (!crePrice.isEmpty() && !creSurface.isEmpty() && !creRoom.isEmpty() && !creLocality.isEmpty() && !creStreet.isEmpty() && !creDescription.isEmpty() && !bitmaps.isEmpty()) {

                    if (checkIfDataIsCorrect(crePrice, creSurface, creRoom, creDescription,creStreet,creLocality) == false) {
                        Toast.makeText(AddFlat.this, "Popraw wprowadzone dane", Toast.LENGTH_SHORT).show();
                    } else {

                        for (Bitmap b : bitmaps) {
                            try {
                                String filename = createTransactionID() + ".jpeg";
                                UploadPhoto(getStringImage(b), "user_data/" + filename);
                                crePhoto = crePhoto + "user_data/" + filename + " ";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = df.format(Calendar.getInstance().getTime());
                        CreateFlat(id, crePrice, creSurface, creRoom, creLocality, creStreet, creDescription, creStudentsCheckBox, creBuildingType, creProvince, crePhoto, date);
                    }


                } else {
                    if (crePrice.isEmpty()) price.setError("Wprowadź cenę za wynajem");
                    if (creSurface.isEmpty()) surface.setError("Wprowadź metraż wynajmowanego obiektu");
                    if (creRoom.isEmpty()) room.setError("Podaj ilość pokoi");
                    if (creLocality.isEmpty()) locality.setError("Podaj miejscowość");
                    if (creStreet.isEmpty()) street.setError("Podaj ulicę");
                    if (creDescription.isEmpty()) description.setError("Napisz krótki opis");
                    if (bitmaps.isEmpty()) Toast.makeText(AddFlat.this, "Dodaj zdjęcia do ogłoszenia", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void CreateFlat(final String id, final String price, final String surface, final String room, final String locality, final String street, final String description, final String students, final String buildingType, final String province, final String photo, final String date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD_FLAT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")) {
                                Toast.makeText(AddFlat.this, "Utworzono ogłoszenie", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddFlat.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(AddFlat.this, "Wystąpił problem w trakcie dodawania ogłoszenia.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddFlat.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddFlat.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", id);
                params.put("price", price);
                params.put("surface", surface);
                params.put("room", room);
                params.put("locality", locality);
                params.put("street", street);
                params.put("description", description);
                params.put("students", students);
                params.put("type", buildingType);
                params.put("province", province);
                params.put("photo", photo);
                params.put("date", date);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void UploadPhoto(final String photo, final String fileName) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD_PHOTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddFlat.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddFlat.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("photo", photo);
                params.put("filename", fileName);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void chooseFile() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            bitmaps = new ArrayList<>();
            photos = new ArrayList<>();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clipData = data.getClipData();

                if (clipData != null) {
                    if (clipData.getItemCount() > 9) {
                        Toast.makeText(AddFlat.this, "Wybierz maksymalnie 9 zdjęć.", Toast.LENGTH_SHORT).show();
                        adapter = new ImageAdapter(this, photos);
                        recyclerView.setAdapter(adapter);
                    } else {
                        int currentElement = 0;
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            Uri filePath = clipData.getItemAt(i).getUri();

                            try {
                                InputStream is = getContentResolver().openInputStream(filePath);
                                bitmap = BitmapFactory.decodeStream(is);
                                bitmap = Bitmap.createScaledBitmap(bitmap, 1920, 1080, false);
                                bitmaps.add(bitmap);

                                if (i % 2 == 0) {
                                    photos.add(filePath.toString());
                                } else {
                                    photos.set(currentElement, photos.get(currentElement) + " " + filePath.toString());
                                    currentElement += 1;
                                }


                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            adapter = new ImageAdapter(this, photos);
                            recyclerView.setAdapter(adapter);
                        }
                    }

                } else {
                    Uri filePath = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(filePath);
                        bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                        photos.add(filePath.toString());
                        adapter = new ImageAdapter(this, photos);
                        recyclerView.setAdapter(adapter);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }


                }
            }

        }
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodedImage;
    }

    public String createTransactionID() throws Exception {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public boolean checkIfDataIsCorrect(String price, String surface, String room, String description, String street, String locality) {

        boolean isCorrect = true;

        if (checkPrice(price) == false) isCorrect = false;
        if (checkSurface(surface) == false) isCorrect = false;
        if (checkRoom(room) == false) isCorrect = false;
        if (checkDescription(description) == false) isCorrect = false;
        if (checkStreet(street) == false) isCorrect = false;
        if (checkLocality(locality) == false) isCorrect = false;

        return isCorrect;
    }

    public boolean checkPrice(String inPrice) {
        if (Integer.parseInt(inPrice) > 500000 || Integer.parseInt(inPrice) < 50) {
            price.setError("Wprowadź poprawną cenę za wynajem.");
            return false;
        } else return true;
    }

    public boolean checkSurface(String inSurface) {
        if (Integer.parseInt(inSurface) > 250000 || Integer.parseInt(inSurface) < 5) {
            surface.setError("Wprowadź poprawny metraż wynajmowanego obiektu.");
            return false;
        } else return true;
    }

    public boolean checkRoom(String inRoom) {
        if (Integer.parseInt(inRoom) > 250 || Integer.parseInt(inRoom) < 1) {
            room.setError("Wprowadź poprawną ilość pokoi.");
            return false;
        } else return true;
    }

    public boolean checkDescription(String inDescription) {
        if (inDescription.length() < 20) {
            description.setError("Za krótki opis.");
            return false;
        }
        else if(inDescription.length()>15000) {
            description.setError("Za długi opis.");
            return false;
        }
            else{
        } return true;
    }

    public boolean checkStreet(String inStreet) {
        if (inStreet.length() > 100) {
            street.setError("Za długa nazwa ulicy.");
            return false;
        } else return true;
    }

    public boolean checkLocality(String inLocality) {
        if (inLocality.length() > 100) {
            street.setError("Za długa nazwa miejscowości.");
            return false;
        } else return true;
    }
}

