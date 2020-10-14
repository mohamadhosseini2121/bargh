package com.example.bargh.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bargh.R;
import com.example.bargh.datamodel.Service;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> services;
    private int selectedItemPos = -1;
    private boolean userIsAdmin = false;

    public ServicesAdapter(Context context, List<Service> services, boolean userIsAdmin) {

        this.context = context;
        this.services = services;
        this.userIsAdmin = userIsAdmin;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.nameTv.setText(service.getName());
        holder.infoTv.setText(service.getInfo());


        if (!userIsAdmin) {
            if (selectedItemPos == position) {
                holder.container.setBackgroundColor(ContextCompat.getColor(context, R.color.highlight_color));
            } else
                holder.container.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedItemPos = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public String getSelectedServiceName() {

        if (selectedItemPos != -1)
            return services.get(selectedItemPos).getName();
        else
            return "-1";
    }


    public static class ServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_service_name)
        TextView nameTv;
        @BindView(R.id.tv_service_info)
        TextView infoTv;
        @BindView(R.id.container_service_item)
        LinearLayout container;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
