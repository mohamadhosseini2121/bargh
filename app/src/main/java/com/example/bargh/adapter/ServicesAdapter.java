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

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> services;

    public ServicesAdapter(Context context, List<Service> services) {

        this.context = context;
        this.services = services;

    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_service,parent,false);

        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = services.get(position);
        holder.nameTv.setText(service.getName());
        holder.infoTv.setText(service.getInfo());

    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    public class ServiceViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_service_name)
        TextView nameTv;
        @BindView(R.id.tv_service_info)
        TextView infoTv;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
