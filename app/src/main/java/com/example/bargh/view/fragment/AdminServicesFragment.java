package com.example.bargh.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.ServicesAdapter;
import com.example.bargh.datamodel.Service;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminServicesFragment extends Fragment {

    @BindView(R.id.recycler_view_admin_services)
    RecyclerView recyclerView;
    @BindView(R.id.tv_empty_data_admin_services)
    TextView emptyTv;

    private List<Service> services = new ArrayList<>();
    private ServicesAdapter sAdapter;
    private ApiService apiService;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_services, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    public void init() {
        apiService = ApiService.getInstance(requireContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false));
        Collections.reverse(services);
        sAdapter = new ServicesAdapter(requireContext(), services, true);
        recyclerView.setAdapter(sAdapter);
        HomeFragment homeFragment = ((HomeFragment) AdminServicesFragment.this.getParentFragment());
        if (homeFragment != null) {
            fab = homeFragment.fab;
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addServiceFragment);
            }
        });
    }

    @Override
    public void onResume() {
        getServicesFromServer();
        isRecyclerViewEmpty();
        super.onResume();
    }

    public void getServicesFromServer () {

        apiService.getAllServices(new ApiService.OnGettingAllServices() {
            @Override
            public void onReceived(List<Service> allServices) {
                services.clear();
                services.addAll(allServices);
                Collections.reverse(services);
                sAdapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(services.size() - 1);
            }
        });
    }

    /**
     * if recycler view is empty, this method hides recycler view and
     * shows empty text view (text view with a message that shows recycler view is empty)
     */
    private void isRecyclerViewEmpty() {

        if (services != null) {
            if (services.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyTv.setVisibility(View.VISIBLE);

            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyTv.setVisibility(View.GONE);
            }
        }
    }
}