package com.example.bargh.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bargh.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment {

    OnMapChanges onMapChanges;
    GoogleMap map;

    public MapFragment (OnMapChanges onMapChanges){

        this.onMapChanges = onMapChanges;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            onMapChanges.onMapReady(MapFragment.this);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }



    public void placeMarker (LatLng position, String markerTitle) {
        if (map != null) {
            map.addMarker(new MarkerOptions().position(position).title(markerTitle));
            map.moveCamera(CameraUpdateFactory.newLatLng(position));
            map.animateCamera(CameraUpdateFactory.zoomTo(14));
        }
    }

    public interface OnMapChanges {
        void onMapReady(MapFragment mapFragment);
    }
}