package com.hse.financemanager.entity;

import com.orm.dsl.Table;

/**
 * Created by Никита on 19.10.2016.
 */
@Table
public class FinanceItem {

    private long id;
    private String name;
    private String category;
    private float count; // double
    private long date;


    public FinanceItem(long id, String name, String category, float count, long date) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.count = count;
        this.date = date;
    }

    public FinanceItem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
