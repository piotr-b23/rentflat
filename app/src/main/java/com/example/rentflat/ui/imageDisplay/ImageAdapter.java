package com.example.rentflat.ui.imageDisplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private ArrayList<String> data;

    public ImageAdapter(Context context, ArrayList<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.image_display_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArrayList<String> photos = new ArrayList<>();
        photos = data;
        Iterator iterator = photos.iterator();
        Scanner s = new Scanner(photos.get(position));

        if (s.hasNext()){
            Picasso.get().load(s.next()).into(holder.cardImage1);
        }
            if (s.hasNext()){
                Picasso.get().load(s.next()).into(holder.cardImage2);
            }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImage1, cardImage2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(v.getContext(), MyFlatDetails.class);
//                    intent.putExtra("selected flat", data.get(getAdapterPosition()));
//                    v.getContext().startActivity(intent);
//                }
//            });

            cardImage1 = itemView.findViewById(R.id.cardImage1);
            cardImage2 = itemView.findViewById(R.id.cardImage2);
        }
    }
}