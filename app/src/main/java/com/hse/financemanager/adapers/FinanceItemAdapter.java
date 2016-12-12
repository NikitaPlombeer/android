package com.hse.financemanager.adapers;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hse.financemanager.FinanceItemUtil;
import com.hse.financemanager.MyApp;
import com.hse.financemanager.R;
import com.hse.financemanager.entity.FinanceItem;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Никита on 19.10.2016.
 */

public class FinanceItemAdapter extends BaseAdapter{

    private List<FinanceItem> items;
    private final LayoutInflater lInflater;


    public FinanceItemAdapter(List<FinanceItem> items) {
        this.items = items;
        lInflater = (LayoutInflater) MyApp.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public FinanceItemAdapter() {
        this(new ArrayList<FinanceItem>());
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    public void addFinanceItem(FinanceItem item){
        items.add(0, item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.finance_item_layout, parent, false);
        }

        FinanceItem item = items.get(position);
        ViewHolder holder = new ViewHolder(view);
        holder.nameTextView.setTextColor(Color.parseColor("#000000"));
        holder.nameTextView.setText(item.getName());

        holder.categoryTextView.setText(item.getCategory());
        holder.categoryTextView.setTextColor(Color.parseColor("#FFFFFF"));

        if(item.getCount() > 0){
            holder.countTextView.setText("+" + FinanceItemUtil.getStringBalance(item.getCount()));
            holder.countTextView.setTextColor(ContextCompat.getColor(MyApp.getContext(),R.color.incomeTextColor));
        } else{
            holder.countTextView.setText(FinanceItemUtil.getStringBalance(item.getCount()));
            holder.countTextView.setTextColor(ContextCompat.getColor(MyApp.getContext(),R.color.outcomeTextColor));
        }

        //holder.dateTextView.setTextColor(Color.parseColor("#000000"));
        //holder.dateTextView.setText(new SimpleDateFormat("d MMM HH:mm").format(new Date(item.getDate())));

        return view;
    }

    public void remove(long id) {
        for (int i = 0; i < items.size(); i++) {
            if(items.get(i).getId() == id){
                items.remove(i);
                break;
            }
        }
    }

    public void updateFinanceItem(FinanceItem item) {

    }

    public List<FinanceItem> getItems() {
        return items;
    }

    public class ViewHolder{
        @Bind(R.id.nameTextView)
        TextView nameTextView;

        @Bind(R.id.countTextView)
        TextView countTextView;

        @Bind(R.id.categoryTextView)
        TextView categoryTextView;

        //@Bind(R.id.dateTextView)
        //TextView dateTextView;

        ViewHolder(View itemView){
            ButterKnife.bind(this, itemView);
        }
    }
}
