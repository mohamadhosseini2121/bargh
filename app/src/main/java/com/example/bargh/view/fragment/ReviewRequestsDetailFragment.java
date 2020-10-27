package com.example.bargh.view.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.bargh.ApiService;
import com.example.bargh.CustomScrollView;
import com.example.bargh.R;
import com.example.bargh.db.AppDatabase;
import com.example.bargh.db.entity.UserRepairRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
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

    private View fragView;
    private int oldState = 0;
    private int newState = 0;

    private UserRepairRequest request = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_review_requests_detail, container, false);
        ButterKnife.bind(this, fragView);
        init();
        return fragView;
    }

    public void init() {

        initToolbar();
        getUserRepairRequestFromDatabase();
        setTextViewsValues();
        setRequestPositionMap();
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (request != null) {
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

    private void showChangeStateDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext(),R.style.AlertDialogTheme);
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
                newState = which;
            }
        });
        alertDialog.setNegativeButton("لغو", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).setPositiveButton("تغییر", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        changeState(newState);
                        dialogInterface.dismiss();
                    }
        });
        AlertDialog alert = alertDialog.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Typeface typeface = ResourcesCompat.getFont(requireContext(), R.font.shabnam_bold);
                Button negativeButton = ((AlertDialog)alert).getButton(DialogInterface.BUTTON_NEGATIVE);
                negativeButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary));
                negativeButton.setTypeface(typeface);

                Button positiveButton = ((AlertDialog)alert).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary));
                positiveButton.setTypeface(typeface);

                Resources resources = alert.getContext().getResources();
                int color = resources.getColor(R.color.colorPrimary); // your color here

                int alertTitleId = resources.getIdentifier("alertTitle", "id", "android");
                TextView alertTitle = (TextView) alert.getWindow().getDecorView().findViewById(alertTitleId);
                alertTitle.setTextColor(color); // change title text color

                int titleDividerId = resources.getIdentifier("titleDivider", "id", "android");
                View titleDivider = alert.getWindow().getDecorView().findViewById(titleDividerId);
                titleDivider.setBackgroundColor(color); // change divider color
            }
        });
        alert.show();
    }

    private void changeState(int state) {
        ApiService apiService = ApiService.getInstance(requireContext());
        oldState = request.getState();
        request.setState(state);
        apiService.changeUserRepairRequestState(request, new ApiService.OnChangingUserRepairRequest() {
            @Override
            public void onStateChangeResult(HashMap<String, String> result) {
                String code = result.get("code");
                String content = result.get("content");
                if (code != null && content != null) {
                    if (code.equals("0"))
                        request.setState(oldState);

                    setTextViewsValues();
                    Snackbar.make(fragView, content, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
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
            request = database.userRepairRequestDao().getRequest(args.getUser(), args.getTimestamp());
        }
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

}