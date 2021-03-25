package com.example.hokiefootballmusic;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicReceiver extends BroadcastReceiver {

    playingScreen activity;

    public MusicReceiver(){
        //empty constructor
    }

    public MusicReceiver(playingScreen activity) {
        this.activity= activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getStringExtra(MusicService.MUSICNAME) != null){
        String musicName= intent.getStringExtra(MusicService.MUSICNAME);
        activity.updateName(musicName);}
        if(intent.getStringExtra("endBack") != null){
            activity.endback(intent.getStringExtra("endBack"));
        }
        if(intent.getStringExtra("startBackID") != null){
            activity.startBack(intent.getIntExtra("startBack",0),intent.getStringExtra("startBackID"));
        }

    }


}