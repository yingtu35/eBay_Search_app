package com.example.ebay_search2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebay_search2.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(SimilarProduct similarProduct);
    }

    static private List<SimilarProduct> similarProductList;
    static private OnProductClickListener onProductClickListener;
    private static String TAG = "SimilarProductsAdapter";

    public SimilarProductsAdapter(List<SimilarProduct> similarProductList, OnProductClickListener onProductClickListener) {
//        Log.d(TAG, "SimilarProductsAdapter: called");
        this.similarProductList = similarProductList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateVi ewHolder: called ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.similar_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: called");
        SimilarProduct similarProduct = similarProductList.get(position);
        // Bind data to views in the ViewHolder
        holder.bind(similarProduct);
    }

    @Override
    public int getItemCount() {
        return similarProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        // Declare views from the item layout
        private TextView productTile;
        private TextView productDaysLeft;
        private TextView productShipping;
        private TextView productPrice;
        private ImageView productImage;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the item layout
            productTile = itemView.findViewById(R.id.productTitle);
            productDaysLeft = itemView.findViewById(R.id.productDaysLeft);
            productShipping = itemView.findViewById(R.id.productShipping);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(similarProductList.get(getAdapterPosition()));
                }
            });
        }

        public void bind(SimilarProduct similarProduct) {
            // Bind data to views
            productTile.setText(similarProduct.getTitle());
            productDaysLeft.setText(similarProduct.getDaysLeft() + " Days Left");
            productShipping.setText(similarProduct.getShippingCost().equals("0.00") ? "Free" : similarProduct.getShippingCost());
            productPrice.setText(similarProduct.getPrice());
            Picasso.with(itemView
                            .getContext())
                    .load(similarProduct.getGalleryURL())
                    .fit()
                    .into(productImage);
        }
    }

//    TODO: Add sorting functionality
}
