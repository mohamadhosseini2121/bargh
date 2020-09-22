package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.ServicesAdapter;
import com.example.bargh.datamodel.Service;
import com.google.android.material.appbar.AppBarLayout;
import java.util.ArrayList;
import java.util.List;


public class ServiceRequestFragment extends Fragment {

    private final String TAG = "ServiceRequestFragment:";
    private View view;
    private ApiService apiService;
    private ServicesAdapter servicesAdapter;

    @BindView(R.id.toolbar_service_request_frag)
    Toolbar toolbar;
    @BindView(R.id.app_bar_service_request_frag)
    AppBarLayout appBarLayout;
    @BindView(R.id.recycler_view_services_request_frag)
    RecyclerView recyclerView;

    private List<Service> services = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_service_request, container, false);
        ButterKnife.bind(this,view);

        setupToolbar();
        apiService = ApiService.getInstance(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        servicesAdapter = new ServicesAdapter(requireContext(), services);
        recyclerView.setAdapter(servicesAdapter);

        return view;
    }

    @Override
    public void onResume() {

        apiService.getAllServices(new ApiService.onGettingAllServices() {
            @Override
            public void onReceived(List<Service> allServices) {
                services.clear();
                services.addAll(allServices);
                Log.d(TAG, "onReceived: allServices(0) name: " + allServices.get(0).getName() );
                Log.d(TAG, "onReceived: services(0) name: " + allServices.get(0).getName() );
                servicesAdapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);

            }
        });
        super.onResume();
    }

    private void setupToolbar() {
        toolbar.setTitle("انتخاب درخواست");
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.title_texts_color));

    }
}