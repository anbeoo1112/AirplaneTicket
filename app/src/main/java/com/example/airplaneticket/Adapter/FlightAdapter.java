package com.example.airplaneticket.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.airplaneticket.Model.Flight;
import com.example.airplaneticket.Activity.SeatListActivity;
import com.example.airplaneticket.databinding.ViewholderFlightBinding;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.ViewHolder> {
    private final ArrayList<Flight> flights;
    private Context context;

    public FlightAdapter(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    @NonNull
    @Override
    public FlightAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FlightAdapter.ViewHolder holder, int position) {
        Flight flight = flights.get(position);
        Glide.with(context)
                .load(flight.getAirlineLogo())
                .into(holder.binding.ivFlightLogo);

        holder.binding.tvFlightFrom.setText(flight.getFrom());
        holder.binding.tvFlightFromShort.setText(flight.getFromShort());
        holder.binding.tvFlightTo.setText(flight.getTo());
        holder.binding.tvFlightToShort.setText(flight.getToShort());
        holder.binding.tvFlightTime.setText(flight.getArriveTime());
        holder.binding.tvFlightClass.setText(flight.getClassSeat());
        holder.binding.tvFlightPrice.setText("$" + flight.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SeatListActivity.class);
                intent.putExtra("flight", flight);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;
        public ViewHolder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
