package com.example.agendaatividadesv2.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences("AgendaAtividadesPrefs", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(int userId, String userName, String userEmail) {
        editor.putBoolean("is_logged_in", true);
        editor.putInt("user_id", userId);
        editor.putString("user_name", userName);
        editor.putString("user_email", userEmail);
        editor.commit();
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean("is_logged_in", false);
    }

    public int getUserId() {
        return pref.getInt("user_id", -1);
    }

    public String getUserName() {
        return pref.getString("user_name", "");
    }

    public String getUserEmail() {
        return pref.getString("user_email", "");
    }
} 