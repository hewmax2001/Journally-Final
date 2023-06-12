package com.example.thestubtoendallstubs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stubFunction();
    }

    private void stubFunction() {
        JController.start(getApplicationContext());
        String username = "test";
        String password = "test";
        //register(username, password);

        JController.getUserByUsername(new Callback() {
            @Override
            public void onCallback(DataSnapshot snap) {
                User user = snap.getValue(User.class);
                login(user);
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "This user does not exist.", Toast.LENGTH_SHORT).show();
            }
        }, username, password);
    }

    private void login(User user) {
        JController.setCurrentUser(user);
        Toast.makeText(getApplicationContext(), user.getId(), Toast.LENGTH_SHORT).show();
        Intent loginIntent = new Intent(this, UserMenuActivity.class);
        startActivity(loginIntent);
    }

    private void register(String username, String password) {
        JController.insertUser(new Callback() {
            @Override
            public void onCallback(DataSnapshot snap) {
                Toast.makeText(getApplicationContext(), "User has been registered successfully.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "User with the same name and password already exists!", Toast.LENGTH_SHORT).show();
            }
        }, username, password);
    }
}