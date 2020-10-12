package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.RequestedServicesAdapter;
import com.example.bargh.adapter.ServicesAdapter;
import com.example.bargh.datamodel.Service;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class ServiceRequestFragment extends Fragment{

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
    @BindView(R.id.btn_next_step)
    Button nextStepBtn;
    @BindView(R.id.et_info_service_request_frag)
    EditText infoEt;

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

        nextStepBtn.setOnClickListener(view -> {
            if (!servicesAdapter.getSelectedServiceName().equals("-1")) {
                ServiceRequestFragmentDirections.ActionServiceRequestFragmentToLocationFragment action =
                        ServiceRequestFragmentDirections.actionServiceRequestFragmentToLocationFragment(
                                servicesAdapter.getSelectedServiceName(),infoEt.getText().toString());
                Navigation.findNavController(view).navigate(action);
            }else{
                Snackbar.make(view, "هیچکدام از خدمات انخاب نشده است!", Snackbar.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onResume() {

        apiService.getAllServices(allServices -> {
            services.clear();
            services.addAll(allServices);
            servicesAdapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);

        });
        super.onResume();
    }

    private void setupToolbar() {
        toolbar.setTitle("انتخاب درخواست");
        toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.app_titles_color));

    }


}