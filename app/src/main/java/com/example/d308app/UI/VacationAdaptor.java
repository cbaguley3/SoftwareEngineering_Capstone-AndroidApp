package com.example.d308app.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308app.R;
import com.example.d308app.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class VacationAdaptor extends RecyclerView.Adapter<VacationAdaptor.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private List<Vacation> mVacationsFiltered; // List for filtered vacations
    private final LayoutInflater mInflator;

    public VacationAdaptor(Context context) {
        mInflator = LayoutInflater.from(context);
        mVacations = new ArrayList<>();
        mVacationsFiltered = new ArrayList<>();
        this.context = context;
    }

    public List<Vacation> getVacations() {
        return mVacations;
    }

    public void filter(String text) {
        mVacationsFiltered.clear();
        if (text.isEmpty()) {
            mVacationsFiltered.addAll(mVacations);
        } else {
            text = text.toLowerCase();
            for (Vacation vacation : mVacations) {
                if (vacation.getVacationName().toLowerCase().contains(text)) {
                    mVacationsFiltered.add(vacation);
                }
            }
        }
        notifyDataSetChanged();
    }


     class VacationViewHolder extends RecyclerView.ViewHolder{
        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationItemView=itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    Intent intent = new Intent(context, VacationDetails.class);
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getVacationName());
                    intent.putExtra("lodging", current.getLodging());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdaptor.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflator.inflate(R.layout.vacation_list_item,parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdaptor.VacationViewHolder holder, int position) {
        if(mVacations != null) {
            Vacation current = mVacations.get(position);
            String name = current.getVacationName();
            holder.vacationItemView.setText(name);
        }
        else holder.vacationItemView.setText("No vacation name");
    }
    @Override
    public int getItemCount() {
        if (mVacations != null) {
            return mVacations.size();
        }
        return mVacationsFiltered != null ? mVacationsFiltered.size() : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        mVacationsFiltered = new ArrayList<>(vacations);
        notifyDataSetChanged();
    }

}
