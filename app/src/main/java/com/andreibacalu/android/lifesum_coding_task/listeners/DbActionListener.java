package com.andreibacalu.android.lifesum_coding_task.listeners;

import com.andreibacalu.android.lifesum_coding_task.models.Product;

import java.util.List;

/**
 * Created by Andrei on 21-08-14.
 */
public interface DbActionListener {
    void onSaveSelected(Product[] products);
    void onDeleteSelected(Product[] products);
}
