package com.example.airplaneticket.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.example.airplaneticket.Adapter.SeatAdapter;
import com.example.airplaneticket.Model.Flight;
import com.example.airplaneticket.Model.Seat;
import com.example.airplaneticket.databinding.ActivitySeatListBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatListActivity extends BaseActivity {
    private ActivitySeatListBinding binding;
    private Flight flight;
    private Double price = 0.0;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeatListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        initSeatList();
        setVariable();
    }

    private void setVariable() {
        binding.ivBack.setOnClickListener(v -> finish());
        binding.btnConfirmSeats.setOnClickListener(v -> {
            if(num > 0){
                flight.setPassenger(binding.nameSelectedSeat.getText().toString());
                flight.setPrice(price);
                Intent intent = new Intent(SeatListActivity.this, TicketDetailActivity.class);
                intent.putExtra("flight", flight);
                startActivity(intent);
            }else{
                Toast.makeText(SeatListActivity.this,"Please select your seat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }
    private void initSeatList() {
        GridLayoutManager gridlayoutManager = new GridLayoutManager(this, 7);
        gridlayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position % 7 == 3) ? 1 : 1;
            }
        });

        binding.rvSeatList.setLayoutManager(gridlayoutManager);

        List<Seat> seatList = new ArrayList<>();
        int row =0;
        int numberSeat = flight.getNumberSeat()+(flight.getNumberSeat()/7) + 1;

        Map<Integer, String> seatAlphabetMap = new HashMap<>();
        seatAlphabetMap.put(0,"A");
        seatAlphabetMap.put(1,"B");
        seatAlphabetMap.put(2,"C");
        seatAlphabetMap.put(4,"D");
        seatAlphabetMap.put(5,"E");
        seatAlphabetMap.put(6,"F");

        for (int i=0;i<numberSeat;i++){
            if (i%7==0){
                row++;
            }
            if (i%7==3){
                seatList.add(new Seat(Seat.SeatStatus.EMPTY,String.valueOf(row)));
            }else {
                String seatName = seatAlphabetMap.get(i%7) + (row);
                Seat.SeatStatus status = flight.getReservedSeats().contains(seatName) ? Seat.SeatStatus.UNAVAILABLE : Seat.SeatStatus.AVAILABLE;
                seatList.add(new Seat(status,seatName));
            }
        }

        SeatAdapter seatAdapter = new SeatAdapter(
                new SeatAdapter.SelectedSeat() {
                    @Override
                    public void Return(String selectedSeat, int num) {
                        binding.tvSeatCount.setText(num + " Seat Selected ");
                        binding.nameSelectedSeat.setText(selectedSeat);
                        DecimalFormat df = new DecimalFormat("#,##0.00");
                        String formattedPrice = df.format(num * flight.getPrice()).replace(",", ".");

                        // Chuyển giá trị thành Double sau khi thay thế dấu phẩy
                        price = Double.valueOf(formattedPrice);
                        SeatListActivity.this.num = num;
                        binding.tvPrice.setText("$" + price);
                    }
                },
                this,
                seatList
        );

        binding.rvSeatList.setAdapter(seatAdapter);
        binding.rvSeatList.setNestedScrollingEnabled(false);
    }
}