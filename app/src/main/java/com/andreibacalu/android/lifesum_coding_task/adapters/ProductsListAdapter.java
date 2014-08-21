package com.andreibacalu.android.lifesum_coding_task.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.andreibacalu.android.lifesum_coding_task.models.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrei on 21-08-14.
 */
public class ProductsListAdapter extends ArrayAdapter<Product> {

    private int itemLayoutId;
    private List<Product> products;
    private List<Integer> selectedProductsPositions;
    private LayoutInflater inflater;

    public ProductsListAdapter(Context context, int textViewResourceId, List<Product> objects) {
        super(context, textViewResourceId, objects);
        products = objects;
        selectedProductsPositions = new ArrayList<Integer>();
        itemLayoutId = textViewResourceId;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<Integer> getSelectedProductsPositions() {
        return selectedProductsPositions;
    }

    public void setSelectedProductsPositions(List<Integer> selectedProductsPositions) {
        this.selectedProductsPositions = selectedProductsPositions;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(itemLayoutId, null);
            viewHolder = new ViewHolder();
            viewHolder.text = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.text.setText(getItem(position).getTitle());
        convertView.setBackgroundColor(selectedProductsPositions.contains(position) ?
                getContext().getResources().getColor(android.R.color.holo_blue_light) :
                getContext().getResources().getColor(android.R.color.white));
        return convertView;
    }

    public Product[] getProductsListByPositions() {
        Product[] products = new Product[selectedProductsPositions.size()];
        int i = 0;
        for (Integer position : selectedProductsPositions) {
            products[i++] = getItem(position);
        }
        return products;
    }

    public void addSelectedPosition(int position) {
        selectedProductsPositions.add(position);
        notifyDataSetChanged();
    }

    public void removeSelectedPosition(int position) {
        selectedProductsPositions.remove((Integer)position);
        notifyDataSetChanged();
    }

    public int getSelectedProductsSize() {
        return selectedProductsPositions.size();
    }

    public void clearSelectedProducts() {
        selectedProductsPositions.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView text;
    }
}