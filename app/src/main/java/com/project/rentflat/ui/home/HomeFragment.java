package com.project.rentflat.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.project.rentflat.R;
import com.project.rentflat.ui.searchForFlat.SearchForFlat;
import com.project.rentflat.ui.session.Login;
import com.project.rentflat.ui.session.Register;

import static com.project.rentflat.ui.MainActivity.sessionManager;
import static com.project.rentflat.ui.MainActivity.userId;

public class HomeFragment extends Fragment {

    public TextView name;
//    private HomeViewModel homeViewModel;
    private Button registerButton, loginButton, findFlatButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        registerButton = root.findViewById(R.id.registerButton);
        loginButton = root.findViewById(R.id.loginButton);
        findFlatButton = root.findViewById(R.id.addFlatButton);


        if (sessionManager.isLogged()) {
            registerButton.setVisibility(View.INVISIBLE);
            loginButton.setText("wyloguj");
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Register.class);
                startActivity(intent);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLogged()) {
                    sessionManager.logout();
                    loginButton.setText("zaloguj");
                    registerButton.setVisibility(View.VISIBLE);
                    userId = null;

                } else {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                }
            }
        });

        findFlatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchForFlat.class);
                startActivity(intent);
            }
        });

//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


}