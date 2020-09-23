package com.example.bargh.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bargh.datamodel.Product;
import com.example.bargh.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    private Context context;
    private List<Product> products;

    public ProductsAdapter(Context context, List<Product> products) {

        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_view_product, parent, false);
        return new ProductsViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

        Product product = products.get(position);
        holder.nameTv.setText(product.getName());
        holder.priceTv.setText(Integer.toString(product.getPrice()).concat(" تومان "));
        holder.infoTv.setText(product.getInfo());
        Picasso.get().load(product.getProductImageUrl().replace("localhost", "192.168.1.10"))
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.productImg);

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
