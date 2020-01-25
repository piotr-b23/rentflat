package com.project.rentflat.ui.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.project.rentflat.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.userId;

public class SendMessage extends AppCompatActivity {

    private static String URL_SEND_MESSAGE = serverIp + "/send_message.php";
    private static String URL_GET_USER_NAME = serverIp + "/get_name.php";
    private EditText title, body;
    private TextView recipientName;
    private Button sendMessage;
    private Message message;
    private boolean replay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);


        Intent intent = getIntent();
        final String recipientId = intent.getStringExtra("recipientId");
        final String isReplay = intent.getStringExtra("is replay");

        getName(recipientId);

        title = findViewById(R.id.sendMessageTitle);
        body = findViewById(R.id.sendMessageText);
        recipientName = findViewById(R.id.recipientName);
        sendMessage = findViewById(R.id.confirmSendMessage);

        if (isReplay.equals("1")) {
            message = intent.getParcelableExtra("replay message");
            title.setText("RE: " + message.getTitle());
            title.setFocusable(false);
            replay = true;
        } else {
            replay = false;
        }


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String senderId = userId;
                String recipientUserId = recipientId;
                String messageTitle = title.getText().toString();
                String text = body.getText().toString();

                if (checkIfMessageCorrect(messageTitle, text, replay)) {

                    if (isReplay.equals("1")) {

                        text += message.generateMessage();

                    }
                    SendMessageToUser(senderId, recipientUserId, messageTitle, text);


                }
            }
        });

    }

    private void SendMessageToUser(final String senderId, final String recipientId, final String title, final String text) {

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
                                Toast.makeText(SendMessage.this, "Wystąpił problem przy wysyłaniu wiadomości", Toast.LENGTH_SHORT).show();
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
                                recipientName.setText("Odbiorca: " + userName);

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

    public boolean checkIfMessageCorrect(String title, String text, boolean isReplay) {

        boolean isCorrect = true;

        if (checkTitle(title, isReplay) == false) isCorrect = false;
        if (checkText(text) == false) isCorrect = false;

        return isCorrect;
    }

    public boolean checkTitle(String inTitle, boolean isReplay) {
        if (inTitle.length() > 30 && !isReplay) {
            title.setError("Za długi tytuł.");
            return false;
        } else if (inTitle.length() < 5) {
            title.setError("Za krótki tytuł.");
            return false;
        } else return true;
    }

    public boolean checkText(String inText) {
        if (inText.length() > 300) {
            body.setError("Za długi tekst.");
            return false;
        } else if (inText.length() < 2) {
            body.setError("Za krótki tekst.");
            return false;
        } else return true;
    }


}
