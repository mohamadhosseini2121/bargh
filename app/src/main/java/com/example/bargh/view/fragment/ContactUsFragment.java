package com.example.bargh.view.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bargh.CustomScrollView;
import com.example.bargh.R;
import com.google.android.gms.maps.model.LatLng;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactUsFragment extends Fragment {

    @BindView(R.id.toolbar_contact_us)
    Toolbar toolbar;
    @BindView(R.id.custom_scrollView_contact_us)
    CustomScrollView customScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        ButterKnife.bind(this,view);
        initToolbar();
        initOurLocationMap();
        return view;
    }

    private void initToolbar() {
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }

    private void initOurLocationMap() {
        MapFragment mapFragment = new MapFragment(new MapFragment.OnMapChanges() {
            @Override
            public void onMapReady(MapFragment mapFragment) {
                LatLng position = new LatLng(30.412386, 55.989745);
                mapFragment.placeMarker(position, "اورژانس برق رفسنجان");
                if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mapFragment.map.setMyLocationEnabled(false);
                customScrollView.requestDisallowInterceptTouchEvent(true);
            }
        });
        this.getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_container_our_location_contact_us,
                        mapFragment).commit();
    }
}