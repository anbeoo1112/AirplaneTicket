package com.example.airplaneticket.Activity;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.airplaneticket.R;
import com.example.airplaneticket.databinding.ActivityIntroBinding;

public class IntroActivity extends BaseActivity {
    private Button btnLogin;
    private ActivityIntroBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(v -> {
            // Handle button click
            startActivity(new Intent(this, RegisterActivity.class));
        });
        btnLogin = (Button) findViewById(R.id.button2);
        btnLogin.setOnClickListener(v -> {
            // Handle button click
            startActivity(new Intent(this, LoginActivity.class));
        });


    }
}