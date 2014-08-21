package com.andreibacalu.android.lifesum_coding_task.activities;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.andreibacalu.android.lifesum_coding_task.R;
import com.andreibacalu.android.lifesum_coding_task.fragments.ProductsFragment;
import com.andreibacalu.android.lifesum_coding_task.listeners.DbActionListener;
import com.andreibacalu.android.lifesum_coding_task.models.Product;
import com.andreibacalu.android.lifesum_coding_task.tasks.SearchProductsAyncTask;
import com.orm.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements Handler.Callback, DbActionListener {

    private static final int WHAT_REQUEST_COMPLETED = 1;

    private MyThread myThread;
    private Handler handler;

    private ViewPager productsViewPager;
    private ProductsPagerAdapter productsPagerAdapter;
    private SearchManager searchManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler(this);
        searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        productsPagerAdapter = new ProductsPagerAdapter(getSupportFragmentManager());
        productsViewPager = (ViewPager) findViewById(R.id.view_pager);
        productsViewPager.setAdapter(productsPagerAdapter);

        configureActionBar();

        productsViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                int previousPosition = position == 0 ? position + 1 : position - 1;
                productsPagerAdapter.getItem(previousPosition).destroyCAB();
                // when swiping between pages, select the corresponding tab
                getActionBar().setSelectedNavigationItem(position);
            }
        });
    }

    private void configureActionBar() {
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                productsPagerAdapter.getItem(productsViewPager.getCurrentItem()).destroyCAB();
                // when swiping between pages, select the corresponding tab
                productsViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };
        for (int i = 0; i < productsPagerAdapter.getCount(); i++) {
            ab.addTab(ab.newTab().setText(productsPagerAdapter.getPageTitle(i)).setTabListener(tabListener));
        }
    }

    @Override
    protected void onDestroy() {
        if (myThread != null && myThread.isAlive()) {
            myThread.interrupt();
        }
        handler = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setIconified(false);
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            public boolean onQueryTextChange(String newText) {
                // this is your adapter that will be filtered
                // TODO: if time, implement smth!!!
                return true;
            }

            public boolean onQueryTextSubmit(String query) {
                if (productsViewPager.getCurrentItem() == ProductsFragment.FRAGMENT_TYPE_ONLINE) {
                    if (!TextUtils.isEmpty(query)) {
                        if (myThread != null && myThread.isAlive()) {
                            myThread.interrupt();
                        }
                        myThread = new MyThread(handler, query);
                        myThread.start();
                    }
                } else {
                    new SearchProductsAyncTask(productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_OFFLINE)).execute(query);
                }
                return true;
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // when closing the search view for offline, display all content
                new SearchProductsAyncTask(productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_OFFLINE)).execute();
                return true;
            }
        });
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem;
        if (productsViewPager.getCurrentItem() == ProductsFragment.FRAGMENT_TYPE_ONLINE) {
            menu.findItem(R.id.action_delete_all).setVisible(false);
            menuItem = menu.findItem(R.id.action_save_all);
        } else {
            menu.findItem(R.id.action_save_all).setVisible(false);
            menuItem = menu.findItem(R.id.action_delete_all);
        }
        menuItem.setVisible(true);
        if (productsPagerAdapter.getItem(productsViewPager.getCurrentItem()).getListAdapter().getCount() == 0) {
            menuItem.setEnabled(false);
        } else {
            menuItem.setEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save_all:
                List<Product> products = productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_ONLINE).getProducts();
                new SaveProductsAyncTask().execute(products.toArray(new Product[products.size()]));
                return true;
            case R.id.action_delete_all:
                new DeleteProductsAyncTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case WHAT_REQUEST_COMPLETED:
                Fragment currentFragment = productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_ONLINE);
                if (currentFragment instanceof ProductsFragment) {
                    ((ProductsFragment) currentFragment).updateProducts((List<Product>) msg.obj);
                }
                break;
        }
        return false;
    }

    @Override
    public void onSaveSelected(Product[] products) {
        new SaveProductsAyncTask().execute(products);
    }

    @Override
    public void onDeleteSelected(Product[] products) {
        new DeleteProductsAyncTask().execute(products);
    }

    private class ProductsPagerAdapter extends FragmentStatePagerAdapter {

        private ProductsFragment[] productsFragments;

        public ProductsPagerAdapter(FragmentManager fm) {
            super(fm);
            productsFragments = new ProductsFragment[getCount()];
        }

        @Override
        public ProductsFragment getItem(int position) {
            if (productsFragments[position] == null) {
                ProductsFragment productsFragment = new ProductsFragment();
                Bundle args = new Bundle();
                args.putInt(ProductsFragment.ARG_FRAGMENT_TYPE, position == 0 ? ProductsFragment.FRAGMENT_TYPE_ONLINE : ProductsFragment.FRAGMENT_TYPE_OFFLINE);
                productsFragment.setArguments(args);
                productsFragments[position] = productsFragment;
            }
            if (productsFragments[position].getDbActionListener() == null) {
                productsFragments[position].setDbActionListener(MainActivity.this);
            }
            return productsFragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? getString(R.string.tabs_title_online) : getString(R.string.tabs_title_offline);
        }
    }

    private class SaveProductsAyncTask extends AsyncTask<Product, Void, List<Product>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", getString(R.string.loading), true);
        }

        @Override
        protected List<Product> doInBackground(Product... params) {
            List<Product> allProducts = Product.listAll(Product.class);
            for (Product product : params) {
                if (!allProducts.contains(product)) {
                    product.save();
                }
            }
            return Select.from(Product.class).orderBy("title").list();
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            super.onPostExecute(result);
            productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_OFFLINE).updateProducts(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private class DeleteProductsAyncTask extends AsyncTask<Product, Void, List<Product>> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "", getString(R.string.loading), true);
        }

        @Override
        protected List<Product> doInBackground(Product... params) {
            if (params == null || params.length == 0) {
                Product.deleteAll(Product.class);
                return new ArrayList<Product>();
            } else {
                for (Product product: params) {
                    product.delete();
                }
                return Select.from(Product.class).orderBy("title").list();
            }
        }

        @Override
        protected void onPostExecute(List<Product> result) {
            super.onPostExecute(result);
            productsPagerAdapter.getItem(ProductsFragment.FRAGMENT_TYPE_OFFLINE).updateProducts(result);
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public static class MyThread extends Thread {

        public static final String HTTPS_API_LIFESUM_V1_SEARCH_QUERY_TYPE_FOOD = "https://api.lifesum.com/v1/search/query?type=food&search=\"";

        private Handler handler;
        private String queryString;

        public MyThread(Handler handler, String queryString) {
            this.handler = handler;
            this.queryString = queryString;
        }

        public String getQueryString() {
            return queryString;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            HttpURLConnection con = null;
            try {
                final String urlString = HTTPS_API_LIFESUM_V1_SEARCH_QUERY_TYPE_FOOD + URLEncoder.encode(queryString) + "\"";

                URL url = new URL(urlString);
                con = (HttpURLConnection) url.openConnection();

                // set up url connection to get retrieve information back
                con.setRequestMethod("GET");
                con.setDoInput(true);

                // stuff the authorization request header
                con.setRequestProperty("Authorization",
                        "Basic a794ecd348a3f71894426c65c37fea35da89a295bcbad687ca68a96fbfc7d371");
                con.connect();
                int status = con.getResponseCode();

                if (isInterrupted()) {
                    return;
                }

                switch (status) {
                    case 200:
                    case 201:
                        // pull the information back from the URL
                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null && !isInterrupted()) {
                            stringBuilder.append(line).append("\n");
                        }

                        if (isInterrupted()) {
                            return;
                        }

                        // output the information
                        System.out.println(stringBuilder);

                        List<Product> productList = new ArrayList<Product>();
                        JSONObject responseJson = new JSONObject(stringBuilder.toString()).getJSONObject("response");
                        JSONArray productsJson = responseJson.getJSONArray("list");
                        for (int i = 0; i < productsJson.length(); i++) {
                            productList.add(new Product(productsJson.getJSONObject(i)));
                            // don't check at every for step, might be a waste of time
                            if (i % 10 == 0 && isInterrupted()) {
                                return;
                            }
                        }

                        if (isInterrupted()) {
                            return;
                        }

                        if (handler != null) {
                            Message message = handler.obtainMessage();
                            message.what = WHAT_REQUEST_COMPLETED;
                            message.obj = productList;
                            handler.sendMessage(message);
                        }
                        break;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}