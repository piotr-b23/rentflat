package com.example.rentflat.ui.myFlats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;
import com.example.rentflat.ui.flat.Flat;

import java.util.List;

public class MyFlatsAdapter extends RecyclerView.Adapter<MyFlatsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Flat> data;

    MyFlatsAdapter(Context context, List<Flat> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_flat_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String title = data.get(position).generateTitle();
        holder.myFlatTitle.setText(title);
        String description = data.get(position).generateDescription();
        holder.myFlatDescription.setText(description);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myFlatTitle,myFlatDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            myFlatTitle = itemView.findViewById(R.id.myFlatTitle);
            myFlatDescription = itemView.findViewById(R.id.myFlatDescription);
        }
    }
}
