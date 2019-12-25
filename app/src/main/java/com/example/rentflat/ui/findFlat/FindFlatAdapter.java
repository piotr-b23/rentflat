package com.example.rentflat.ui.findFlat;

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

public class FindFlatAdapter extends RecyclerView.Adapter<FindFlatAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Flat> data;

    FindFlatAdapter(Context context, List<Flat> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.find_flat_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String title = data.get(position).generateTitle();
        holder.findFlatTitle.setText(title);
        String description = data.get(position).generateDescription();
        holder.findFlatDescription.setText(description);
        ArrayList<String> photos = new ArrayList<>();
        photos = data.get(position).generatePhotos();
        Picasso.get().load(photos.get(0)).into(holder.findFlatImage);
        String date = data.get(position).getDate();
        holder.findFlatDate.setText(date);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView findFlatTitle, findFlatDescription,findFlatDate;
        ImageView findFlatImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), FindFlatDetails.class);
                    intent.putExtra("selected flat", data.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });

            findFlatTitle = itemView.findViewById(R.id.findFlatTitle);
            findFlatDescription = itemView.findViewById(R.id.findFlatDescription);
            findFlatImage = itemView.findViewById(R.id.findFlatCardImage);
            findFlatDate = itemView.findViewById(R.id.findFlatDate);
        }
    }
}
