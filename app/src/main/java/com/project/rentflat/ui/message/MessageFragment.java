package com.project.rentflat.ui.message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.rentflat.ui.MainActivity.TOKEN;
import static com.project.rentflat.ui.MainActivity.serverIp;
import static com.project.rentflat.ui.MainActivity.sessionManager;
import static com.project.rentflat.ui.MainActivity.userId;

public class MessageFragment extends Fragment {

//    private MessageViewModel mViewModel;
    RecyclerView recyclerView;
    MessageAdapter adapter;
    ArrayList<Message> messages;
    private TextView myMessagesText;
    private static String URL_GET_MESSAGES = serverIp + "/get_messages.php";

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_message, container, false);
        String id = userId;
        recyclerView = root.findViewById(R.id.messageRecycler);
        messages = new ArrayList<>();
        myMessagesText = root.findViewById(R.id.myMessagesText);

        if (sessionManager.isLogged()) {
            getMessages(id);
            myMessagesText.setText("Otrzymane wiadomości");
        } else {
            myMessagesText.setText("Zaloguj się by móc otrzymywać i wysyłać wiadomości");
        }


        return root;
    }


private void getMessages(final String userId) {

    String url = String.format(URL_GET_MESSAGES + "?userId=%s", userId);

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String success = jsonObject.getString("success");
                        JSONArray jsonArray = jsonObject.getJSONArray("message");

                        if (success.equals("1")) {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String senderId =object.getString("senderId").trim();
                                String senderName = object.getString("senderName").trim();
                                String title = object.getString("title").trim();
                                String text = object.getString("text").trim();
                                String date = object.getString("date").trim();

                                messages.add(new Message(senderId, senderName, title, text, date));


                            }
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            adapter = new MessageAdapter(getActivity(), messages);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Toast.makeText(getActivity(), "Nie masz żadnych wiadomości.", Toast.LENGTH_SHORT).show();
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
