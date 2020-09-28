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
    OnRequestedServicesListener onRequestedServicesListener;

    public RequestedServicesAdapter(Context context,
                                    List<UserRepairRequest> userRepairRequests,
                                    OnRequestedServicesListener onRequestedServicesListener) {

        this.userRepairRequests = userRepairRequests;
        this.context = context;
        this.onRequestedServicesListener = onRequestedServicesListener;

    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_requested_service, parent, false);
        return new ServicesViewHolder(view, onRequestedServicesListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, int position) {

        UserRepairRequest userRepairRequest = userRepairRequests.get(position);
        holder.statusTv.setText(userRepairRequest.getStateString());
        holder.typeTv.setText(userRepairRequest.getType());
        holder.infoTv.setText(userRepairRequest.getInfo());
        holder.timeTv.setText(userRepairRequest.getDate());

        /*holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                onRequestedServicesLongClick.onRequestedServiceLongClick(userRepairRequest);
                return true;
            }
        });*/

    }


    @Override
    public int getItemCount() {
        return userRepairRequests.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        OnRequestedServicesListener onRequestedServicesListener;
        @BindView(R.id.tv_status_service_item)
        TextView statusTv;
        @BindView(R.id.tv_type_service_item)
        TextView typeTv;
        @BindView(R.id.tv_info_service_item)
        TextView infoTv;
        @BindView(R.id.tv_time_service_item)
        TextView timeTv;

        public ServicesViewHolder(@NonNull View itemView, OnRequestedServicesListener onRequestedServicesListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.onRequestedServicesListener = onRequestedServicesListener;
        }

        @Override
        public void onClick(View view) {
            UserRepairRequest userRepairRequest = userRepairRequests.get(getAdapterPosition());
            if (userRepairRequest.isSelected()) {
                userRepairRequest.setSelected(false);
            } else {
                userRepairRequest.setSelected(true);
            }
            onRequestedServicesListener.onClick(userRepairRequests.get(getAdapterPosition()), view);
        }

        @Override
        public boolean onLongClick(View view) {
            onRequestedServicesListener.onLongClick(userRepairRequests.get(getAdapterPosition()), view);
            return true;
        }
    }

    public interface OnRequestedServicesListener {
        void onLongClick(UserRepairRequest userRepairRequest, View view);
        void onClick(UserRepairRequest userRepairRequest, View view);
    }

}
