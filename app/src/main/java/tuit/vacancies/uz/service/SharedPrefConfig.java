package tuit.vacancies.uz.service;

import android.content.Context;
import android.content.SharedPreferences;

import tuit.vacancies.uz.model.User;

public class SharedPrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("user_preference", Context.MODE_PRIVATE);
    }

    public User getUser() {
        User user = new User(
                "", sharedPreferences.getString("fname", "No name"),
                sharedPreferences.getString("lname", ""),
                sharedPreferences.getString("phone", ""),
                sharedPreferences.getString("password", ""),
                "", sharedPreferences.getString("status", ""),
                "", ""
        );

        return user;
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("fname", user.getFname());
        editor.putString("lname", user.getLname());
        editor.putString("password", user.getPassword());
        editor.putString("phone", user.getPhone());
        editor.putString("status", user.getStatus());
        editor.putString("id", user.getId());
        editor.apply();
    }

    public boolean getIsLogin() {
        return sharedPreferences.getBoolean("isLogin", false);
    }

    public void setIsLogin(boolean isLogin) {
        sharedPreferences.edit().putBoolean("isLogin", isLogin).apply();
    }
}
