package com.e.midtern;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

public class SongFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {




        return inflater.inflate(R.layout.activity_songs,container,false);



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Random rand = new Random(); //instance of random class
        int upperbound = 3;
        //generate random values from 0-24
        int random_num = rand.nextInt(upperbound);

        WebView myWebView;
        myWebView = (WebView) getView().findViewById(R.id.webview);


        myWebView.getSettings().setLoadsImagesAutomatically(true);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        ArrayList<String> embeded = new ArrayList<>();

        String html1 = "<iframe src=\"https://www.nhaccuatui.com/lh/auto/ZmkxNF5O4tnX\" width=\"100%\" height=\"500\" frameborder=\"0\" allowfullscreen allow=\"autoplay\"></iframe>";
        String html2="<iframe width=\"100%\" height=\"500\" scrolling=\"no\" frameborder=\"no\" allow=\"autoplay\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1031921836&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true\"></iframe><div style=\"font-size: 10px; color: #cccccc;line-break: anywhere;word-break: normal;overflow: hidden;white-space: nowrap;text-overflow: ellipsis; font-family: Interstate,Lucida Grande,Lucida Sans Unicode,Lucida Sans,Garuda,Verdana,Tahoma,sans-serif;font-weight: 100;\"><a href=\"https://soundcloud.com/music-for-life-playlists\" title=\"Music\" target=\"_blank\" style=\"color: #cccccc; text-decoration: none;\">Music</a> · <a href=\"https://soundcloud.com/music-for-life-playlists/sets/meditation\" title=\"meditation\" target=\"_blank\" style=\"color: #cccccc; text-decoration: none;\">meditation</a></div>";
        String html3="<iframe width=\"100%\" height=\"500\" scrolling=\"no\" frameborder=\"no\" allow=\"autoplay\" src=\"https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/playlists/1045716481&color=%23ff5500&auto_play=false&hide_related=false&show_comments=true&show_user=true&show_reposts=false&show_teaser=true&visual=true\"></iframe><div style=\"font-size: 10px; color: #cccccc;line-break: anywhere;word-break: normal;overflow: hidden;white-space: nowrap;text-overflow: ellipsis; font-family: Interstate,Lucida Grande,Lucida Sans Unicode,Lucida Sans,Garuda,Verdana,Tahoma,sans-serif;font-weight: 100;\"><a href=\"https://soundcloud.com/music-for-life-playlists\" title=\"Music\" target=\"_blank\" style=\"color: #cccccc; text-decoration: none;\">Music</a> · <a href=\"https://soundcloud.com/music-for-life-playlists/sets/sleep\" title=\"Sleep\" target=\"_blank\" style=\"color: #cccccc; text-decoration: none;\">Sleep</a></div>";

        embeded.add(html1);
        embeded.add(html2);
        embeded.add(html3);

        myWebView.loadData(embeded.get(random_num), "text/html", null);


        ArrayList<String> knowlist = new ArrayList<>();
        knowlist.add("A soothing relaxing music can help you sleep faster!");
        knowlist.add("Reduce the light in your room will help you sleep better.Just turn off the light and enjoy our music ! We hope you will get a good dream :) ");
        knowlist.add("We have selected carefully music playlists to help you sleep well !");
        TextView didyouknow = getView().findViewById(R.id.did_you_know);
        didyouknow.setText(knowlist.get(random_num));




    }


}
