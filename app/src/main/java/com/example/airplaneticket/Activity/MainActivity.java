package com.example.airplaneticket.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.airplaneticket.Model.Location;
import com.example.airplaneticket.R;
import com.example.airplaneticket.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends BaseActivity {
    //    "48:21 cua video roi nhe"
    private ActivityMainBinding binding;
    private int adultPassenger = 1, childPassenger = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("d MM, yyyy", Locale.ENGLISH);
    private Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initLocations();
        initPassengers();
        initClassSeat();
        initDatePicup();
        setVariable();
        setupBottomNavigation();
    }

    private void setVariable() {
        binding.btnSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            intent.putExtra("from", ((Location) binding.fromSp.getSelectedItem()).getName());
            intent.putExtra("to", ((Location) binding.toSp.getSelectedItem()).getName());
            intent.putExtra("date", binding.departureDate.getText().toString());
            intent.putExtra("numPassenger", adultPassenger + childPassenger);
            startActivity(intent);
        });
    }

    private void initDatePicup() {
        Calendar calendarToday = Calendar.getInstance();
        String currentDate = dateFormat.format(calendarToday.getTime());
        binding.departureDate.setText(currentDate);

        Calendar calendarTommorow = Calendar.getInstance();
        calendarTommorow.add(Calendar.DAY_OF_YEAR, 1);
        String tommorowDate = dateFormat.format(calendarTommorow.getTime());
        binding.returnDate.setText(tommorowDate);

        binding.departureDate.setOnClickListener(view -> ShowDatePickerDialog(binding.departureDate));
        binding.returnDate.setOnClickListener(view -> ShowDatePickerDialog(binding.returnDate));
    }

    private void initClassSeat() {
        binding.progressBarClass.setVisibility(View.VISIBLE);
        ArrayList<String> list = new ArrayList<>();
        list.add(" Business Class");
        list.add("First Class");
        list.add("Economy Class");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.classSp.setAdapter(adapter);
        binding.progressBarClass.setVisibility((View.GONE));
    }

    private void initPassengers() {
        binding.plusAdultBtn.setOnClickListener(v -> {
            adultPassenger++;
            binding.tvAdult.setText(adultPassenger + " Adult");
        });
        binding.minusAdultBtn.setOnClickListener(v -> {
            if (adultPassenger > 1) {
                adultPassenger--;
                binding.tvAdult.setText(adultPassenger + " Adult");
            }
        });

        binding.plusChildBtn.setOnClickListener(view -> {
            childPassenger++;
            binding.tvChild.setText(childPassenger + " Child");
        });
        binding.minusChildBtn.setOnClickListener(v -> {
            if (childPassenger > 0) {
                childPassenger--;
                binding.tvChild.setText(childPassenger + " Child");
            }
        });
    }

    private void initLocations() {
        binding.progressBarFrom.setVisibility(View.VISIBLE);
        binding.ProgressBarTo.setVisibility(View.VISIBLE);

        DatabaseReference myRef = database.getReference("Locations");

        ArrayList<Location> list = new ArrayList<>();

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        list.add(issue.getValue(Location.class));
                    }

                    ArrayAdapter<Location> adapter = new ArrayAdapter<>(MainActivity.this, R.layout.sp_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    binding.fromSp.setAdapter(adapter);
                    binding.toSp.setAdapter(adapter);
                    binding.fromSp.setSelection(1);

                    binding.progressBarFrom.setVisibility(View.GONE);
                    binding.ProgressBarTo.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
                Log.e("MainActivity", "Database error: " + error.getMessage());
            }
        });
    }

    private void ShowDatePickerDialog(TextView textView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            calendar.set(selectedYear, selectedMonth, selectedDay);
            String formattedDate = dateFormat.format(calendar.getTime());
            textView.setText(formattedDate);
        }, year, month, day);
        datePickerDialog.show();
    }

    private void setupBottomNavigation() {
        ChipNavigationBar navigationBar = findViewById(R.id.bottomNavigation);
        navigationBar.setItemSelected(R.id.home, true);
        if (navigationBar != null) {
            navigationBar.setItemSelected(R.id.home, true);

            navigationBar.setOnItemSelectedListener(id -> {
                if (id == R.id.home) {
                    return;
                } else if (id == R.id.explore) {
                    Log.d("MainActivity", "Explore clicked");
                    // Implement explore functionality
                } else if (id == R.id.bookmark) {
                    Log.d("MainActivity", "Bookmark clicked");
                    // Implement bookmark functionality
                } else if (id == R.id.profile) {
                    Log.d("MainActivity", "Profile clicked");
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    // Add this after startActivity() calls in both activities
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        } else {
            Log.e("MainActivity", "Bottom navigation view not found");
        }
    }
}