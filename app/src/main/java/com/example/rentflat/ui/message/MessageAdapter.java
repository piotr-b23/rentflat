package com.example.rentflat.ui.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;
import com.example.rentflat.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private List<Message> data;

    MessageAdapter(Context context, List<Message> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.message_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data.get(position).getTitle();
        holder.messageTitle.setText(title);
        String userName = data.get(position).getUserName();
        holder.messageDescription.setText("Nadawca: "+ userName);
        String date = data.get(position).getDate();
        holder.messageDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTitle, messageDescription, messageDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), SelectedMessageDetails.class);
                    intent.putExtra("selected message", data.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });

            messageTitle = itemView.findViewById(R.id.messageTitle);
            messageDescription = itemView.findViewById(R.id.messageDescription);
            messageDate = itemView.findViewById(R.id.messageDate);
        }
    }
}