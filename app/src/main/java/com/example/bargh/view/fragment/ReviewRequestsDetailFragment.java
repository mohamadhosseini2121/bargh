package com.example.bargh.view.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.bargh.CustomScrollView;
import com.example.bargh.R;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewRequestsDetailFragment extends Fragment {

    public static final String TAG = ReviewRequestsDetailFragment.class.getSimpleName();

    @BindView(R.id.toolbar_review_requests_detail)
    Toolbar toolbar;
    @BindView(R.id.til_status_review_requests_detail)
    TextInputLayout statusTil;
    @BindView(R.id.til_type_review_requests_detail)
    TextInputLayout typeTil;
    @BindView(R.id.til_user_review_requests_detail)
    TextInputLayout userTil;
    @BindView(R.id.til_time_review_requests_detail)
    TextInputLayout timeTil;
    @BindView(R.id.til_info_review_requests_detail)
    TextInputLayout infoTil;
    @BindView(R.id.fragment_container_position_review_requests_detail)
    FragmentContainerView positionFragmentContainer;
    @BindView(R.id.custom_scrollView_review_request_detail)
    CustomScrollView customScrollView;
    @BindView(R.id.btn_call_user_review_requests_detail)
    Button callBtn;
    @BindView(R.id.btn_change_state_review_requests_detail)
    Button changeStateBtn;

    private View view;

    private UserRepairRequest request = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_review_requests_detail, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    public void init() {

        initToolbar();
        getUserRepairRequestFromDatabase();
        setTextViewsValues();
        setRequestPositionMap();
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request != null){
                Uri number = Uri.parse("tel:" + request.getUser());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
                }
            }
        });
        changeStateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeStateDialog();
            }
        });
    }

    private void showChangeStateDialog () {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
        alertDialog.setTitle("تغییر وضعیت");
        
        UserRepairRequest repairRequest = new UserRepairRequest();
        List<String> states = new ArrayList<>();
        states.add(repairRequest.getStateStringByKey(UserRepairRequest.STATE_PENDING));
        states.add(repairRequest.getStateStringByKey(UserRepairRequest.STATE_DOING));
        states.add(repairRequest.getStateStringByKey(UserRepairRequest.STATE_DONE));
        states.add(repairRequest.getStateStringByKey(UserRepairRequest.STATE_CANCELLED));

        String[] items = states.toArray(new String[states.size()]);

        alertDialog.setSingleChoiceItems(items, request.getState(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        })
                .setNeutralButton("تغییر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeState(i);
                    }
                });
        AlertDialog alert = alertDialog.create();;
        alert.show();
    }

    private void changeState (int state) {

        Snackbar.make(view, "وضعیت انتخاب شده: " + state, Snackbar.LENGTH_SHORT).show();
    }

    private void setRequestPositionMap() {

        if (request != null) {
            MapFragment mapFragment = new MapFragment(new MapFragment.OnMapChanges() {
                @Override
                public void onMapReady(MapFragment mapFragment) {
                    LatLng position = new LatLng(request.getLat(), request.getLng());
                    mapFragment.placeMarker(position);
                    if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mapFragment.map.setMyLocationEnabled(false);
                    customScrollView.requestDisallowInterceptTouchEvent(true);
                }
            });
            this.getChildFragmentManager().beginTransaction()
                    .add(R.id.fragment_container_position_review_requests_detail,
                            mapFragment).commit();
        }
    }

    private void setTextViewsValues() {

        if (request != null) {
            Objects.requireNonNull(statusTil.getEditText()).setText(request.getStateString());
            Objects.requireNonNull(typeTil.getEditText()).setText(request.getType());
            Objects.requireNonNull(userTil.getEditText()).setText(request.getUser());
            Objects.requireNonNull(timeTil.getEditText()).setText(request.getDate());
            Objects.requireNonNull(infoTil.getEditText()).setText(request.getInfo());
        }
    }

    private void getUserRepairRequestFromDatabase() {
        AppDatabase database = AppDatabase.getInstance(requireContext());
        if (getArguments() != null) {
            ReviewRequestsDetailFragmentArgs args = ReviewRequestsDetailFragmentArgs.fromBundle(getArguments());
            request = database.userRepairRequestDao().getRequest(args.getUser(),args.getTimestamp());
        }
    }

    public void initToolbar () {
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)requireActivity()).getSupportActionBar();
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

}