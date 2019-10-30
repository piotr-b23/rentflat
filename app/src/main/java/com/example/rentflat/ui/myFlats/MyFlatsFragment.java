package com.example.rentflat.ui.myFlats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rentflat.R;

public class MyFlatsFragment extends Fragment {

    private MyFlatsViewModel myFlatsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myFlatsViewModel =
                ViewModelProviders.of(this).get(MyFlatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_flats, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        myFlatsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}