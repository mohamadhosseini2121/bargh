package com.example.bargh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bargh.R;
import com.example.bargh.db.entity.UserRepairRequest;
import com.example.bargh.view.fragment.HomeFragmentDirections;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewRequestsAdapter extends RecyclerView.Adapter<ReviewRequestsAdapter.ReviewRequestsViewHolder> {

    private Context context;
    private List<UserRepairRequest> requests;


    public ReviewRequestsAdapter(Context context,List<UserRepairRequest> requests ) {
        this.context = context;
        this.requests = requests;
    }

    @NonNull
    @Override
    public ReviewRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_review_requests,parent,false);
        return new ReviewRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewRequestsViewHolder holder, int position) {

        UserRepairRequest request = requests.get(position);
        holder.statusTv.setText(request.getStateString());
        holder.typeTv.setText(request.getType());
        holder.infoTv.setText(request.getInfo());
        holder.timeTv.setText(request.getDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view)
            .navigate(HomeFragmentDirections.actionHomeFragmentToReviewRequestsDetailFragment(request.getUser(),request.getTimestamp()));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (requests != null)
            return requests.size();
        else
            return 0;
    }

    public static class ReviewRequestsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.container_requested_review_requests_item)
        RelativeLayout relativeLayout;
        @BindView(R.id.tv_status_review_requests_item)
        TextView statusTv;
        @BindView(R.id.tv_type_review_requests_item)
        TextView typeTv;
        @BindView(R.id.tv_info_review_requests_item)
        TextView infoTv;
        @BindView(R.id.tv_time_review_requests_item)
        TextView timeTv;

        public ReviewRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
