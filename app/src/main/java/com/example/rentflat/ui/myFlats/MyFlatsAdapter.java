package com.example.rentflat.ui.myFlats;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;
import com.example.rentflat.ui.flat.Flat;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyFlatsAdapter extends RecyclerView.Adapter<MyFlatsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Flat> data;

    MyFlatsAdapter(Context context, List<Flat> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_flat_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title = data.get(position).generateTitle();
        holder.myFlatTitle.setText(title);
        String description = data.get(position).generateDescription();
        holder.myFlatDescription.setText(description);
        ArrayList<String> photos = new ArrayList<>();
        photos = data.get(position).generatePhotos();
        Picasso.get().load(photos.get(0)).into(holder.myFlatImage);
        String date = data.get(position).getDate();
        holder.myFlatDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myFlatTitle, myFlatDescription, myFlatDate;
        ImageView myFlatImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), MyFlatDetails.class);
                    intent.putExtra("selected flat", data.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });

            myFlatTitle = itemView.findViewById(R.id.findFlatTitle);
            myFlatDescription = itemView.findViewById(R.id.findFlatDescription);
            myFlatImage = itemView.findViewById(R.id.findFlatCardImage);
            myFlatDate = itemView.findViewById(R.id.myFlatDate);
        }
    }
}
