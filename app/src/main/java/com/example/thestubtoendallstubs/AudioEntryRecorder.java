package com.example.thestubtoendallstubs;

import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import java.io.IOException;
import java.util.UUID;

/**
 * Handles MediaRecorder of the associated Entry's audio file.
 */
public class AudioEntryRecorder {
    // Contains UI elements
    private CardView cv;
    // Button used to record and finish MediaRecorder
    private ImageButton recordButton;
    // Used to display the time of the playback
    private TextView seconds;
    // MediaRecorder object
    private MediaRecorder mediaRecorder;
    // Determines whether recording or not
    private boolean recording = false;
    // Save path used to identify audio file
    private String audioSavePath;
    // Entry of the associated audio file
    private Entry entry;
    // Timer used to count seconds of playback
    private CountDownTimer timer;
    // Maximum duration afforded for recording
    private static final int maxSeconds = 60;
    // Indicated current second of played audio
    private int currentSecond = 0;
    // Externally implemented interface for event handling
    private eventsInterface events;
    private String audioId;

    /**
     * Creates an AudioEntryRecorder object with itemView, entry. Used to control MediaRecorder object
     * for the audio of an entry.
     * @param itemView
     * @param entry
     */
    public AudioEntryRecorder(@NonNull View itemView, Entry entry) {
        // Set UI elements to associated variables
        cv = itemView.findViewById(R.id.content_record_view);
        recordButton = itemView.findViewById(R.id.btn_record);
        seconds = itemView.findViewById(R.id.txt_seconds);
        // Set associated entry and audio file path
        this.entry = entry;
        // Set path to audio file to global audio file path
        this.audioSavePath = JController.getAudioFilePath();
        // Creating new unique ID
        audioId = UUID.randomUUID().toString() + ".gp3";
    }

    /**
     * Setup media recorder:
     * - Audio Source + OutputFormat + Encoder
     * - Output file
     * - Max duration
     */
    private void setMediaRecorder() {
        // Instantiate a new MediaRecorder object
        mediaRecorder = new MediaRecorder();
        // Set file save path to external files dir + unique ID of recorded file
        audioSavePath += "/" + audioId;
        // Set AudioSource to Microphone.
        // Set Output format + encoder (.gp3 extension)
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // Set output file
        mediaRecorder.setOutputFile(audioSavePath);
        // Max duration (in milliseconds)
        mediaRecorder.setMaxDuration(maxSeconds * 1000);
        try { // Risk of IOException
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setSeconds("0");
    }

    /**
     * Interface used to import external code into button clicked interface.
     */
    public interface eventsInterface {
        void setRecordOn();
        void setRecordOff();
    }

    /**
     * Set the OnClick event for record button.
     * Used for recorder and end recording for the media player.
     * Takes an external implementation of an interface.
     * @param events
     */
    public void setButtonEvents(eventsInterface events) {
        this.events = events;
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if recording or not
                if (!recording) { // If not recording
                    // Execute external code
                    events.setRecordOn();
                    // Set up the media recorder
                    setMediaRecorder();
                    // Start recorder
                    mediaRecorder.start();
                    // Create a new CountDownTimer
                    setTimer();
                    // Start Timer
                    timer.start();
                    // Set recording boolean
                    recording = true;
                }
                else { // If recording
                    // Creates a new unique id for the audio file to be recorded
                    entry.setAudioId(audioId);
                    finishRecording();
                }
            }
        });
    }

    /**
     * Sets the seconds of the UI element according to String argument.
     * @param currentSecond
     */
    private void setSeconds(String currentSecond) {
        // Maximum duration for audio file
        String duration = String.valueOf(maxSeconds);
        // Set UI element to parameter + formatted duration
        seconds.setText(currentSecond + "/" + duration + "s");
    }

    /**
     * Creates a CountDownTimer object to increment the text of the Seconds counter to current seconds during recording.
     */
    private void setTimer() {
        // Duration of maximum recording (formatted to milliseconds)
        int duration = maxSeconds * 1000;
        // Create new CountDownTimer object that changes UI seconds text every second
        timer = new CountDownTimer(duration - (currentSecond * 1000), 1000){
            public void onTick(long millisUntilFinished){
                setSeconds(String.valueOf(currentSecond));
                currentSecond++;
            }
            public void onFinish(){
                finishRecording();
            }
        };
    }

    /**
     * Finish the recoding and execute external code.
     */
    private void finishRecording() {
        // Stop recorder and store audio file
        mediaRecorder.stop();
        mediaRecorder.release();
        // Not recording
        recording = false;
        currentSecond = 0;
        // Cancel timer
        timer.cancel();
        // Execute external code
        events.setRecordOff();
    }
}
