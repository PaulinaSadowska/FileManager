package com.nekodev.paulina.sadowska.filemanager.utilities;

/**
 * Created by Paulina Sadowska on 16.04.16.
 */
public class CheckCounter {

    private int checkCount = 0;

    public boolean updateCheckCount(boolean isChecked){

        if(isChecked){
            checkCount++;
            return false;
        }
        checkCount--;
        return checkCount<1;
    }

    public int getCheckCount(){
        return checkCount;
    }
}
