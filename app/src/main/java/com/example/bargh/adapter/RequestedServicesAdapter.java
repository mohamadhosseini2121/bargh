package com.example.bargh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.R;
import com.example.bargh.db.entity.UserRepairRequest;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestedServicesAdapter extends RecyclerView.Adapter<RequestedServicesAdapter.ServicesViewHolder> {

    List<UserRepairRequest> userRepairRequests;
    Context context;
    OnRequestedServicesLongClick onRequestedServicesLongClick;

    public RequestedServicesAdapter(Context context,
                                    List<UserRepairRequest> userRepairRequests,
                                    OnRequestedServicesLongClick onRequestedServicesLongClick) {

        this.userRepairRequests = userRepairRequests;
        this.context = context;
        this.onRequestedServicesLongClick = onRequestedServicesLongClick;

    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_requested_service, parent, false);
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {

        UserRepairRequest userRepairRequest = userRepairRequests.get(position);
        holder.statusTv.setText(userRepairRequest.getStateString());
        holder.typeTv.setText(userRepairRequest.getType());
        holder.infoTv.setText(userRepairRequest.getInfo());
        holder.timeTv.setText(userRepairRequest.getDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                onRequestedServicesLongClick.onRequestedServiceLongClick(userRepairRequest);
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return userRepairRequests.size();
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

    public interface OnRequestedServicesLongClick {
        void onRequestedServiceLongClick(UserRepairRequest userRepairRequest);
    }

}
