package com.example.flamingolive.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flamingolive.R;
import com.example.flamingolive.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;


    private final Context context;

    private final LayoutInflater mInflater;
    private final String vacationStart;
    private final String vacationEnd;

    public ExcursionAdapter(Context context, String vacationStart, String vacationEnd) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.vacationStart = vacationStart;
        this.vacationEnd = vacationEnd;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context,ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionID());
                    intent.putExtra("excursion_title", current.getExcursionTitle());
                    intent.putExtra("date", current.getExcursionDate());
                    intent.putExtra("vacationID", current.getVacationID());
                    intent.putExtra("vacationStartDate", vacationStart);
                    intent.putExtra("vacationEndDate", vacationEnd);
                    context.startActivity(intent);
                }
            });
        }
    }
    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item,parent, false);
        return new ExcursionViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if (mExcursions != null) {
            Excursion current = mExcursions.get(position);
            String name = current.getExcursionTitle();
            String date = current.getExcursionDate();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(date);
        }
        else {
            holder.excursionItemView.setText("No excursion name");
            holder.excursionItemView2.setText("No excursion date");
        }
    }

    @Override
    public int getItemCount() {
        if (mExcursions != null) {
            return mExcursions.size();
        }
        else return 0;
    }


    public void setExcursions(List<Excursion> excursions) {
        mExcursions = excursions;
        notifyDataSetChanged();
    }


}
