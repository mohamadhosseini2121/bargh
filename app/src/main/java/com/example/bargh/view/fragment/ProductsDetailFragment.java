package com.example.bargh.view.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bargh.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.pouriahemati.phjustifiedtextview.PHJustifiedTextView;

import java.util.Objects;


public class ProductsDetailFragment extends Fragment {

    @BindView(R.id.app_bar_product_detail_frag)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar_product_detail_frag)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar_product_detail_frag)
    Toolbar toolbar;
    @BindView(R.id.tv_name_product_detail)
    TextView nameTv;
    @BindView(R.id.tv_info_product_detail)
    PHJustifiedTextView infoTv;
    @BindView(R.id.tv_price_product_detail)
    TextView priceTv;
    @BindView(R.id.img_product_detail)
    ImageView productImg;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_detail, container, false);
        ButterKnife.bind(this,view);
        initViews();
        return view;
    }


    public void initToolbar () {
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity)requireActivity()).getSupportActionBar() != null){
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.app_titles_color));
    }

    public void initViews () {
        initToolbar();
        infoTv.setLineSpacing(15);
        if (getArguments() != null) {
            ProductsDetailFragmentArgs args = ProductsDetailFragmentArgs.fromBundle(getArguments());
                    nameTv.setText(args.getProductName());
                    String price = args.getProductPrice() + " تومان " ;
                    priceTv.setText(price);
                    infoTv.setText(args.getProductInfo());
            Glide.with(requireContext()).load(args.getProductImageUrl().replace("localhost", "192.168.100.129"))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(productImg);

        }
    }

}