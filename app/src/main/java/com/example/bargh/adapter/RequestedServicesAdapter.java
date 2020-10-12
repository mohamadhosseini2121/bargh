package com.example.bargh.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bargh.R;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RequestedServicesAdapter extends RecyclerView.Adapter<RequestedServicesAdapter.ServicesViewHolder> {

    private static final String TAG = "RequestedServicesAd: ";
    List<UserRepairRequest> userRepairRequests;
    Context context;
    OnRequestedServicesListener onRequestedServicesListener;
    boolean isActionMode = false;


    private SparseBooleanArray selectedItems;


    public RequestedServicesAdapter(Context context,
                                    List<UserRepairRequest> userRepairRequests,
                                    OnRequestedServicesListener onRequestedServicesListener) {

        this.userRepairRequests = userRepairRequests;
        this.context = context;
        this.onRequestedServicesListener = onRequestedServicesListener;
        selectedItems = new SparseBooleanArray();

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

        if (isActionMode) {
            if (isSelected(position))
                holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.highlight_color));
            else
                holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isActionMode) {
                    UserRepairRequest uRR = userRepairRequests.get(position);
                    if (uRR.getState() == UserRepairRequest.STATE_DOING) {
                        Snackbar.make(view, "درخواست های در حال انجام را نمیتوان حذف کرد", Snackbar.LENGTH_SHORT).show();
                    } else {
                        toggleSelection(position);
                    }
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (!isActionMode) {
                    isActionMode = true;
                    onRequestedServicesListener.onActionModeChange(true);
                    UserRepairRequest uRR = userRepairRequests.get(position);
                    if (uRR.getState() == UserRepairRequest.STATE_DOING) {
                        Snackbar.make(view, "درخواست های در حال انجام را نمیتوان حذف کرد", Snackbar.LENGTH_SHORT).show();
                    } else {
                        selectedItems.put(position, true);
                        holder.relativeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.highlight_color));
                        notifyItemChanged(position);
                    }
                }
                return true;
            }
        });

    }

    public void setActionMode(boolean state) {
        isActionMode = state;
    }


    @Override
    public int getItemCount() {
        if (userRepairRequests != null)
            return userRepairRequests.size();
        else
            return 0;
    }

    /**
     * Indicates if the item at position position is selected
     *
     * @param position Position of the item to check
     * @return true if the item is selected, false otherwise
     */
    public boolean isSelected(int position) {
        return getSelectedItems().contains(position);
    }

    /**
     * Toggle the selection status of the item at a given position
     *
     * @param position Position of the item to toggle the selection status for
     */
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false))
            selectedItems.delete(position);
        else
            selectedItems.put(position, true);

        notifyItemChanged(position);
    }

    public void toggleSelectAll() {
        SparseBooleanArray temp = new SparseBooleanArray();
        int allItems = 0;
        for (int i = 0; i < userRepairRequests.size(); i++) {
            UserRepairRequest userRepairRequest = userRepairRequests.get(i);
            if (userRepairRequest.getState() != UserRepairRequest.STATE_DOING){
                temp.put(i, true);
                allItems++;}
        }
        if (selectedItems.size() == allItems)
            clearSelection();
        else{
            selectedItems.clear();
            selectedItems = null;
            selectedItems = temp;}
        notifyItemRangeChanged(0, userRepairRequests.size());
    }

    /**
     * Clear the selection status for all items
     */
    public void clearSelection() {
        List<Integer> selection = getSelectedItems();
        selectedItems.clear();
        for (Integer i : selection) {
            notifyItemChanged((int)i);
        }
    }

    /**
     * Count the selected items
     *
     * @return Selected items count
     */
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    /**
     * Indicates the list of selected items
     *
     * @return List of selected items ids
     */
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); ++i) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }


    public void removeItem(int position) {
        userRepairRequests.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItems(List<Integer> positions) {
        for (Integer pos : positions) {
            removeItem((int) pos);
        }
    }


    public static class ServicesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.container_requested_service_item)
        RelativeLayout relativeLayout;
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

    public interface OnRequestedServicesListener {

        void onActionModeChange (boolean isActionMode);

    }

}
