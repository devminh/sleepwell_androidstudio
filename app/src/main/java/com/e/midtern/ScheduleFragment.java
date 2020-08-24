package com.e.midtern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.TimePicker;

public class ScheduleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_schedule,container,false);
    }


}



class InputFilterMinMax implements InputFilter {

    private int min, max;

    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public InputFilterMinMax(String min, String max) {
        this.min = Integer.parseInt(min);
        this.max = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}

class TimeToWake{
    int wHour,wMin;
    boolean overDay = false;
    public TimeToWake(int hour,int min){
        wHour = hour;
        wMin = min;

    }

    public void RoundedTime(){
        while(wMin>=60) {
            wMin -= 60;
            wHour += 1;
        }
        if(wHour >=24) {
            wHour-=24;
            overDay=true;
        }
    }

    public void plusTime(int hour,int min){
        wMin += min;
        wHour+=hour;
        RoundedTime();
    }

    public String getTime() {
        int tempHour = wHour;
        String h = Integer.toString(wHour);
        String m = Integer.toString(wMin);
        if (wMin < 10)
            m = '0' + m;

        if (wHour < 10)
            h = '0' + h;
        if(wHour >12)
        {
            tempHour-=12;
            h = Integer.toString(tempHour);
            return h+':'+m+" PM";
        }
        return h+':'+m+" AM";
    }

    public int getwMin(){return wMin;}
    public int getwHour(){return wHour;}
    public boolean checkOverDay(){
        return overDay;
    }
}