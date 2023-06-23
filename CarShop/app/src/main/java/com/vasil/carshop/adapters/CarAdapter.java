package com.vasil.carshop.adapters;

import static com.vasil.carshop.utils.Utils.getStringResource;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vasil.carshop.R;
import com.vasil.carshop.activities.CarActivity;
import com.vasil.carshop.responses.cars.CarGetResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<CarGetResponse> data;

    public CarAdapter(Context context, List<CarGetResponse> data) {
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_car, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CarGetResponse car = data.get(position);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        NumberFormat formatter = new DecimalFormat("#0");

//        String name = car.getName();
        holder.getCarsBrandModelTV().setText(car.getBrand() + " " + car.getModel());
        holder.getCarsPriceTV().setText(formatter.format(car.getPrice()) + " " + getStringResource(layoutInflater.getContext(), "currency_bgn"));
        holder.getCarsYearTV().setText(String.valueOf(car.getYear()));
        holder.getCarsCreatedOnTV().setText(sdf.format(car.getAddedOn()));

        holder.getCardView().setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CarActivity.class);
            intent.putExtra("carId", car.getCarId());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The layout wrapper
        private final CardView cardView;
//        private final ImageView carsImageIV;
        private final TextView carsBrandModelTV;
        private final TextView carsPriceTV;
        private final TextView carsYearTV;
        private final TextView carsCreatedOnTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.car_card);

            carsBrandModelTV = itemView.findViewById(R.id.carsBrandModelTV);
            carsPriceTV = itemView.findViewById(R.id.carsPriceTV);
            carsYearTV = itemView.findViewById(R.id.carsYearTV);
            carsCreatedOnTV = itemView.findViewById(R.id.carsCreatedOnTV);
        }

        public CardView getCardView() {
            return cardView;
        }

//        public ImageView getCarsImageIV() {
//            return carsImageIV;
//        }


        public TextView getCarsBrandModelTV() {
            return carsBrandModelTV;
        }

        public TextView getCarsPriceTV() {
            return carsPriceTV;
        }

        public TextView getCarsYearTV() {
            return carsYearTV;
        }

        public TextView getCarsCreatedOnTV() {
            return carsCreatedOnTV;
        }
    }
}
