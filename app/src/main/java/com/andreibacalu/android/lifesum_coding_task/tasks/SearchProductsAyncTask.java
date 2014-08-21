package com.andreibacalu.android.lifesum_coding_task.tasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.andreibacalu.android.lifesum_coding_task.R;
import com.andreibacalu.android.lifesum_coding_task.fragments.ProductsFragment;
import com.andreibacalu.android.lifesum_coding_task.models.Product;
import com.orm.query.Select;

import java.util.List;

/**
 * Created by andrei.bacalu on 8/21/2014.
 */
public class SearchProductsAyncTask extends AsyncTask<String, Void, List<Product>> {
    private ProductsFragment productsFragment;
    private ProgressDialog dialog;

    public SearchProductsAyncTask(ProductsFragment productsFragment) {
        this.productsFragment = productsFragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (productsFragment.isVisible()) {
            dialog = ProgressDialog.show(productsFragment.getActivity(), "", productsFragment.getString(R.string.loading), true);
        }
    }

    @Override
    protected List<Product> doInBackground(String... params) {
        return params == null || params.length == 0 ?
                Select.from(Product.class).orderBy("title").list() :
                Select.from(Product.class).where("title like '%" + params[0] + "%'").orderBy("title").list();
    }

    @Override
    protected void onPostExecute(List<Product> result) {
        super.onPostExecute(result);
        productsFragment.updateProducts(result);
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}