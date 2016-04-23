package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;
import android.util.Log;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Paulina Sadowska on 16.04.16.
 */
public class CustomDateFactory {

    @BindString(R.string.january)
    String january;
    @BindString(R.string.february)
    String february;
    @BindString(R.string.march)
    String march;
    @BindString(R.string.april)
    String april;
    @BindString(R.string.may)
    String may;
    @BindString(R.string.june)
    String june;
    @BindString(R.string.july)
    String july;
    @BindString(R.string.august)
    String august;
    @BindString(R.string.september)
    String september;
    @BindString(R.string.october)
    String october;
    @BindString(R.string.november)
    String november;
    @BindString(R.string.december)
    String december;
    @BindString(R.string.unknown_date)
    String dateUnknown;


    private Map<Integer, String> months;
    Calendar date;


    public CustomDateFactory(Date date, Activity activity) {
        this.date = new GregorianCalendar();
        this.date.setTime(date);

        ButterKnife.bind(this, activity);
        initMonthMap();

        int month = this.date.get(Calendar.MONTH);
        if (month < 0 || month > 11) {
            Log.e("UNKNOWN", date.toString());
        }
    }

    private void initMonthMap() {
        months = new HashMap<>();
        months.put(0, january);
        months.put(1, february);
        months.put(2, march);
        months.put(3, april);
        months.put(4, may);
        months.put(5, june);
        months.put(6, july);
        months.put(7, august);
        months.put(8, september);
        months.put(9, october);
        months.put(10, november);
        months.put(11, december);
    }

    private String _toString() {
        int month = date.get(Calendar.MONTH);
        NumberFormat nf = NumberFormat.getInstance();
        if (month >= 0 && month < 12) {
            String time = String.format("%02d:%02d", date.get(Calendar.HOUR), date.get(Calendar.MINUTE));
            return date.get(Calendar.DAY_OF_MONTH) + " " + months.get(month) + " " + date.get(Calendar.YEAR) +
                    " " + time;
        } else
            return dateUnknown;
    }

    public CustomDate build() {
        return new CustomDate(date, this._toString());
    }
}
