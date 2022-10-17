package com.projecttwenty.expirationtracker;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
public class CountryAdapter extends ArrayAdapter<CountryItem> {
    public CountryAdapter(Context context, ArrayList<CountryItem> countryList) {
        super(context, 0, countryList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.country_spinner_row, parent, false
            );
        }
//        ImageView imageViewFlag = convertView.findViewById(R.id.image_view_flag);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        CountryItem currentItem = getItem(position);
        if (currentItem != null) {
//            imageViewFlag.setImageResource(currentItem.getFlagImage());
            textViewName.setText(currentItem.getCountryName());
        }
        return convertView;
    }
}
