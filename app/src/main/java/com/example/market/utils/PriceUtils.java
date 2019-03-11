package com.example.market.utils;

import android.content.Context;

import com.example.market.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PriceUtils {
    public static String getCurrencyFormat(String rawPrice, Context context) {
        String formattedPrice="0";
        if (!rawPrice.isEmpty()) {
            //Some of prices are double
            int price;
            if (rawPrice.contains(".")) {
                int end = rawPrice.lastIndexOf(".");
                int start = 0;
                price = Integer.parseInt(rawPrice.substring(start, end));

            } else
                price = Integer.parseInt(rawPrice);
            NumberFormat formatter = new DecimalFormat("#,###");
            String currencyName = context.getString(R.string.currency);
            formattedPrice = formatter.format(price) + " " + currencyName;
        }
        return formattedPrice;
    }
}
