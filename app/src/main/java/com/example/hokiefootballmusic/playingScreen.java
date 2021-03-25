package com.example.hokiefootballmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class playingScreen extends AppCompatActivity implements View.OnClickListener {
    TextView music;
    Button play,restart;
    ImageView image;
    ArrayList<Integer> backMusic = new ArrayList<Integer>();
    ArrayList<Integer> backtime = new ArrayList<Integer>();
    MusicService musicService;
    MusicReceiver musicReceiver;
    Intent startMusicServiceIntent;
    Bundle b1;
    int mainMusic;
    boolean isBound = false;
    boolean isInitialized = false;
    String id = "";

    public static final String INITIALIZE_STATUS = "intialization status";
    public static final String MUSIC_PLAYING = "music playing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_screen);

        music = (TextView) findViewById(R.id.music);
        image = (ImageView) findViewById(R.id.imageView);


        b1 = this.getIntent().getExtras();
        for(int i = 0; i < 3; i++){
            backMusic.add(b1.getIntegerArrayList("backMusic").get(i));
            backtime.add(b1.getIntegerArrayList("backtime").get(i));
        }
        if(b1.getString("lastMusic") != null){
            music.setText(b1.getString("lastMusic"));
        }
        else{
            music.setText(b1.getString("main"));
        }
        if (b1.getString("main").equals("Go Tech Go")) {
                mainMusic = 0;
        }
        if (b1.getString("main").equals("Great Day To Be A Hokie")) {
                mainMusic = 1;
        }
        if (b1.getString("main").equals("Hokies Fight Song")) {
                mainMusic = 2;
        }

        if(savedInstanceState != null){
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            music.setText(savedInstanceState.getString(MUSIC_PLAYING));
        }

        startMusicServiceIntent= new Intent(this, MusicService.class);

        if(!isInitialized){
            startService(startMusicServiceIntent);
            isInitialized= true;
        }

        musicReceiver = new MusicReceiver(this);
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);

    }

    public void backClicked(View view){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("lastMusic", b1.getString("main"));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.restart:
                if (isBound) {
                    play.setText("play");
                    musicService.stopMusic();
                    music.setText(b1.getString("main"));
                    backMusic.clear();
                    backtime.clear();
                    for(int i = 0; i < 3; i++){
                        backMusic.add(b1.getIntegerArrayList("backMusic").get(i));
                        backtime.add(b1.getIntegerArrayList("backtime").get(i));
                    }
                }
                break;
            case R.id.play:
                if (isBound) {
                    switch (musicService.getPlayingStatus()){
                        case 0:
                            musicService.startMusic(mainMusic,backMusic,backtime);
                            play.setText("Pause");
                            break;
                        case 1:
                            musicService.pauseMusic();
                            play.setText("Resume");
                            break;
                        case 2:
                            musicService.resumeMusic();
                            play.setText("Pause");
                            break;
                    }
                }
                break;
        }

    }
    public void updateName(String musicName) {

        music.setText(musicName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInitialized && !isBound){
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }

        registerReceiver(musicReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isBound){
            unbindService(musicServiceConnection);
            isBound= false;
        }

        unregisterReceiver(musicReceiver);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, b1.getString("main"));
        super.onSaveInstanceState(outState);
    }


    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;

            switch (musicService.getPlayingStatus()) {
                case 0:
                    play.setText("Start");
                    break;
                case 1:
                    play.setText("Pause");
                    break;
                case 2:
                    play.setText("Resume");
                    break;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
            isBound = false;
        }
    };

    public void endback(String id) {
        if(this.id.equals(id)) {
            Drawable res = getResources().getDrawable(R.drawable.hokie);
            image.setImageDrawable(res);
        }
    }

    public void startBack(int current,String id) {
        this.id = id;

        if(current == 3){
            image.setImageResource(R.drawable.cheering);
        }
        if(current == 4){
            image.setImageResource(R.drawable.clapping);
        }
        if(current == 5){
            image.setImageResource(R.drawable.lestgohokies);
        }
    }
}