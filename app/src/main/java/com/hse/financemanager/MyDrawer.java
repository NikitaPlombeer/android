package com.hse.financemanager;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

/**
 * Created by Никита on 06.03.2016.
 */
public class MyDrawer {

    private static MyDrawer instance;

    private Drawer drawer;
    public Drawer getDrawer() {
        return drawer;
    }

    private MyDrawer(final Activity activity, Toolbar toolbar) {

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.list).withTextColorRes(R.color.primaryText).withIcon(R.drawable.ic_list_black_24dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.graphics).withTextColorRes(R.color.primaryText).withIcon(R.drawable.ic_timeline_black_24dp);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.visualization).withTextColorRes(R.color.primaryText).withIcon(R.drawable.ic_place_black_24dp);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(R.string.prediction).withTextColorRes(R.color.primaryText).withIcon(R.drawable.ic_trending_up_black_24dp);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName(R.string.achievements).withTextColorRes(R.color.primaryText).withIcon(R.drawable.ic_done_all_black_24dp);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(R.drawable.header_background)
                .addProfiles(
                        new ProfileDrawerItem().withEmail("lyashuk.x@gmail.com").withName("Ляшук Александр").withIcon(R.drawable.nobody)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .withSelectionListEnabledForSingleProfile(false)
                .build();

        //Создаем боковое меню
        drawer = new DrawerBuilder()
                .withActivity(activity)
                .withToolbar(toolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //Обработка событий нажатия на элемент меню из списка
                        Log.i("position", "" + position);
                        return false;
                    }
                })
                .withSelectedItem(-1)
                .build();
        drawer.setSelectionAtPosition(1);
    }

    public void changeBalance(float balance){
        View header = drawer.getHeader();
        TextView textView = (TextView) header.findViewById(R.id.material_drawer_account_header_email);
        String color = "#FFFFFF";
        if(balance < 0)
            color = String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(MyApp.getContext(),R.color.outcomeTextColor)));
        else
            color = String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(MyApp.getContext(),R.color.incomeTextColor)));

        String balanceStr = String.valueOf(balance);

        String str = "Баланс: <font color = \"" + color +"\">"+FinanceItemUtil.getStringBalance(balance) + "</font> руб.";
        textView.setText(Html.fromHtml(str));

    }
    public static MyDrawer getInstance() {
        return instance;
    }

    public static void init(Activity activity, Toolbar toolbar) {
        instance = new MyDrawer(activity, toolbar);
    }
}
