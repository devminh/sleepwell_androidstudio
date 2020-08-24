package com.e.midtern;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;


import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Array;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Button CalculateNow,mFirstcycle,mSecondcycle,mThirdcycle,mFouthcycle,mFifthcycle,mSixcycle;
    TextView firstCycle,secondCycle,thirdCycle,fourthCycle,fifthCycle,sixthCycle,firstwake,secondwake,thirdwake,fourthwake;

    RelativeLayout rela,rela2;
    EditText LimHour,LimMin;

    Fragment fragmentA = new NowFragment();
    Fragment fragmentB = new ScheduleFragment();
    Fragment fragmentC = new SongFragment();
    Fragment fragmentD = new TrackingFragment();

    ArrayList<String> seven_days = new ArrayList<String>();
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        


        bottomNavigationView = findViewById(R.id.bottom_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        getSupportFragmentManager().beginTransaction()

                .add(R.id.fragment_container, fragmentA , "nowFrag")
                .add(R.id.fragment_container, fragmentB, "scheduleFrag")
                .add(R.id.fragment_container, fragmentC , "songFrag")
                .add(R.id.fragment_container, fragmentD , "trackingFrag")
                .show(fragmentA)
                .hide(fragmentB)
                .hide(fragmentC)
                .hide(fragmentD)

                .commit();


        final LayoutInflater factory = getLayoutInflater();
        final View textEntryView = factory.inflate(R.layout.activity_schedule, null);
        //LayoutInflater actinow = getLayoutInflater();
        //View getactinow = actinow.inflate(R.layout.activity_now,null);
        //LimHour = textEntryView.findViewById(R.id.input_hour);
        //LimMin = textEntryView.findViewById(R.id.input_min);
        //LimHour.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        //LimMin.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});





    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                    if(menuItem.getItemId() == R.id.action_now ){

                        getSupportFragmentManager().beginTransaction()
                                .show(fragmentA)
                                .hide(fragmentB)
                                .hide(fragmentC)
                                .hide(fragmentD)

                                .commit();
                    }

                    else if(menuItem.getItemId() == R.id.action_schedule ){
                        getSupportFragmentManager().beginTransaction()
                                .show(fragmentB)
                                .hide(fragmentA)
                                .hide(fragmentC)
                                .hide(fragmentD)

                                .commit();

                    }
                    else if(menuItem.getItemId() == R.id.action_songs ){





                        getSupportFragmentManager().beginTransaction()
                                .show(fragmentC)
                                .hide(fragmentA)
                                .hide(fragmentB)
                                .hide(fragmentD)

                                .commit();
                    }
                    else if(menuItem.getItemId() == R.id.action_tracking ){

                        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("tracking_db", MODE_APPEND);
                        String sleep_cache = sh.getString("sleeptime_cache","");
                        if(sleep_cache !=""){
                            Button starttosleep_btn;
                            starttosleep_btn = findViewById(R.id.start_sleep);
                            starttosleep_btn.setVisibility(View.INVISIBLE);

                            TextView starttime;
                            starttime = findViewById(R.id.show_startsleep_time);
                            starttime.setText(sleep_cache);

                        }

                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat shortformat = new SimpleDateFormat("dd-MM-yyyy");


                        for(int i=0;i<7;i++){
                            cal.add(Calendar.DATE, -1);
                            String temp = shortformat.format(cal.getTime());
                            seven_days.add(temp);
                            Log.d("first day",temp);
                        }

                        float sum=0;
                        barEntries = new ArrayList<>();
                        float count=0;

                        for(int i=0;i<7;i++){
                            float gethour = sh.getFloat(seven_days.get(i),0);



                            if(gethour > 0){
                                count++;
                            }
                            sum +=gethour;


                        }

                        float avg=sum/count;
                        //float avg= (float) 2.5;

                        TextView showavghour;
                        TextView day1today7;
                        TextView warning_sleep = findViewById(R.id.warning_sleep);
                        TextView suggest_sleep = findViewById(R.id.suggest_sleep);
                        barChart = findViewById(R.id.BarChart);
                        TextView suggest_header =  findViewById(R.id.suggest_header);


                        showavghour = findViewById(R.id.show_sleep_avg);
                        day1today7 = findViewById(R.id.day1to7);

                        if(avg >0) {

                            ArrayList<String> danger_suggest = new ArrayList<String>();
                            danger_suggest.add("You may have sleeping difficulty.You should see a doctor if your sleeping difficulties are ongoing and affecting your quality of life.");
                            danger_suggest.add("Don’t consume caffeine late in the day.When consumed late in the day, caffeine stimulates your nervous system and may stop your body from naturally relaxing at night.");
                            danger_suggest.add("Try to sleep and wake at consistent times !");

                            ArrayList<String> warning_suggest = new ArrayList<String>();
                            warning_suggest.add("You should listen to relaxing music, read a book, take a hot bath or meditate.They can lead to a deep and quality sleep.");
                            warning_suggest.add("Get a comfortable bed, mattress, and pillow can help improve your sleep.");
                            warning_suggest.add("Be smart about what you eat and drink.Your daytime eating habits play a role in how well you sleep, especially in the hours before bedtime.");

                            ArrayList<String> normal_suggest = new ArrayList<String>();
                            normal_suggest.add("You already have a good routine of sleep.Try to maintain to sleep from 7-9 hours/a night");
                            normal_suggest.add("Keep your room dark, cool, and quiet. Small changes to your environment can make a big difference to your quality of sleep.");
                            normal_suggest.add("Drinking a cup of warm water may help you sleep better!");

                            Random rand = new Random(); //instance of random class
                            int upperbound = 3;
                            //generate random values from 0-24
                            int random_num = rand.nextInt(upperbound);

                            if (avg < 3) {
                                warning_sleep.setText("Danger ! Sleep less than 3 hours will deteriorate your health !");
                                warning_sleep.setTextColor(Color.parseColor("#fc9303"));
                                suggest_sleep.setText(danger_suggest.get(random_num));

                            } else if (avg < 6) {
                                warning_sleep.setText("Warning ! Getting less than 6 hours of sleep could double – or even triple – your risk of dying from heart disease or cancer, especially if you have chronic diseases.");
                                warning_sleep.setTextColor(Color.parseColor("#ffce00"));
                                suggest_sleep.setText(warning_suggest.get(random_num));


                            } else if (avg > 6) {
                                warning_sleep.setText("Normal sleep ! In today's fast-paced society, six or seven hours of sleep may sound pretty good");
                                warning_sleep.setTextColor(Color.parseColor("#a5d610"));
                                suggest_sleep.setText(normal_suggest.get(random_num));
                            } else if (avg >= 7) {
                                warning_sleep.setText("Very good ! You have a very good average hour of sleep which is ranged from 7-9 hours");
                                warning_sleep.setTextColor(Color.parseColor("#005221"));
                                suggest_sleep.setText(normal_suggest.get(random_num));
                            }


                            showavghour.setText("In 7 recent days , your average sleep is : " + Float.toString(avg) + " hours");


                            //draw vertical bar chart



                            Collections.reverse(seven_days);

                            day1today7.setText("The bar chart below shows your sleeping time from " + seven_days.get(0) + " to " + seven_days.get(6));


                            barEntries.add(new BarEntry(1f, sh.getFloat(seven_days.get(0), 0)));
                            barEntries.add(new BarEntry(2f, sh.getFloat(seven_days.get(1), 0)));
                            barEntries.add(new BarEntry(3f, sh.getFloat(seven_days.get(2), 0)));
                            barEntries.add(new BarEntry(4f, sh.getFloat(seven_days.get(3), 0)));
                            barEntries.add(new BarEntry(5f, sh.getFloat(seven_days.get(4), 0)));
                            barEntries.add(new BarEntry(6f, sh.getFloat(seven_days.get(5), 0)));
                            barEntries.add(new BarEntry(7f, sh.getFloat(seven_days.get(6), 0)));

                            barDataSet = new BarDataSet(barEntries, "");
                            barData = new BarData(barDataSet);
                            barChart.setData(barData);


                            XAxis xAxis = barChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

                            seven_days.add(0, "lala");
                            Log.d("first element", seven_days.get(0));


                            IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(seven_days);
                            xAxis.setGranularity(1f);
                            xAxis.setValueFormatter(formatter);

                            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                            barDataSet.setValueTextColor(Color.BLACK);
                            barDataSet.setValueTextSize(18f);

                        }
                        else{
                            warning_sleep.setText("The sleep data is empty.Start to track from today !");
                            warning_sleep.setTextColor(Color.parseColor("#ffffff"));
                            suggest_sleep.setVisibility(View.INVISIBLE);
                            barChart.setVisibility(View.INVISIBLE);
                            suggest_header.setVisibility(View.INVISIBLE);

                        }


                        getSupportFragmentManager().beginTransaction()
                                .show(fragmentD)
                                .hide(fragmentA)
                                .hide(fragmentB)
                                .hide(fragmentC)

                                .commit();
                    }


                    return true;

                }
            };

    public void CalculateOnClick(View view) {

        rela = findViewById(R.id.rela_now);
        firstCycle = findViewById(R.id.first_cycle);
        secondCycle = findViewById(R.id.second_cycle);
        thirdCycle = findViewById(R.id.third_cycle);
        fourthCycle = findViewById(R.id.fourth_cycle);
        fifthCycle = findViewById(R.id.fifth_cycle);
        sixthCycle = findViewById(R.id.sixth_cycle);

        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.inTime();
        firstCycle.setText(itstime.getTime());
        itstime.inTime();
        secondCycle.setText(itstime.getTime());
        itstime.inTime();
        thirdCycle.setText(itstime.getTime());
        itstime.inTime();
        fourthCycle.setText(itstime.getTime());
        itstime.inTime();
        fifthCycle.setText(itstime.getTime());
        itstime.inTime();
        sixthCycle.setText(itstime.getTime());
        rela.setVisibility(View.VISIBLE);

    }

    public void ScheduleOnClick(View view) {
        TimePicker timePicker ;
        timePicker = findViewById(R.id.timepicker1);

        int hour=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();


        rela2 = findViewById(R.id.rela_schedule);


        firstwake = findViewById(R.id.first_cycle_wake);
        secondwake = findViewById(R.id.second_cycle_wake);
        thirdwake = findViewById(R.id.third_cycle_wake);
        fourthwake = findViewById(R.id.fourth_cycle_wake);

        //LimHour.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "23")});
        //LimMin.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "59")});

        //int hour = Integer.parseInt(LimHour.getText().toString());
        //int min = Integer.parseInt(LimMin.getText().toString());



        if (hour <24 && hour >=0 && min <60 && min>=0){
            TimeToWake itstime = new TimeToWake(hour,min);
            itstime.plusTime(15,0);
            firstwake.setText(itstime.getTime());
            itstime.plusTime(1,30);
            secondwake.setText(itstime.getTime());
            itstime.plusTime(1,30);
            thirdwake.setText(itstime.getTime());
            itstime.plusTime(1,30);
            fourthwake.setText(itstime.getTime());
            rela2.setVisibility(View.VISIBLE);
        }

    }

    public void SetAlarmFirst(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(1);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetAlarmSecond(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(2);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetAlarmThird(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(3);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetAlarmFourth(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(4);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetAlarmFifth(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(5);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetAlarmSixth(View view) {
        Calendar now = Calendar.getInstance();
        int instanceMin = now.get(Calendar.MINUTE);
        int instanceHour = now.get(Calendar.HOUR_OF_DAY);
        TimeToSleep itstime = new TimeToSleep(instanceHour,instanceMin+14);
        itstime.SetTimeForAlarm(6);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,itstime.getHour());
        intent.putExtra(AlarmClock.EXTRA_MINUTES,itstime.getMin());
        startActivity(intent);
    }

    public void SetReminderSecond(View view) {
        Calendar cal = Calendar.getInstance();
        TimePicker timePicker ;
        timePicker = findViewById(R.id.timepicker1);

        int hour=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        TimeToWake itstime = new TimeToWake(hour,min);
        itstime.plusTime(16,30);
        cal.set(Calendar.HOUR_OF_DAY,itstime.getwHour());
        cal.set(Calendar.MINUTE,itstime.getwMin());
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date temp = cal.getTime();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        if(itstime.checkOverDay())
        {
            intent.putExtra("beginTime", cal.getTimeInMillis()+60*24*60*1000);
            intent.putExtra("endTime", cal.getTimeInMillis()+60*24*60*1000);
        }
        else{
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("endTime", cal.getTimeInMillis());
        }

        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("title", "A reminder from Sleepwell");
        startActivity(intent);
    }

    public void SetReminderFirst(View view) {
        Calendar cal = Calendar.getInstance();
        TimePicker timePicker ;
        timePicker = findViewById(R.id.timepicker1);

        int hour=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        TimeToWake itstime = new TimeToWake(hour,min);
        itstime.plusTime(15,0);
        cal.set(Calendar.HOUR_OF_DAY,itstime.getwHour());
        cal.set(Calendar.MINUTE,itstime.getwMin());
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date temp = cal.getTime();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        if(itstime.checkOverDay())
        {
            intent.putExtra("beginTime", cal.getTimeInMillis()+60*24*60*1000);
            intent.putExtra("endTime", cal.getTimeInMillis()+60*24*60*1000);
        }
        else{
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("endTime", cal.getTimeInMillis());
        }

        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("title", "A reminder from Sleepwell");
        startActivity(intent);
    }

    public void SetReminderFourth(View view) {
        Calendar cal = Calendar.getInstance();
        TimePicker timePicker ;
        timePicker = findViewById(R.id.timepicker1);

        int hour=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        TimeToWake itstime = new TimeToWake(hour,min);
        itstime.plusTime(19,30);
        cal.set(Calendar.HOUR_OF_DAY,itstime.getwHour());
        cal.set(Calendar.MINUTE,itstime.getwMin());
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date temp = cal.getTime();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        if(itstime.checkOverDay())
        {
            intent.putExtra("beginTime", cal.getTimeInMillis()+60*24*60*1000);
            intent.putExtra("endTime", cal.getTimeInMillis()+60*24*60*1000);
        }
        else{
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("endTime", cal.getTimeInMillis());
        }

        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("title", "A reminder from Sleepwell");
        startActivity(intent);
    }

    public void SetReminderThird(View view) {
        Calendar cal = Calendar.getInstance();
        TimePicker timePicker ;
        timePicker = findViewById(R.id.timepicker1);

        int hour=timePicker.getCurrentHour();
        int min=timePicker.getCurrentMinute();
        TimeToWake itstime = new TimeToWake(hour,min);
        itstime.plusTime(18,0);
        cal.set(Calendar.HOUR_OF_DAY,itstime.getwHour());
        cal.set(Calendar.MINUTE,itstime.getwMin());
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        Date temp = cal.getTime();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        if(itstime.checkOverDay())
        {
            intent.putExtra("beginTime", cal.getTimeInMillis()+60*24*60*1000);
            intent.putExtra("endTime", cal.getTimeInMillis()+60*24*60*1000);
        }
        else{
            intent.putExtra("beginTime", cal.getTimeInMillis());
            intent.putExtra("endTime", cal.getTimeInMillis());
        }

        intent.putExtra("allDay", false);
        intent.putExtra("rrule", "FREQ=YEARLY");

        intent.putExtra("title", "A reminder from Sleepwell");
        startActivity(intent);
    }


    public String currentDate;

    public void startSleeping(View view) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");

        SimpleDateFormat shortformat = new SimpleDateFormat("dd-MM-yyyy");



        String datetime = dateformat.format(c.getTime());

        currentDate = shortformat.format(c.getTime());

        Button wakeup_btn;
        wakeup_btn = findViewById(R.id.wake_up);
        wakeup_btn.setVisibility(View.VISIBLE);


        SharedPreferences sharedPreferences = getSharedPreferences("tracking_db", MODE_PRIVATE);

        SharedPreferences.Editor myEdit  = sharedPreferences.edit();

        myEdit.putString("sleeptime_cache",datetime);
        myEdit.commit();


        Button starttosleep_btn;
        starttosleep_btn = findViewById(R.id.start_sleep);
        starttosleep_btn.setVisibility(View.INVISIBLE);

        TextView starttime;
        starttime = findViewById(R.id.show_startsleep_time);
        starttime.setText(datetime);

    }




    public void wakeUp(View view) throws ParseException{
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss aa");
        String wakeuptime = dateformat.format(c.getTime());

        TextView starttime;
        starttime = findViewById(R.id.show_startsleep_time);

        String begintime  = starttime.getText().toString();



        Date olddate = dateformat.parse(begintime);
        Date newdate = dateformat.parse(wakeuptime);

        double diff = newdate.getTime() - olddate.getTime();

        double seconds = diff / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;
        double days = hours / 24;

        TextView showwakeup;
        showwakeup = findViewById(R.id.show_wakeup_time);

        int roundhours  = (int) hours;

        double decimal = hours - roundhours;

        double newminutes =  decimal * 60;

        int minmin = (int) newminutes;


        showwakeup.setText( "You have slept  : " + Integer.toString(roundhours)+ "  hours " + Integer.toString(minmin) + " minutes");

        Button starttosleep_btn;
        starttosleep_btn = findViewById(R.id.start_sleep);
        starttosleep_btn.setVisibility(View.VISIBLE);

        Button wakeup_btn;
        wakeup_btn = findViewById(R.id.wake_up);
        wakeup_btn.setVisibility(View.INVISIBLE);

        @SuppressLint("WrongConstant") SharedPreferences sh = getSharedPreferences("tracking_db", MODE_APPEND);
        float previous_hours = sh.getFloat(currentDate,0);


        SharedPreferences sharedPreferences = getSharedPreferences("tracking_db", MODE_PRIVATE);

        SharedPreferences.Editor myEdit  = sharedPreferences.edit();

        myEdit.putFloat(currentDate,(float) (hours + previous_hours) );




        myEdit.putString("sleeptime_cache","");

        myEdit.commit();

    }
}
