package com.example.bargh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.R;
import com.example.bargh.datamodel.RequestedService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestedServicesAdapter extends RecyclerView.Adapter<RequestedServicesAdapter.ServicesViewHolder> {

    List<RequestedService> requestedServices;
    Context context;

    public RequestedServicesAdapter(Context context, List<RequestedService> requestedServices) {

        this.requestedServices = requestedServices;
        this.context = context;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_requested_service, parent, false);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {

        RequestedService requestedService = requestedServices.get(position);
        switch (requestedService.getState()) {
            case 0:
                holder.statusTv.setText("در حال بررسی");
                break;
            case 1:
                holder.statusTv.setText("در حال انجام");
                break;
        }
        holder.typeTv.setText(requestedService.getType());
        holder.infoTv.setText(requestedService.getInfo());
        holder.timeTv.setText(requestedService.getDate());

    }

    @Override
    public int getItemCount() {
        return requestedServices.size();
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
