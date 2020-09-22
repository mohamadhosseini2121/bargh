package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.bargh.datamodel.RequestedService;

import java.util.ArrayList;
import java.util.List;

public class ServicesFragment extends Fragment {

    @BindView(R.id.recycler_view_services)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty_data)
    TextView emptyTv;

    private List<RequestedService> requestedServices = new ArrayList<>();
    private RequestedServicesAdapter requestedServicesAdapter;
    private ApiService apiService;
    private View view;


    public ServicesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_services, container, false);
        ButterKnife.bind(this,view);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        apiService = ApiService.getInstance(requireContext());
        requestedServicesAdapter = new RequestedServicesAdapter(requireContext(), requestedServices);
        recyclerView.setAdapter(requestedServicesAdapter);

        if (requestedServices.isEmpty()){
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
            public void onReceived(List<RequestedService> rsp) {
                if (rsp != null && !rsp.isEmpty()) {
                    requestedServices.clear();
                    requestedServices.addAll(rsp);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
