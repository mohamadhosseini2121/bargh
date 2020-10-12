package com.example.bargh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.bargh.datamodel.Product;
import com.example.bargh.R;
import com.example.bargh.view.fragment.HomeFragmentDirections;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private Context context;
    private List<Product> products;
    private View view;

    public ProductsAdapter(Context context, List<Product> products) {

        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.item_view_product, parent, false);
        return new ProductsViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        Product product = products.get(position);
        holder.nameTv.setText(product.getName());
        holder.priceTv.setText(Integer.toString(product.getPrice()).concat(" تومان "));
        holder.infoTv.setText(product.getInfo());

        Glide.with(context).load(product.getProductImageUrl().replace("localhost", "192.168.1.11"))
                .placeholder(R.drawable.ic_placeholder)
                .fitCenter()
                .centerInside()
                .into(holder.productImg);

        holder.itemView.setOnClickListener(view -> {
            HomeFragmentDirections.ActionHomeFragmentToProductsDetailFragment action = HomeFragmentDirections.actionHomeFragmentToProductsDetailFragment(product.getName(),
                    product.getPrice(), product.getInfo(), product.getProductImageUrl());
            Navigation.findNavController(view).navigate(action);
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ProductsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name_product_detail)
        TextView nameTv;
        @BindView(R.id.tv_price_products)
        TextView priceTv;
        @BindView(R.id.tv_info_product)
        TextView infoTv;
        @BindView(R.id.img_product)
        ImageView productImg;

        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
