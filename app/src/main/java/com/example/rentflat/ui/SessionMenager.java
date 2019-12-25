package com.example.rentflat.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.rentflat.MainActivity;
import com.example.rentflat.ui.login.Login;

import java.util.HashMap;

public class SessionMenager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGGED";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String ID = "ID";

    public SessionMenager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String name, String username, String id) {
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, name);
        editor.putString(USERNAME, username);
        editor.putString(ID, id);
        editor.apply();
    }

    public boolean isLogged() {
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkIfLogged() {
        if (this.isLogged()) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(ID, sharedPreferences.getString(ID, null));

        return user;
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }


}
