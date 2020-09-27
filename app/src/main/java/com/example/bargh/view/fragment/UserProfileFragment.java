package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bargh.R;
import com.google.android.material.textfield.TextInputEditText;

public class UserProfileFragment extends Fragment {

    @BindView(R.id.toolbar_user_profile)
    Toolbar toolbar;
    @BindView(R.id.tv_name_user_profile_frag)
    TextView FullNameTv;
    @BindView(R.id.tv_mobile_number_user_profile_frag)
    TextView MobileNumberTv;
    @BindView(R.id.tiet_first_name_user_profile)
    TextInputEditText firstNameEt;
    @BindView(R.id.tiet_last_name_user_profile)
    TextInputEditText lastNameEt;
    @BindView(R.id.tiet_email_user_profile)
    TextInputEditText emailEt;
    @BindView(R.id.tiet_mobile_number_user_profile)
    TextInputEditText mobileNumberEt;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        ButterKnife.bind(this,view);
        setupToolbar();

        return view;
    }



    public void setupToolbar () {

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.show();
        }

    }




}