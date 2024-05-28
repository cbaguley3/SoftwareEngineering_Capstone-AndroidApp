package com.example.d308app.UI;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308app.R;
import com.example.d308app.entities.Excursion;

import java.util.List;

public class ExcursionAdaptor extends RecyclerView.Adapter<ExcursionAdaptor.ExcursionViewHolder> {

    private List<ClipData.Item> itemList;

    public AdapterView.OnItemClickListener clickListener;

    public void setClickListener(AdapterView.OnItemClickListener myListener) {
        this.clickListener = myListener;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder{
        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        public ExcursionViewHolder(@NonNull View itemView){
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    final Excursion current=mExcursions.get(position);
                    Intent intent=new Intent(context,ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    String excursionName;
                    intent.putExtra("name", excursionName = excursionItemView.getText().toString());
                    intent.putExtra("date", current.getDate());
                    intent.putExtra("vacationID",current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    public ExcursionAdaptor(Context context){
        mInflater= LayoutInflater.from(context);
        this.context=context;
    }
    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView=mInflater.from(parent.getContext()).inflate(R.layout.excursion_list_item,parent,false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions!=null){
            Excursion current=mExcursions.get(position);
            String name=current.getExcursionName();
            int vacationID= current.getVacationID();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(Integer.toString(vacationID));
        }
        else{
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView.setText("No vacation id");
        }
    }

    public void setExcursions(List<Excursion> excursions){
        mExcursions=excursions;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mExcursions!=null) return mExcursions.size();
        else return 0;
    }
}



