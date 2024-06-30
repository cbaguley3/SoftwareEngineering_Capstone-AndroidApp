package com.example.d308app.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308app.R;
import com.example.d308app.entities.Vacation;
import java.util.ArrayList;
import java.util.List;

public class VacationAdaptor extends RecyclerView.Adapter<VacationAdaptor.VacationViewHolder> {
    private final LayoutInflater mInflater;
    private List<Vacation> mVacations; // Cached copy of vacations
    private List<Vacation> mVacationsFiltered; // List for filtered vacations

    public VacationAdaptor(Context context) {
        mInflater = LayoutInflater.from(context);
        mVacations = new ArrayList<>();
        mVacationsFiltered = new ArrayList<>();
    }

    @Override
    public VacationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VacationViewHolder holder, int position) {
        if (mVacationsFiltered != null && !mVacationsFiltered.isEmpty()) {
            Vacation current = mVacationsFiltered.get(position);
            holder.vacationItemView.setText(current.getVacationName());
        }
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        mVacationsFiltered = new ArrayList<>(vacations);
        notifyDataSetChanged();
    }

    public List<Vacation> getVacations() {
        return mVacations; // Return current list of vacations
    }

    @Override
    public int getItemCount() {
        return mVacationsFiltered != null ? mVacationsFiltered.size() : 0;
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

    class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        private VacationViewHolder(View itemView) {
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView);
        }
    }
}
