package com.nekodev.paulina.sadowska.filemanager.data;

import java.util.Calendar;

/**
 * Created by Paulina Sadowska on 23.04.16.
 */
public class CustomDate {

    private Calendar date;
    private String dateFormat;

    public CustomDate(Calendar date, String dateFormat){
        this.date = date;
        this.dateFormat = dateFormat;
    }

    public String toString(){
        return dateFormat;
    }

    public Calendar getDate(){
        return date;
    }


}
