package com.example.bargh.view.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bargh.ApiService;
import com.example.bargh.R;
import com.example.bargh.adapter.ProductsAdapter;
import com.example.bargh.db.entity.Product;

import java.util.List;


public class ProductsFragment extends Fragment {
    
    private final String TAG = "ProductFragment: ";

    private ApiService apiService;

    @BindView(R.id.recycler_view_products)
    RecyclerView recyclerView;


    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        apiService = ApiService.getInstance(requireContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        apiService.getProducts(new ApiService.OnProductsReceived() {
            @Override
            public void onReceived(List<Product> products) {
                ProductsAdapter adapter = new ProductsAdapter(ProductsFragment.this.requireContext(), products);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductsFragment.this.requireContext(), RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }
        });
    }

}
