package com.example.airplaneticket;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.airplaneticket.Activity.BaseActivity;
import com.example.airplaneticket.Adapter.FlightAdapter;
import com.example.airplaneticket.Model.Flight;
import com.example.airplaneticket.databinding.ActivitySearch2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends BaseActivity {
    private ActivitySearch2Binding binding;
    private String from, to, date;
    private int numPassengers;
    private FirebaseDatabase database; // Added FirebaseDatabase instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearch2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance(); // Initialize FirebaseDatabase
        getIntentExtras();
        initList();
        setVariable();
    }

    private void setVariable() {
        binding.ivBackButton.setOnClickListener(v -> finish());
    }

    private void initList() {
        DatabaseReference myRef = database.getReference("Flights");
        ArrayList<Flight> list = new ArrayList<>();
        Query query = myRef.orderByChild("from").equalTo(from);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        Flight flight = issue.getValue(Flight.class);
                        if (flight != null && flight.getTo().equals(to)) {
                            list.add(flight);
                        }
                    }

                    if (!list.isEmpty()) {
                        binding.rvSearchResults.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                        binding.rvSearchResults.setAdapter(new FlightAdapter(list));
                    }

                    binding.progressBar2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar2.setVisibility(View.GONE);
            }
        });
    }

    private void getIntentExtras() {
        from = getIntent().getStringExtra("from");
        to = getIntent().getStringExtra("to");
        date = getIntent().getStringExtra("date");
        numPassengers = getIntent().getIntExtra("numPassengers", 1);
    }
}