package com.example.airplaneticket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.airplaneticket.R;
import com.example.airplaneticket.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // User not logged in, redirect to LoginActivity
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize Firebase Database reference
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

        // Set up UI
        setupUI();
        loadUserData();
        setupListeners();
    }

    private void setupUI() {
        // Set up bottom navigation (to maintain consistent navigation)
        ChipNavigationBar navigationBar = findViewById(R.id.bottomNavigation);
        navigationBar.setItemSelected(R.id.profile, true);
        if (navigationBar != null) {

            navigationBar.setOnItemSelectedListener(id -> {
                if (id == R.id.home) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    // Add this after startActivity() calls in both activities
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else if (id == R.id.explore) {
                    Log.d("MainActivity", "Explore clicked");
                    // Implement explore functionality
                } else if (id == R.id.bookmark) {
                    Log.d("MainActivity", "Bookmark clicked");
                    // Implement bookmark functionality
                } else if (id == R.id.profile) {
                    Log.d("MainActivity", "Profile clicked");

                }
            });
        } else {
            Log.e("MainActivity", "Bottom navigation view not found");
        }
    }

    private void loadUserData() {
        binding.tvEmail.setText(currentUser.getEmail());

        // Try to get username or display name
        if (currentUser.getDisplayName() != null && !currentUser.getDisplayName().isEmpty()) {
            binding.tvUsername.setText(currentUser.getDisplayName());
        } else {
            // If no display name, try to get from database
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.hasChild("name")) {
                            String name = snapshot.child("name").getValue(String.class);
                            binding.tvUsername.setText(name);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ProfileActivity.this,
                            "Failed to load user data: " + error.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setupListeners() {
        binding.btnEditProfile.setOnClickListener(v -> {
            // Launch edit profile activity (to be implemented)
            Toast.makeText(this, "Edit profile functionality coming soon", Toast.LENGTH_SHORT).show();
        });

        binding.btnLogout.setOnClickListener(v -> {
            // Sign out from Firebase
            mAuth.signOut();

            // Return to login screen
            startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
            finish();
        });

        binding.ivBack.setOnClickListener(v -> finish());
    }
}