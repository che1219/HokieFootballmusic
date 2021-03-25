package com.example.hokiefootballmusic;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusicService extends Service {

    MusicPlayer musicPlayer;
    private final IBinder iBinder= new MyBinder();

    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
    }

    public void startMusic(int number, ArrayList<Integer> backMusic, ArrayList<Integer> backtime){

        musicPlayer.playMusic(number, backMusic,backtime);
    }

    public void pauseMusic(){

        musicPlayer.pauseMusic();
    }

    public void resumeMusic(){

        musicPlayer.resumeMusic();
    }

    public void stopMusic(){

        musicPlayer.stopPlay();
    }


    public int getPlayingStatus(){

        return musicPlayer.getMusicStatus();
    }


    public void onUpdateMusicName(String musicname) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(MUSICNAME, musicname);
        sendBroadcast(intent);
    }

    public void endBack(String id) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra("endBack", id);
        sendBroadcast(intent);
    }

    public void startBack(int number,String id) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra("startBack", number);
        intent.putExtra("startBackID", id);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    public class MyBinder extends Binder {

        MusicService getService(){
            return MusicService.this;
        }
    }

}