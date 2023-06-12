package com.example.thestubtoendallstubs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.File;

public class EntryContentActivity extends AppCompatActivity {

    private EditText txtContent;
    private LinearLayout layAudioContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_content);
        setElements();
    }

    @Override
    public void finish() {
        super.finish();
        String content = txtContent.getText().toString();
        JController.getCurrentEntry().setContent(content);
        JController.insertEntry(JController.getCurrentUser(), JController.getCurrentJournal(), JController.getCurrentEntry());
        System.out.println(content);
    }

    private void setElements() {
        txtContent = findViewById(R.id.txt_entry_content);
        layAudioContainer = findViewById(R.id.lay_audio_container);

        String content = JController.getCurrentEntry().getContent();
        txtContent.setText(content);

        setUpAudio();
        setEvents();
    }

    private void setUpAudio() {
        setPermissions();
        if (JController.getCurrentEntry().getAudioId() == null) {
            createRecorder();
        }
        else {
            createPlayback();
        }
    }

    private void createRecorder() {
        clearAudioView();
        View recorder = getLayoutInflater().inflate(R.layout.audio_recorder, null);
        layAudioContainer.addView(recorder);
        AudioEntryRecorder entryRecorder = new AudioEntryRecorder(recorder, JController.getCurrentEntry());
        entryRecorder.setButtonEvents(new AudioEntryRecorder.eventsInterface() {
            @Override
            public void setRecordOn() {

            }

            @Override
            public void setRecordOff() {
                createPlayback();
            }
        });
    }

    private void createPlayback() {
        clearAudioView();
        View playback = getLayoutInflater().inflate(R.layout.audio_playback, null);
        layAudioContainer.addView(playback);
        AudioEntryPlayback entryPlayback = new AudioEntryPlayback(playback, JController.getCurrentEntry(),
                new AudioEntryPlayback.eventsInterface() {
            @Override
            public void setPlayOn() {

            }

            @Override
            public void setPlayOff() {

            }

            @Override
            public void deleteAudio() {
                JController.getCurrentEntry().setAudioId(null);
                String audioSavePath = JController.getAudioFilePath() + "/" + JController.getCurrentEntry().getAudioId();
                File file = new File(audioSavePath);
                if(file.delete()) {
                    System.out.println("File has been deleted at: " + audioSavePath);
                }
                createRecorder();
            }

            @Override
            public void audioError() {
                createRecorder();
            }

        });
        entryPlayback.setButtonEvents();
    }

    private void clearAudioView() {
        layAudioContainer.removeAllViews();
    }

    private void setEvents() {
        txtContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });
    }

    private void setPermissions() {
        if (!checkPermissions()) {
            ActivityCompat.requestPermissions(EntryContentActivity.this, new String[] {
                    android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private boolean checkPermissions() {
        int first = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.RECORD_AUDIO);
        int second = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return first == getApplicationContext().getPackageManager().PERMISSION_GRANTED &&
                second == getApplicationContext().getPackageManager().PERMISSION_GRANTED;
    }
}