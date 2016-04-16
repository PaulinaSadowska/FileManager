package com.nekodev.paulina.sadowska.filemanager;

import android.app.Activity;

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
public class CustomDateFormat {

    @BindString(R.string.january) String january;
    @BindString(R.string.february) String february;
    @BindString(R.string.march) String march;
    @BindString(R.string.april) String april;
    @BindString(R.string.may) String may;
    @BindString(R.string.june) String june;
    @BindString(R.string.july) String july;
    @BindString(R.string.august) String august;
    @BindString(R.string.september) String september;
    @BindString(R.string.october) String october;
    @BindString(R.string.november) String november;
    @BindString(R.string.december) String december;
    @BindString(R.string.unknown_date) String dateUnknown;


    private Map<Integer, String> months;

    private Calendar date;
    public CustomDateFormat(Date date, Activity activity){
        this.date = new GregorianCalendar();
        this.date.setTime(date);
        ButterKnife.bind(this, activity);
        initMonthMap();
    }

    private void initMonthMap() {
        months = new HashMap<>();
        months.put(1, january);
        months.put(2, february);
        months.put(3, march);
        months.put(4, april);
        months.put(5, may);
        months.put(6, june);
        months.put(7, july);
        months.put(8, august);
        months.put(9, september);
        months.put(10, october);
        months.put(11, november);
        months.put(12, december);
    }

    public String toString() {
        int month = date.get(Calendar.MONTH);
        if(month>0 && month<13)
            return date.get(Calendar.DAY_OF_MONTH) + " " + months.get(month)+ " " + date.get(Calendar.YEAR) +
                    " " + date.get(Calendar.HOUR) + ":" + date.get(Calendar.MINUTE);
        else
            return dateUnknown;
    }
}
