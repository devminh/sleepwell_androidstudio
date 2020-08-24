package com.e.midtern;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class NowFragment extends Fragment {
    Button mFirstcycle;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_now,container,false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}






class TimeToSleep{
    int nowHour;
    int nowMinnute;

    public TimeToSleep(int hour, int min){
        nowHour = hour;
        nowMinnute = min;
    }

    public void RoundedTime(){
        while(nowMinnute>=60) {
            nowMinnute -= 60;
            nowHour += 1;
        }
        if(nowHour >=24) {
            nowHour-=24;
        }
    }

    public void inTime(){
        nowMinnute += 90;
        RoundedTime();
    }



    public Integer getMin(){
        return nowMinnute;
    }

    public Integer getHour(){
        return nowHour;
    }

    public String getTime() {
        int tempHour = nowHour;
        String h = Integer.toString(nowHour);
        String m = Integer.toString(nowMinnute);
        if (nowMinnute < 10)
            m = '0' + m;

        if (nowHour < 10)
            h = '0' + h;
        if(nowHour >12)
        {
            tempHour-=12;
            h = Integer.toString(tempHour);
            return h+':'+m+" PM";
        }
        return h+':'+m+" AM";
    }

    public void SetTimeForAlarm(int i){
        nowMinnute += 90*i;
        RoundedTime();
    }
}
