package com.nekodev.paulina.sadowska.filemanager;

/**
 * Created by Paulina Sadowska on 16.04.16.
 */
public class CheckCounter {

    private int checkCount = 0;

    public boolean updateCheckCount(boolean isChecked, boolean previousData){
        if(previousData == isChecked && isChecked && checkCount!=0)
            return false;

        if(isChecked){
            checkCount++;
            return false;
        }
        checkCount--;
        return checkCount<1;
    }

    public boolean isCheckCountEmpty(){
        return checkCount<1;
    }
}
