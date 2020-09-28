package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.RequestedServicesAdapter;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    @BindView(R.id.recycler_view_services)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty_data)
    TextView emptyTv;

    private List<UserRepairRequest> userRepairRequests = new ArrayList<>();
    private RequestedServicesAdapter requestedServicesAdapter;
    private ApiService apiService;
    private View view;
    private RequestedServicesAdapter.OnRequestedServicesListener onRequestedServicesListener;

    public ServicesFragment (RequestedServicesAdapter.OnRequestedServicesListener onRequestedServicesListener) {

        this.onRequestedServicesListener = onRequestedServicesListener;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        apiService = ApiService.getInstance(requireContext());
        requestedServicesAdapter = new RequestedServicesAdapter(requireContext(), userRepairRequests,onRequestedServicesListener);
        recyclerView.setAdapter(requestedServicesAdapter);

        if (userRepairRequests.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        apiService.getUserServices("09024331673", new ApiService.OnGettingUserServices() {
            @Override
            public void onReceived(List<UserRepairRequest> rsp) {
                if (rsp != null && !rsp.isEmpty()) {
                    userRepairRequests.clear();
                    userRepairRequests.addAll(rsp);
                    requestedServicesAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyTv.setVisibility(View.GONE);
                }else {
                    recyclerView.setVisibility(View.GONE);
                    emptyTv.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void deleteRequestedService (UserRepairRequest userRepairRequest) {

        if (userRepairRequest.getState() == 0)
            apiService.deleteUserService(userRepairRequest, new ApiService.onDeletingRequestedService() {
                @Override
                public void onDelete(int rsp) {
                    if (rsp == 1)
                        Snackbar.make(view , "حذف با موفقیت انجام شد", Snackbar.LENGTH_SHORT).show();
                    else
                        Snackbar.make(view , "حذف با موفقیت انجام نشد", Snackbar.LENGTH_SHORT).show();
                }
            });

        int position = userRepairRequests.indexOf(userRepairRequest);
        userRepairRequests.remove(userRepairRequest);
        requestedServicesAdapter.notifyItemRemoved(position);
        if (userRepairRequests.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            emptyTv.setVisibility(View.VISIBLE);
        }else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyTv.setVisibility(View.GONE);
        }

    }

}
