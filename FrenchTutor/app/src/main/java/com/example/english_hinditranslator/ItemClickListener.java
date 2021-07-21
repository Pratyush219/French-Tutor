package com.example.english_hinditranslator;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ItemClickListener  implements AdapterView.OnItemClickListener, AudioManager.OnAudioFocusChangeListener, MediaPlayer.OnCompletionListener {
    AudioManager mAudioManager;
    AudioFocusRequest mFocusRequest;
    AudioAttributes mAttributes;
    MediaPlayer mediaPlayer;
    int res;
    boolean resumeOnFocusGain = false;
    boolean playbackDelayed = false;
    View v;
    ImageView playButton;
    ImageView pauseButton;
    Context context;
    ArrayList<Word> words;
    final Object objectLock = new Object();

    public ItemClickListener(Context context, ArrayList<Word> words) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.context = context;
        this.words = words;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build();
            mFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(mAttributes)
                    .setAcceptsDelayedFocusGain(true)
                    .setOnAudioFocusChangeListener(this)
                    .setWillPauseWhenDucked(true)
                    .build();

        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        resetMediaPlaybackSettings();
        releaseMediaPlayer(v);
        abandonAudioFocusForMedia();
        v = null;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                if(playbackDelayed || resumeOnFocusGain) {
                    synchronized (objectLock) {
                        resetMediaPlaybackSettings();
                    }
                    startPlayback();
                    mediaPlayer.setOnCompletionListener(this);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                synchronized (objectLock) {
                    resetMediaPlaybackSettings();
                    releaseMediaPlayer(v);
                }
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                synchronized (objectLock) {
                    playbackDelayed = false;
                    resumeOnFocusGain = mediaPlayer != null && mediaPlayer.isPlaying();
                    releaseMediaPlayer(v);
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Word currentWord = words.get(position);
        playButton = view.findViewById(R.id.play);
        pauseButton = view.findViewById(R.id.pause);
        if(mediaPlayer != null && mediaPlayer.isPlaying() && v == view){
            pausePlayback(view);
            abandonAudioFocusForMedia();
            resetMediaPlaybackSettings();
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                res = mAudioManager.requestAudioFocus(mFocusRequest);
            else
                res = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            Log.v("request value","returned " + res);
            synchronized (objectLock) {
                switch (res) {
                    case AudioManager.AUDIOFOCUS_REQUEST_GRANTED:
                        playbackDelayed = false;
                        if(mediaPlayer == null || view !=v) {
                            releaseMediaPlayer(v);
                            mediaPlayer = MediaPlayer.create(context, currentWord.getMmediaResource());
                        }
                        startPlayback();
                        v = view;
                        Log.v("Focus request","Granted");
                        break;
                    case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                        playbackDelayed = false;
                        Log.v("Focus request","Failed");
                        break;
                    case AudioManager.AUDIOFOCUS_REQUEST_DELAYED:
                        playbackDelayed = mediaPlayer != null && mediaPlayer.isPlaying();
                        Log.v("Playback delayed"," "+playbackDelayed);
                        break;
                    default:
                        Log.v("Focus request","Something else happened");
                }
            }
        }
        if(mediaPlayer != null) mediaPlayer.setOnCompletionListener(this);
    }

    private void resetMediaPlaybackSettings(){
        playbackDelayed = false;
        resumeOnFocusGain = false;
    }
    private void abandonAudioFocusForMedia(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)mAudioManager.abandonAudioFocusRequest(mFocusRequest);
        else mAudioManager.abandonAudioFocus(this);
    }
    private void pausePlayback(View view){
        if(mediaPlayer != null) mediaPlayer.pause();
        view.findViewById(R.id.pause).setVisibility(View.GONE);
        view.findViewById(R.id.play).setVisibility(View.VISIBLE);
    }
    private void startPlayback(){
        if(mediaPlayer != null) {
            mediaPlayer.start();
            playButton.setVisibility(View.GONE);
            pauseButton.setVisibility(View.VISIBLE);
        }
    }
    void releaseMediaPlayer(View view) {
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(view != null) {
            view.findViewById(R.id.pause).setVisibility(View.GONE);
            view.findViewById(R.id.play).setVisibility(View.VISIBLE);
        }
    }
}
