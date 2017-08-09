package com.silver5302.kakaologin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RegistActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        Intent intent=getIntent();
        String name=intent.getStringExtra("name");

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }
}
