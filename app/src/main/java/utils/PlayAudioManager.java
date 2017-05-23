package utils;

import android.media.MediaPlayer;
import android.widget.ImageView;

import com.reformation.home.R;

import java.io.File;
import java.io.IOException;

import model.Audio;

/**
 * Created by muvi on 21/5/17.
 */

public class PlayAudioManager {

    private MediaPlayer mp;

    private PlayAudioManager(){
        mp = new MediaPlayer();
    }
    private static  PlayAudioManager _instance;
    public PlayAudioManager getInstance(){
        if(_instance==null){
            return new PlayAudioManager();
        }else{
            return _instance;
        }
    }

    public MediaPlayer getMediaPlayer(){
        return  mp;
    }


    /**
     * Function to play a song
     * */
    public void  playSong(String path,ImageView btnPlay,int imgSrc) {
        LogUtil.createLog("select lang",Constant.SELECTED_LANG );
        if (new File(path).exists()) {
            // Play song
            try {
                mp.reset();
                mp.setDataSource(path);
                mp.prepare();
                mp.start();
                btnPlay.setImageResource(imgSrc);
                // Displaying Song title
            } catch(IllegalArgumentException e){
                e.printStackTrace();
            } catch(IllegalStateException e){
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    public void  stopSong(MediaPlayer mp,ImageView btnPlay,int imgSrc) {
        LogUtil.createLog("select lang",Constant.SELECTED_LANG );
        if(mp!=null) {
            // check for already playing
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
                // Changing button image to play button
                btnPlay.setImageResource(imgSrc);

            }
        }
    }




    public void playPause(ImageView btnPlay,int imgSrc){
        if(mp!=null) {
            // check for already playing
            if (mp.isPlaying()) {

                mp.pause();
                // Changing button image to play button
                btnPlay.setImageResource(imgSrc);

            } else {
                // Resume song
                try {
                    // mp.prepare();
                   // mp.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Changing button image to pause button
                btnPlay.setImageResource(imgSrc);
            }
        }
    }



}
