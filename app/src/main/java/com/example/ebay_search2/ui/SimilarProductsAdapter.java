package com.example.ebay_search2.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ebay_search2.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimilarProductsAdapter extends RecyclerView.Adapter<SimilarProductsAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(SimilarProduct similarProduct);
    }

    static private List<SimilarProduct> defaultSimilarProductList;
    static private List<SimilarProduct> reversedDefaultSimilarProductList;
    static private List<SimilarProduct> similarProductList;
    static private String sortingCategory;
    static private String sortingDirection;
    static private OnProductClickListener onProductClickListener;
    private static String TAG = "SimilarProductsAdapter";

    public SimilarProductsAdapter(List<SimilarProduct> similarProductList, OnProductClickListener onProductClickListener) {
//        Log.d(TAG, "SimilarProductsAdapter: called");
        this.sortingCategory = "Default";
        this.sortingDirection = "Ascending";
        this.defaultSimilarProductList = new ArrayList<>(similarProductList);
        this.reversedDefaultSimilarProductList = new ArrayList<>(reverseArrayList(defaultSimilarProductList));
        this.similarProductList = new ArrayList<>(similarProductList);
        this.onProductClickListener = onProductClickListener;
    }

    private List<SimilarProduct> reverseArrayList(List<SimilarProduct> defaultSimilarProductList) {
        List<SimilarProduct> reversedList = new ArrayList<>();
        for (int i = defaultSimilarProductList.size() - 1; i >= 0; i--) {
            reversedList.add(defaultSimilarProductList.get(i));
        }
        return reversedList;
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

    public void setSortingCategory(String sortingCategory) {
        if (!sortingCategory.equals(this.sortingCategory)) {
            this.sortingCategory = sortingCategory;
            if (!sortingCategory.equals("Default")) {
                sortSimilarProductList();
            }
            else {
                similarProductList = sortingDirection.equals("Ascending") ?
                        new ArrayList<>(defaultSimilarProductList) :
                        new ArrayList<>(reversedDefaultSimilarProductList);
            }
            notifyDataSetChanged();
        }

    }

    public void setSortingDirection(String sortingDirection) {
        if (!sortingDirection.equals(this.sortingDirection)) {
            this.sortingDirection = sortingDirection;
            if (!sortingCategory.equals("Default")) {
                sortSimilarProductList();
            }
            else {
                similarProductList = sortingDirection.equals("Ascending") ?
                        new ArrayList<>(defaultSimilarProductList) :
                        new ArrayList<>(reversedDefaultSimilarProductList);
            }
            notifyDataSetChanged();
        }
    }

    private void sortSimilarProductList() {
        similarProductList.sort(new SimilarProductComparator());
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
    class SimilarProductComparator implements Comparator<SimilarProduct> {
        @Override
        public int compare(SimilarProduct o1, SimilarProduct o2) {
            switch (sortingCategory) {
                case "Name":
                    return sortingDirection.equals("Ascending") ?
                            o1.getTitle().compareTo(o2.getTitle()) :
                            o2.getTitle().compareTo(o1.getTitle());
                case "Price":
                    double o1Price = Double.parseDouble(o1.getPrice());
                    double o2Price = Double.parseDouble(o2.getPrice());
                    return sortingDirection.equals("Ascending") ?
                            Double.compare(o1Price, o2Price) :
                            Double.compare(o2Price, o1Price);
                case "Days":
                    int o1Days = Integer.parseInt(o1.getDaysLeft());
                    int o2Days = Integer.parseInt(o2.getDaysLeft());
                    return sortingDirection.equals("Ascending") ?
                            Integer.compare(o1Days, o2Days) :
                            Integer.compare(o2Days, o1Days);
            }
            return 0;
        }
    }
}


