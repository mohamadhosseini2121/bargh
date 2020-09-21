package com.example.bargh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.R;
import com.example.bargh.datamodel.Service;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    List<Service> services;
    Context context;

    public ServicesAdapter(Context context, List<Service> services) {

        this.services = services;
        this.context = context;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_service, parent, false);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {

        Service service = services.get(position);
        switch (service.getState()) {
            case 0:
                holder.statusTv.setText("در حال بررسی");
                break;
            case 1:
                holder.statusTv.setText("در حال انجام");
                break;
        }
        holder.typeTv.setText(service.getType());
        holder.infoTv.setText(service.getInfo());
        holder.timeTv.setText(service.getDate());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_status_service_item)
        TextView statusTv;
        @BindView(R.id.tv_type_service_item)
        TextView typeTv;
        @BindView(R.id.tv_info_service_item)
        TextView infoTv;
        @BindView(R.id.tv_time_service_item)
        TextView timeTv;

        public ServicesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
