package com.example.rentflat.ui.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rentflat.R;
import com.example.rentflat.ui.register.Register;

import static com.example.rentflat.MainActivity.sessionMenager;

public class MyAccountFragment extends Fragment {

    private MyAccountViewModel myAccountViewModel;
    private Button updateEmail,updatePhone,changePassword;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAccountViewModel =
                ViewModelProviders.of(this).get(MyAccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_account, container, false);
        final TextView textView = root.findViewById(R.id.text_my_account);

        updateEmail = (Button) root.findViewById(R.id.updateEmail);
        updatePhone = (Button) root.findViewById(R.id.updatePhone);
        changePassword = (Button) root.findViewById(R.id.changePassword);

        if(sessionMenager.isLogged()) {

            updateEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new  Intent(getActivity(), ChangeEmail.class);
                    startActivity(intent);
                }
            });

            updatePhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new  Intent(getActivity(), ChangePhone.class);
                    startActivity(intent);
                }
            });

            changePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new  Intent(getActivity(), ChangePassword.class);
                    startActivity(intent);
                }
            });


        }


        return root;
    }
}