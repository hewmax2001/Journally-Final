package com.example.thestubtoendallstubs;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.io.File;
import java.io.IOException;

/**
 * Handles MediaPlayer of the associated Entry's audio file.
 */
public class AudioEntryPlayback {
    // Contains UI elements
    private CardView cv;
    // Button used to play and pause MediaPlayer
    private ImageButton playButton;
    // Button used to delete audio file
    private ImageButton deleteButton;
    private View itemView;
    // Used to display the time of the playback
    private TextView seconds;
    // Entry of the associated audio file
    private Entry entry;
    // Media player object
    private MediaPlayer mediaPlayer;
    // Save path used to identify audio file
    private String audioSavePath;
    // Timer used to count seconds of playback
    private CountDownTimer timer;
    // Indicated current second of played audio
    private int currentSecond = 0;
    private eventsInterface events;

    /**
     * Creates an AudioEntryPlayback object with itemView, entry. Used to control MediaPlayer object
     * for the audio of an entry.
     * @param itemView
     * @param entry
     */
    public AudioEntryPlayback(@NonNull View itemView, Entry entry, eventsInterface events) {
        this.itemView = itemView;
        // Set UI elements to associated variables
        cv = itemView.findViewById(R.id.content_playback_view);
        playButton = itemView.findViewById(R.id.btn_playback);
        deleteButton = itemView.findViewById(R.id.btn_delete);
        seconds = itemView.findViewById(R.id.txt_seconds);
        // Set associated entry and audio file path
        this.entry = entry;
        this.events = events;
        // Set path to audio file to global audio file path + id of audio file
        this.audioSavePath = JController.getAudioFilePath() + "/" + entry.getAudioId();
        // Set up the media player
        setMediaPlayer();
    }

    /**
     * Setup media player:
     * - Data source
     * - Current second
     * - OnCompletionListener
     */
    private void setMediaPlayer() {
        // Instantiate a new MediaPlayer object
        mediaPlayer = new MediaPlayer();
        try { // Risk of IOException
            // Set data source
            mediaPlayer.setDataSource(audioSavePath);
            // Prepare and set current seconds to 0
            mediaPlayer.prepare();
            setSeconds("0");
        } catch (IOException e) {
            events.audioError();
            throw new RuntimeException(e);
        }
        setTimer();
        // On completion of media player, rewind player to beginning of audio.
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopMediaPlayer();
                currentSecond = 0;
            }
        });
    }

    /**
     * Interface used to import external code into button clicked interface.
     */
    public interface eventsInterface {
        void setPlayOn();
        void setPlayOff();
        void deleteAudio();
        void audioError();
    }

    /**
     * Set the OnClick event for playback button.
     * Used for playing and pausing the media player.
     * Takes an external implementation of an interface.
     */
    public void setButtonEvents() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if mediaPlayer is playing or is paused
                if (!mediaPlayer.isPlaying()) { // If paused
                    // Start the media player
                    mediaPlayer.start();
                    // Create a new CountDownTimer
                    setTimer();
                    // Start timer
                    timer.start();
                    // Execute externally imported code
                    events.setPlayOn();
                    // Set playback button to a pause symbol
                    Drawable pauseD = itemView.getContext().getDrawable(android.R.drawable.ic_media_pause); // Getting pause drawable
                    playButton.setImageDrawable(pauseD);
                }
                else { // If playing
                    events.setPlayOff();
                    stopMediaPlayer();
                    timer.cancel();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMediaPlayer();
                timer.cancel();
                timer = null;
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
                events.deleteAudio();
            }
        });
    }

    /**
     * Pause the media player.
     * Sets button to pause symbol.
     */
    private void stopMediaPlayer() {
        // Check if mediaPlayer exists
        if (mediaPlayer != null) {
            // Pause media player
            mediaPlayer.pause();
            // Get play button drawable
            Drawable playD = itemView.getContext().getDrawable(android.R.drawable.ic_media_play);
            // Set playback button to play drawable image
            playButton.setImageDrawable(playD);
        }
    }

    /**
     * Sets the seconds of the UI element according to String argument.
     * @param currentSecond
     */
    private void setSeconds(String currentSecond) {
        // Duration of entire audio file
        String duration = String.valueOf(mediaPlayer.getDuration() / 1000); // Durations returns as milliseconds
        // Set UI element to parameter + formatted duration
        seconds.setText(currentSecond + "/" + duration + "s");
    }

    /**
     * Creates a CountDownTimer object to increment the text of the Seconds counter to current seconds during playback.
     * Timer counts down to duration of audio file from the current second of played audio;
     * Allows pause functionality
     */
    private void setTimer() {
        // Duration of entire audio file
        int duration = mediaPlayer.getDuration();
        // Create new CountDownTimer object that changes UI seconds text every second
        timer = new CountDownTimer(duration - (currentSecond * 1000), 1000){
            public void onTick(long millisUntilFinished){
                setSeconds(String.valueOf(currentSecond));
                currentSecond++;
            }
            public  void onFinish(){

            }
        };
    }
}
