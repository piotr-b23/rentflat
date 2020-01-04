package com.project.rentflat.ui.message;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.rentflat.R;
import com.project.rentflat.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class SendMessage extends AppCompatActivity {

    private EditText title, body;
    private TextView recipientName;
    private Button sendMessage;
    private static String URL_SEND_MESSAGE = serverIp + "/send_message.php";
    private static String URL_GET_USER_NAME = serverIp + "/get_name.php";
    private Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);



        Intent intent = getIntent();
        final String recipientId = Integer.toString(intent.getIntExtra("recipientId",0));
        final String isReplay = intent.getStringExtra("is replay");

        getName(recipientId);

        title = findViewById(R.id.sendMessageTitle);
        body = findViewById(R.id.sendMessageText);
        recipientName = findViewById(R.id.recipientName);
        sendMessage = findViewById(R.id.confirmSendMessage);

        if(isReplay.equals("1")){
            message = intent.getParcelableExtra("replay message");
            title.setText("RE: " +  message.getTitle());
            title.setFocusable(false);
        }



        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String senderId = userId;
                String recipientUserId = recipientId;
                String messageTitle = title.getText().toString();
                String text = body.getText().toString();

                if(isReplay.equals("1")){

                    text += message.generateMessage();
                }

                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());

                SendMessageToUser(senderId,recipientUserId,messageTitle,text,date);
            }
        });

    }

    private void SendMessageToUser(final String senderId, final String recipientId, final String title, final String text, final String date) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SEND_MESSAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if (success.equals("1")) {
                                Toast.makeText(SendMessage.this, "Wysłano wiadomość", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(SendMessage.this, "Wystąpił problem przy edycji oceny", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SendMessage.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendMessage.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("senderId", senderId);
                params.put("recipientId", recipientId);
                params.put("title", title);
                params.put("text", text);
                params.put("date", date);

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
    private void getName(final String userId) {
        String url = String.format(URL_GET_USER_NAME + "?userId=%s", userId);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String userName = jsonObject.getString("name");
                            if (success.equals("1")) {
                                recipientName.setText("Odbiorca: "+userName);


                            } else {
                                Toast.makeText(SendMessage.this, "Wystąpił problem", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SendMessage.this, "Błąd" + e.toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SendMessage.this, "Błąd" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


}
