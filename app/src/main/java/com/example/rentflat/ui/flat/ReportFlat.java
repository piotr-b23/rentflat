package com.example.rentflat.ui.flat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.TOKEN;
import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class ReportFlat extends AppCompatActivity {

    private EditText reportDescription;
    private Button confirmReport;

    private static String URL_REPORT = serverIp + "/report_flat.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_flat);

        reportDescription = findViewById(R.id.reportDescription);
        confirmReport = findViewById(R.id.confirmReportButton);


        Intent intent = getIntent();
        final Flat reportedFlat = (Flat) intent.getParcelableExtra("reported flat");

        confirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());


                String id = userId;
                String reportText = reportDescription.getText().toString();
                String flatId = Integer.toString(reportedFlat.getFlatId());
                sendReport(flatId, id, reportText, date);

            }
        });

    }

    private void sendReport(final String flatId, final String reportingUserId, final String reportDescription, final String dateTime) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(ReportFlat.this, "Zgłoszono ogłoszenie", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(ReportFlat.this, "Wystąpił problem przy zgłaszaniu", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportFlat.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReportFlat.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatId", flatId);
                params.put("reportUserId", reportingUserId);
                params.put("comment", reportDescription);
                params.put("date", dateTime);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                headers.put("Authorization-token",TOKEN);

                return headers;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
