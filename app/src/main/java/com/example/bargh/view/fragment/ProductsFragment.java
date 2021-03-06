package com.example.bargh.view.fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.bargh.datamodel.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class ProductsFragment extends Fragment {
    
    private final String TAG = "ProductFragment: ";

    private ApiService apiService;
    private FloatingActionButton fab;

    @BindView(R.id.recycler_view_products)
    RecyclerView recyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products, container, false);
        ButterKnife.bind(this, view);
        apiService = ApiService.getInstance(requireContext());
        HomeFragment homeFragment = ((HomeFragment) ProductsFragment.this.getParentFragment());
        if (homeFragment != null) {
            fab = homeFragment.fab;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addProductFragment);
            }
        });

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
