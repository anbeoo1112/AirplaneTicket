package com.example.airplaneticket;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
            startActivity(new Intent(this, MainActivity.class));
        });
        btnLogin = (Button) findViewById(R.id.button2);
        btnLogin.setOnClickListener(v -> {
            // Handle button click
            startActivity(new Intent(this, LoginActivity.class));
        });


    }
}