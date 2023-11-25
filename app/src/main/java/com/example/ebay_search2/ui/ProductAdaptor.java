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

public class ProductAdaptor extends RecyclerView.Adapter<ProductAdaptor.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnButtonClickListener {
        void onButtonClick(Product product);
    }
    static private List<Product> productList;
    static private OnProductClickListener onProductClickListener;
    static private OnButtonClickListener onButtonClickListener;
    private static String TAG = "ProductAdaptor";

    public ProductAdaptor(List<Product> productList, OnProductClickListener onProductClickListener, OnButtonClickListener onButtonClickListener) {
//        Log.d(TAG, "ProductAdaptor: called");
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
        this.onButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d(TAG, "onCreateVi ewHolder: called ");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
//        Log.d(TAG, "onBindViewHolder: called");
        Product product = productList.get(position);
        // Bind data to views in the ViewHolder
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public void setProductWishListed(int position, boolean isWishListed) {
        productList.get(position).setIsWishListed(isWishListed);
        notifyItemChanged(position);
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {

        // Declare views from the item layout
        private TextView productTile;
        private TextView productZip;
        private TextView productShipping;
        private TextView productCondition;
        private TextView productPrice;
        private ImageView productImage;
        private Button addToWishlistButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the item layout
            productTile = itemView.findViewById(R.id.productTitle);
            productZip = itemView.findViewById(R.id.productZip);
            productShipping = itemView.findViewById(R.id.productShipping);
            productCondition = itemView.findViewById(R.id.productCondition);
            productPrice = itemView.findViewById(R.id.productPrice);
            productImage = itemView.findViewById(R.id.productImage);
            addToWishlistButton = itemView.findViewById(R.id.addToWishlistButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onProductClickListener.onProductClick(productList.get(getAdapterPosition()));
                }
            });

            addToWishlistButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onButtonClickListener != null) {
                        Product clickedProduct = productList.get(position);
                        onButtonClickListener.onButtonClick(clickedProduct);
                    }
                }
            });


        }

        public void bind(Product product) {
            // Bind data to views
            productTile.setText(product.getTitle());
            productZip.setText("Zip " + product.getPostalCode());
            productShipping.setText(product.getShippingCost().equals("0.0") ? "Free" : product.getShippingCost());
            productCondition.setText(product.getCondition());
            productPrice.setText(product.getCurrentPrice());
            Picasso.with(itemView
                    .getContext())
                    .load(product.getGalleryURL())
                    .fit()
                    .into(productImage);
            if (product.getIsWishListed()) {
                addToWishlistButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.cart_remove, 0);
            } else {
                addToWishlistButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.cart_plus, 0);
            }
        }
    }
}



