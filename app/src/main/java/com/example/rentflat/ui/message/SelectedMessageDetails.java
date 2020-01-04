package com.example.rentflat.ui.message;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rentflat.R;
import com.example.rentflat.models.Message;

public class SelectedMessageDetails extends AppCompatActivity {

    private TextView title, sender, date, body;
    private Button replayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();
        final Message selectedMessage = intent.getParcelableExtra("selected message");

        title = findViewById(R.id.messageReceivedTitle);
        sender = findViewById(R.id.messageReceivedSender);
        date = findViewById(R.id.messageReceivedDate);
        body = findViewById(R.id.messageReceivedText);

        replayButton = findViewById(R.id.replayMessageButton);

        title.setText("Tytuł: " + selectedMessage.getTitle());
        sender.setText("Nadawca: "+ selectedMessage.getUserName());
        date.setText("wysłano: "+ selectedMessage.getDate());
        body.setText(selectedMessage.getDescription());

        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectedMessageDetails.this, SendMessage.class);
                intent.putExtra("recipientId", selectedMessage.getSenderId());
                intent.putExtra("is replay", "1");
                intent.putExtra("replay message", selectedMessage);
                startActivity(intent);
            }
        });


    }
}
