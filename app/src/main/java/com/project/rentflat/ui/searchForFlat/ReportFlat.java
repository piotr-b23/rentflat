package com.project.rentflat.ui.searchForFlat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;
import com.project.rentflat.models.Flat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class ReportFlat extends AppCompatActivity {

    private static String URL_REPORT = serverIp + "/report_flat.php";
    private EditText reportDescription;
    private Button confirmReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_flat);

        reportDescription = findViewById(R.id.reportDescription);
        confirmReport = findViewById(R.id.confirmReportButton);


        Intent intent = getIntent();
        final Flat reportedFlat = intent.getParcelableExtra("reported flat");

        confirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String id = userId;
                String reportText = reportDescription.getText().toString();
                String flatId = Integer.toString(reportedFlat.getFlatId());
                sendReport(flatId, id, reportText);

            }
        });

    }

    private void sendReport(final String flatId, final String reportingUserId, final String reportDescription) {

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
                            } else {
                                Toast.makeText(ReportFlat.this, "Wystąpił problem przy zgłaszaniu", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ReportFlat.this, "Wystąpił problem. Sprawdź połączenie z internetem.", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ReportFlat.this, "Wystąpił problem.", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("flatId", flatId);
                params.put("reportUserId", reportingUserId);
                params.put("comment", reportDescription);

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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
