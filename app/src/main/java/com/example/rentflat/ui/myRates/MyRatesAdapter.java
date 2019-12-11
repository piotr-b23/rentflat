package com.example.rentflat.ui.rate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentflat.R;
import com.example.rentflat.ui.myRates.ReportRate;

import java.util.List;

public class MyRatesAdapter extends RecyclerView.Adapter<MyRatesAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<Rate> data;

    public MyRatesAdapter(Context context, List<Rate> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.my_rates_row, parent, false);
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
        TextView rateDate, rateDescription, reportRate;
        RatingBar contactRate, descriptionRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            rateDate = itemView.findViewById(R.id.myRateDate);
            rateDescription = itemView.findViewById(R.id.myComments);
            reportRate = itemView.findViewById(R.id.reportRate);

            contactRate = itemView.findViewById(R.id.ratingBarMyContact);
            descriptionRate = itemView.findViewById(R.id.ratingBarMyDescription);

            reportRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ReportRate.class);
                    intent.putExtra("reported rate", data.get(getAdapterPosition()));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
