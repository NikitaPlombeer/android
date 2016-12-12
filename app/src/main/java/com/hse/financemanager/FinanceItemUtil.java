package com.hse.financemanager;

import com.hse.financemanager.entity.FinanceItem;
import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by dev on 06.11.16.
 */

public class FinanceItemUtil {

    public static float getBalance(List<FinanceItem> items){
        float count = 0.f;
        for (FinanceItem item : items) {
            count += item.getCount();
        }
        return count;
    }

    public static String getStringBalance(float b){
        String balance = String.valueOf(b);
        int index = balance.indexOf('.');
        for (int i = index - 3; i >= 0; i -= 3) {
            balance = balance.substring(0, i).concat(" ").concat(balance.substring(i));
        }
        return balance;
    }
}
