package com.cvrahimis.costasv.icope.MusicCode;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.cvrahimis.costasv.icope.ICopeActivity;
import com.cvrahimis.costasv.icope.ICopePatDB;
import com.cvrahimis.costasv.icope.MenuActitvity.MenuActivity;
import com.cvrahimis.costasv.icope.MyApplication;
import com.cvrahimis.costasv.icope.R;
import com.cvrahimis.costasv.icope.RatingScreenCode.RatingScreenActivity;

import android.net.Uri;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.Toast;


public class MusicActivity extends ActionBarActivity implements MediaPlayerControl {

    //song list variables
    private ArrayList<Song> songList;
    private ListView songView;
    //service
    private MusicService musicSrv;
    private Intent playIntent;
    //binding
    private boolean musicBound=false;

    //controller
    private MusicController controller;

    //activity and playback pause flags
    private boolean paused=false, playbackPaused=false;

    private ICopeActivity activity;
    private TimerTask mTimerTask;
    private int seconds = 0;
    ICopePatDB patDB;
    final Handler handler = new Handler();

    Timer t = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        startTimerTask();

        SimpleDateFormat sdf = new SimpleDateFormat("H");
        int hour = Integer.parseInt(sdf.format(new Date()));

        sdf = new SimpleDateFormat("MM/d/yyyy h:m");
        String str = sdf.format(new Date());

        if(hour > 12)
            str = str +"pm";
        else
            str = str + "am";
        activity = new ICopeActivity("Music", str);

        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.background);
        Drawable d;

        sdf = new SimpleDateFormat("HH");
        str = sdf.format(new Date());


        hour = Integer.parseInt(str);
        if(hour >= 12 && hour < 18)
        {
            d = getResources().getDrawable(R.drawable.afternoon);
            mainLayout.setBackground(d);
        }
        else if (hour > 0 && hour <= 24)
        {
            d = getResources().getDrawable(R.drawable.evening);
            mainLayout.setBackground(d);
        }
        else
        {
            d = getResources().getDrawable(R.drawable.morning);
            mainLayout.setBackground(d);
        }

        //retrieve list view
        songView = (ListView)findViewById(R.id.song_list);
        //instantiate list
        songList = new ArrayList<Song>();
        //get songs from device
        getSongList();
        //sort alphabetically by title
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });
        //create and set adapter
        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);

        //setup controller
        setController();
    }

    @Override
    public void onBackPressed() { exitLogic(); }

    private void CreateMenu(Menu menu)
    {
        MenuItem itm1 = (MenuItem) findViewById(R.id.action_shuffle);
        {
            //itm1.setIcon(R.drawable.add);
            itm1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        MenuItem itm2 = (MenuItem) findViewById(R.id.action_end);
        {
            //itm2.setIcon(R.drawable.del);
            itm2.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_music, menu);
        MenuItem itm1 = menu.add(0, 0, 0, "ADD");
        {
            itm1.setTitle("Done");
            itm1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //menu item selected
        switch (item.getItemId()) {
            case R.id.action_shuffle:
            {
                musicSrv.setShuffle();
                break;
            }
            case R.id.action_end:
            {
                stopService(playIntent);
                musicSrv = null;
                System.exit(0);
                break;
            }
            case 0:
            {
                exitLogic();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //start and bind the service when the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    //user song select
    public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //method to retrieve song info from device
    public void getSongList(){
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    //set the controller up
    private void setController(){
        controller = new MusicController(this);
        //set previous and next button listeners
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        //set and show
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }

    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        unbindService(musicConnection);
        musicSrv=null;
        super.onDestroy();
    }

    public void exitLogic(){
        //Toast.makeText(getApplicationContext(), "Back Button Pressed", Toast.LENGTH_SHORT).show();

        stopTask();
        int hours = seconds / 3600;
        seconds = seconds - (3600 * hours);
        int min = seconds / 60;
        seconds = seconds - (60 * min);
        activity.setActivityDuration(hours + " hours " + min + " minuets " + seconds + " seconds");
        ((MyApplication) this.getApplication()).push(activity);
        Intent intent = new Intent(this, RatingScreenActivity.class);
        finish();
        startActivity(intent);
    }

    public void startTimerTask(){

        mTimerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        seconds++;
                    }
                });
            }};

        // public void schedule (TimerTask task, long delay, long period)
        t.schedule(mTimerTask, 0, 1000);  //

    }

    public void stopTask(){

        if(mTimerTask!=null){
            mTimerTask.cancel();
        }

    }

}