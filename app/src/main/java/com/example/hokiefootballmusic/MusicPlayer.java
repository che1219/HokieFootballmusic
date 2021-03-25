package com.example.hokiefootballmusic;



    import android.media.MediaPlayer;
    import android.os.AsyncTask;

    import java.util.ArrayList;

public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MediaPlayer player, backA,backB,backC;
    MyAsyncTask myAsyncTask;
    int currentPosition = 0;
    int currentbackAPosition = 0;
    int currentbackBPosition = 0;
    int currentbackCPosition = 0;
    int musicIndex = 0;
    int count = 0;
    int backIndex = 0;
    int songlen = 0;
    private int musicStatus = 0;//0: before playing, 1 playing, 2 paused
    private MusicService musicService;
    ArrayList<Integer> backMusic,backtime;

    static final int[] MAINMUSICPATH = new int[]{
            R.raw.gotechgo,
            R.raw.greatdaytobeahokie,
            R.raw.hokiesfightsong,
            R.raw.cheering,
            R.raw.clapping,
            R.raw.lestgohokies
    };

    static final String[] MUSICNAME = new String[]{
            "Go Tech Go",
            "Great Day To Be A Hokie",
            "Hokies Fight Song"
    };


    public MusicPlayer(MusicService service) {

        this.musicService = service;
    }


    public int getMusicStatus() {

        return musicStatus;
    }

    public String getMusicName() {

        return MUSICNAME[musicIndex];
    }

    public void playMusic(int number, ArrayList<Integer> backMusic,ArrayList<Integer> backtime) {
        this.backMusic = backMusic;
        this.backtime = backtime;
        musicIndex = number;
        player = MediaPlayer.create(this.musicService, MAINMUSICPATH[musicIndex]);
        player.start();
        player.setOnCompletionListener(this);
        musicService.onUpdateMusicName(getMusicName());
        musicStatus = 1;
        myAsyncTask = new MyAsyncTask();
        switch(number){
            case 0:
                songlen = 49;
                break;
            case 1:
                songlen = 3*60+20;
                break;
            case 2:
                songlen =2*60+13;
                break;
        }
        myAsyncTask.execute(songlen + 1);
    }

    public void playBackAMusic(int number) {
        backA = MediaPlayer.create(this.musicService, MAINMUSICPATH[number]);
        backA.start();
        backA.setOnCompletionListener(this);
        musicService.startBack(number,"A");
    }
    public void playBackBMusic(int number) {
        backB = MediaPlayer.create(this.musicService, MAINMUSICPATH[number]);
        backB.start();
        backB.setOnCompletionListener(this);
        musicService.startBack(number,"B");
    }
    public void playBackCMusic(int number) {
        backC = MediaPlayer.create(this.musicService, MAINMUSICPATH[number]);
        backC.start();
        backC.setOnCompletionListener(this);
        musicService.startBack(number,"C");
    }

    public void pauseMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
            currentPosition = player.getCurrentPosition();
            musicStatus = 2;
            myAsyncTask.cancel(true);
        }
        if (backA != null && backA.isPlaying()) {
            backA.pause();
            currentbackAPosition = backA.getCurrentPosition();
        }
        if (backB != null && backB.isPlaying()) {
            backB.pause();
            currentbackBPosition = backB.getCurrentPosition();
        }
        if (backC != null && backC.isPlaying()) {
            backC.pause();
            currentbackAPosition = backC.getCurrentPosition();
        }
    }

    public void resumeMusic() {
        if (player != null) {
            player.seekTo(currentPosition);
            player.start();
            musicStatus = 1;
            myAsyncTask = new MyAsyncTask();
            myAsyncTask.execute(songlen + 1);
        }
        if (backA != null) {
            backA.seekTo(currentbackAPosition);
            backA.start();
        }
        if (backB != null) {
            backB.seekTo(currentbackBPosition);
            backB.start();
        }
        if (backC != null) {
            backC.seekTo(currentbackCPosition);
            backC.start();
        }
    }

    public void stopPlay() {
        if (player != null) {
            player.stop();
            musicStatus = 0;
        }
        if (backA != null) {
            backA.stop();
        }
        if (backB != null) {
            backB.stop();
        }
        if (backC != null) {
            backC.stop();
        }
        count = 0;
        myAsyncTask.cancel(true);
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.equals(player)) {
            player.release();
            player = null;
            count = 0;
        }
        if (mediaPlayer.equals(backA)) {
            backA.release();
            backA = null;
            musicService.endBack("A");
        }
        if (mediaPlayer.equals(backB)) {
            backB.release();
            backB = null;
            musicService.endBack("B");
        }
        if (mediaPlayer.equals(backC)) {
            backC.release();
            backC = null;
            musicService.endBack("C");
        }
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            while (count < params[0]) {
                try {
                    //checking if the asynctask has been cancelled, end loop if so
                    if (isCancelled()) break;

                    Thread.sleep(1000);

                    count++;
                    if(backMusic.size() != 0) {
                        for(int i = 0; i < backMusic.size();i++){
                            if(backtime.get(i) == 0){
                                if(backMusic.size() == 3){
                                    playBackAMusic(backMusic.get(i));
                                }
                                if(backMusic.size() == 2){
                                    playBackBMusic(backMusic.get(i));
                                }
                                if(backMusic.size() == 1){
                                    playBackCMusic(backMusic.get(i));
                                }
                                backtime.remove(i);
                                backMusic.remove(i);
                            }
                            else if(songlen * backtime.get(backIndex)/100 <= count){
                                if(backMusic.size() == 3){
                                    playBackAMusic(backMusic.get(i));
                                }
                                if(backMusic.size() == 2){
                                    playBackBMusic(backMusic.get(i));
                                }
                                if(backMusic.size() == 1){
                                    playBackCMusic(backMusic.get(i));
                                }
                                backtime.remove(i);
                                backMusic.remove(i);
                            }
                        }
                    }

                    //send count to onProgressUpdate to update UI
                    publishProgress(count);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }

    }
}
