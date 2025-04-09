package com.example.airplaneticket.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.airplaneticket.Model.Seat;
import com.example.airplaneticket.R;
import com.example.airplaneticket.databinding.SeatItemBinding;

import android.content.Context;


import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<Seat> seatList;
    private final Context context;
    private ArrayList<String> selectedSeatName = new ArrayList<>();
    private SelectedSeat selectedSeat;
    // Define your adapter properties and methods here

    @NonNull
    @Override
    public SeatAdapter.SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SeatItemBinding binding = SeatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SeatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SeatViewHolder holder, int position) {
        Seat seat = seatList.get(position);
        holder.binding.SeatImageVIew.setText(seat.getName());

        switch ( seat.getStatus()) {
            case AVAILABLE:
                holder.binding.SeatImageVIew.setBackgroundResource(R.drawable.ic_seat_avaiable);
                holder.binding.SeatImageVIew.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case SELECTED:
                holder.binding.SeatImageVIew.setBackgroundResource(R.drawable.ic_seat_selected);
                holder.binding.SeatImageVIew.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case UNAVAILABLE:
                holder.binding.SeatImageVIew.setBackgroundResource(R.drawable.ic_seat_unavaiable);
                holder.binding.SeatImageVIew.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case EMPTY:
                holder.binding.SeatImageVIew.setBackgroundResource(R.drawable.ic_seat_empty);
                holder.binding.SeatImageVIew.setTextColor(context.getResources().getColor(R.color.white));
                break;
        }

        holder.binding.SeatImageVIew.setOnClickListener((View.OnClickListener) view -> {
            if (seat.getStatus() == Seat.SeatStatus.AVAILABLE) {
                seat.setStatus(Seat.SeatStatus.SELECTED);
                selectedSeatName.add(seat.getName());
                notifyItemChanged(position);
            } else if (seat.getStatus() == Seat.SeatStatus.SELECTED) {
                seat.setStatus(Seat.SeatStatus.AVAILABLE);
                selectedSeatName.remove(seat.getName());
                notifyItemChanged(position);
            }

            String selected = selectedSeatName.toString().replace("[", "").replace("]", "").replace(" ", "");

            selectedSeat.Return(selected, selectedSeatName.size());
        });
    }

    public SeatAdapter(SelectedSeat selectedSeat, Context context, List<Seat> seatList) {
        this.selectedSeat = selectedSeat;
        this.context = context;
        this.seatList = seatList;
    }

    @Override
    public int getItemCount() {
        // Return the total number of items in the adapter
        return seatList.size();
    }

    public class SeatViewHolder extends RecyclerView.ViewHolder {
        SeatItemBinding binding;
        public SeatViewHolder(@NonNull SeatItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface  SelectedSeat{
        void Return(String selectedName,int num);
    }
}
