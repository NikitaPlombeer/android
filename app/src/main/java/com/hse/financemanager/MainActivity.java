package com.hse.financemanager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hse.financemanager.adapers.FinanceItemAdapter;
import com.hse.financemanager.entity.FinanceItem;
import com.orm.SugarRecord;
import com.orm.query.Select;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.app_bar)
    protected Toolbar toolbar;

    @Bind(R.id.lvMain)
    protected ListView lvMain;

    private FinanceItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        MyDrawer.init(this, toolbar);

        //SugarORM
        List<FinanceItem> items = SugarRecord.listAll(FinanceItem.class, "date desc");

        adapter = new FinanceItemAdapter(items);
        lvMain.setAdapter(adapter);
        lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addButtonClick(false, (FinanceItem) adapter.getItem(position));
            }
        });

        MyDrawer.getInstance().changeBalance(FinanceItemUtil.getBalance(items));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                addButtonClick(true, null);
                return true;
            case R.id.search:
                openSearch();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addButtonClick(final boolean create, final FinanceItem item) {
        LayoutInflater li = getLayoutInflater();
        final View v = li.inflate(R.layout.edit_item_layout, null);
        final AlertDialogViewHolder holder = new AlertDialogViewHolder(v);

        if(!create){
            holder.nameEditText.setText(item.getName());
            for (int i = 0; i < holder.categorySpinner.getCount() - 1; i++) {
                String other = holder.categorySpinner.getItemAtPosition(i).toString();
                if(item.getCategory().equals(other)){
                    holder.categorySpinner.setSelection(i + 1);
                    break;
                }
            }
            holder.countEditText.setText(String.valueOf(Math.abs(item.getCount())));
            holder.financeTypeSpinner.setSelection(item.getCount() < 0 ? 2 : 1);
        }

        int resId = create ? R.string.create : R.string.update;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setPositiveButton(resId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = holder.nameEditText.getText().toString();
                        String category = holder.categorySpinner.getSelectedItem().toString();
                        float summ = Float.parseFloat(holder.countEditText.getText().toString());
                        if(holder.financeTypeSpinner.getSelectedItemPosition() == 2)
                            summ = -summ;

                        if(create)
                            addFinanceItem(name, category, summ);
                        else
                            updateFinanceItem(item, name, category, summ);
                        MyDrawer.getInstance().changeBalance(FinanceItemUtil.getBalance(adapter.getItems()));

                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setView(v);

        if(!create){
            builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SugarRecord.delete(item);
                    adapter.remove(item.getId());
                    adapter.notifyDataSetChanged();
                }
            });
        }

        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void updateFinanceItem(FinanceItem item, String name, String category, float count) {
        item.setName(name);
        item.setCategory(category);
        item.setCount(count);
        item.setDate(System.currentTimeMillis());
        adapter.notifyDataSetChanged();
        SugarRecord.save(item);
    }

    public void addFinanceItem(String name, String category, float count){
        FinanceItem first = Select.from(FinanceItem.class).orderBy("id desc").first();
        FinanceItem item = new FinanceItem(first == null ? 1 : first.getId() + 1, name,
                category, count, System.currentTimeMillis());

        adapter.addFinanceItem(item);
        adapter.notifyDataSetChanged();
        SugarRecord.save(item);
    }

    public class AlertDialogViewHolder{

        @Bind(R.id.nameEditText)
        MaterialEditText nameEditText;

        @Bind(R.id.categorySpinner)
        MaterialSpinner categorySpinner;

        @Bind(R.id.financeTypeSpinner)
        MaterialSpinner financeTypeSpinner;

        @Bind(R.id.countEditText)
        MaterialEditText countEditText;

        AlertDialogViewHolder(View view) {
            ButterKnife.bind(this, view);

            String[] week = new String[]{
                    getString(R.string.food), getString(R.string.car), getString(R.string.salary),
                    getString(R.string.transport), getString(R.string.health), getString(R.string.clothes),
                    getString(R.string.communication), getString(R.string.home), getString(R.string.taxi),
                    getString(R.string.cafe), getString(R.string.entertainment), getString(R.string.hygiene),
                    getString(R.string.sport), getString(R.string.pets), getString(R.string.gifts)
            };
            ArrayAdapter<String> weekAdapter = new ArrayAdapter<>(MyApp.getContext(), R.layout.spinner_item_preview, week);
            weekAdapter.setDropDownViewResource(R.layout.spinner_item);
            categorySpinner.setAdapter(weekAdapter);

            String[] types = new String[]{
                    getString(R.string.earnings), getString(R.string.costs)
            };
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(MyApp.getContext(), R.layout.spinner_item_preview, types);
            typeAdapter.setDropDownViewResource(R.layout.spinner_item);
            financeTypeSpinner.setAdapter(typeAdapter);
        }
    }

    private void openSearch() {

    }
}
