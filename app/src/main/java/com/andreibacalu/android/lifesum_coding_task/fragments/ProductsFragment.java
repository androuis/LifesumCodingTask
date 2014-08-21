package com.andreibacalu.android.lifesum_coding_task.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.andreibacalu.android.lifesum_coding_task.R;
import com.andreibacalu.android.lifesum_coding_task.adapters.ProductsListAdapter;
import com.andreibacalu.android.lifesum_coding_task.listeners.DbActionListener;
import com.andreibacalu.android.lifesum_coding_task.models.Product;
import com.andreibacalu.android.lifesum_coding_task.tasks.SearchProductsAyncTask;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrei on 21-08-14.
 */
public class ProductsFragment extends ListFragment {

    public static String ARG_FRAGMENT_TYPE = "fragment_type";
    public static int FRAGMENT_TYPE_ONLINE = 0;
    public static int FRAGMENT_TYPE_OFFLINE = 1;

    private List<Product> products;
    private int fragmentType;
    private String searchTerm;

    private DbActionListener dbActionListener;

    private ProductsListAdapter productsListAdapter;

    private ActionMode actionMode;

    private AbsListView.MultiChoiceModeListener multiChoiceActionModeCallback = new AbsListView.MultiChoiceModeListener() {

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if (checked) {
                productsListAdapter.addSelectedPosition(position);
            } else {
                productsListAdapter.removeSelectedPosition(position);
            }
            mode.setTitle(getString(R.string.title_cab, productsListAdapter.getSelectedProductsSize()));
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu, menu);
            if (fragmentType == FRAGMENT_TYPE_ONLINE) {
                menu.findItem(R.id.action_delete).setVisible(false);
            } else {
                menu.findItem(R.id.action_save).setVisible(false);
            }
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_save:
                    if (dbActionListener != null) {
                        dbActionListener.onSaveSelected(productsListAdapter.getProductsListByPositions());
                    }
                    mode.finish(); // action picked, so close the CAB
                    return true;
                case R.id.action_delete:
                    if (dbActionListener != null) {
                        dbActionListener.onDeleteSelected(productsListAdapter.getProductsListByPositions());
                    }
                    mode.finish(); // action picked, so close the CAB
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            productsListAdapter.clearSelectedProducts();
            actionMode = null;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentType = getArguments().getInt(ARG_FRAGMENT_TYPE);
        products = new ArrayList<Product>();
        if (fragmentType == FRAGMENT_TYPE_OFFLINE) {
            new SearchProductsAyncTask(this).execute();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setMultiChoiceModeListener(multiChoiceActionModeCallback);
        // TODO: use another layout, to look prettier!
        productsListAdapter = new ProductsListAdapter(getActivity(), android.R.layout.simple_list_item_1, products);
        setListAdapter(productsListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(productsListAdapter.getItem(position).getHumanReadableString())
                .setTitle(R.string.product_details);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public DbActionListener getDbActionListener() {
        return dbActionListener;
    }

    public void setDbActionListener(DbActionListener dbActionListener) {
        this.dbActionListener = dbActionListener;
    }

    public List<Product> getProducts() {
        return productsListAdapter.getProducts();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public int getFragmentType() {
        return fragmentType;
    }

    public void setFragmentType(int fragmentType) {
        this.fragmentType = fragmentType;
    }

    public void updateProducts(List<Product> products) {
        productsListAdapter.setProducts(products);
        productsListAdapter.notifyDataSetChanged();
    }

    public void destroyCAB() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }
}