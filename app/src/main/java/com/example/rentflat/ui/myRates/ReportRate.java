package com.example.rentflat.ui.myRates;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentflat.R;
import com.example.rentflat.ui.rate.Rate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentflat.MainActivity.serverIp;
import static com.example.rentflat.MainActivity.userId;

public class ReportRate extends AppCompatActivity {

    private EditText reportRateText;
    private Button confirmReport;


    private static String URL_REPORT_RATE= serverIp + "/report_rate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_rate);


        reportRateText = findViewById(R.id.reportRateDescription);
        confirmReport = findViewById(R.id.confirmRateReportButton);

        Intent intent = getIntent();
        final Rate editedRate = (Rate) intent.getParcelableExtra("reported rate");



        confirmReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String reportRateComment = reportRateText.getText().toString().trim();
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());
                String id = userId;


                reportRate(editedRate.getRateId(),id,reportRateComment,date);
            }
        });
    }

    private void reportRate(final String rateId,final String reportingUserId,final String comment, final String dateTime){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REPORT_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String succes = jsonObject.getString("success");
                            if (succes.equals("1")){
                                Toast.makeText(ReportRate.this,"Zgłoszono ocenę",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(ReportRate.this,"Błąd" + e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReportRate.this,"Błąd" + error.toString(),Toast.LENGTH_SHORT).show();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("rateId",rateId);
                params.put("reportingUserId",reportingUserId);
                params.put("comment",comment);
                params.put("date",dateTime);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}