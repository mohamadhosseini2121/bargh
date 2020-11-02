package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bargh.R;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddProductFragment extends Fragment {

    @BindView(R.id.toolbar_add_product)
    Toolbar toolbar;
    @BindView(R.id.tv_empty_view_pager_add_product)
    TextView emptyTv;
    @BindView(R.id.tiet_name_add_product)
    TextInputEditText nameEt;
    @BindView(R.id.tiet_price_add_product)
    TextInputEditText priceEt;
    @BindView(R.id.tiet_info_add_product)
    TextInputEditText infoEt;
    @BindView(R.id.btn_submit_add_product)
    Button addProductBtn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        ButterKnife.bind(this,view);
        initToolbar();
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

}