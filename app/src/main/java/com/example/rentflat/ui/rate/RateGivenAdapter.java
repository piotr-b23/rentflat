package com.example.rentflat.ui.rate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;

import java.util.List;

public class RateGivenAdapter extends RecyclerView.Adapter<RateGivenAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Rate> data;

    public RateGivenAdapter(Context context, List<Rate> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_rates_given_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String date = data.get(position).getDate();
        holder.rateDate.setText(date);

        String description = data.get(position).getRateDescription();
        holder.rateDescription.setText(description);

        float rateContact = data.get(position).getContactRate();
        holder.contactRate.setRating(rateContact);

        float rateDescription = data.get(position).getDescriptionRate();
        holder.descriptionRate.setRating(rateDescription);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rateDate, rateDescription, editRate;
        RatingBar contactRate, descriptionRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            rateDate = itemView.findViewById(R.id.rateDateGiven);
            rateDescription = itemView.findViewById(R.id.commentsGiven);
            editRate = itemView.findViewById(R.id.editRate);

            contactRate = itemView.findViewById(R.id.ratingBarContactGiven);
            descriptionRate = itemView.findViewById(R.id.ratingBarDescriptionGiven);
        }
    }
}
