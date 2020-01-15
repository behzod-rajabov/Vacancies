package tuit.vacancies.uz.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.User;
import tuit.vacancies.uz.service.SharedPrefConfig;
import tuit.vacancies.uz.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private SharedPrefConfig spc;
    public static FragmentManager fm;
    private LinearLayout buttonLogin, buttonFav, buttonLogout, buttonContact, buttonAbout, buttonProfile, buttonPhone;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        initFunctions();
    }

    private void initFunctions() {
        fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.container, new MainFragment(), null).addToBackStack(null).commit();

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (spc.getIsLogin()) {
            buttonProfile.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.VISIBLE);
            buttonFav.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonProfile.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.GONE);
            buttonFav.setVisibility(View.GONE);
        }
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FavouriteActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Profildan chiqish")
                        .setMessage("Siz rostdan ham profilingizdan chiqmoqchimisiz?")
                        .setPositiveButton("Bekor qilish", null)
                        .setNegativeButton("Chiqish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spc.setIsLogin(false);
                                spc.setUser(new User("", "", "", "", "", "", "", "", ""));
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                drawer.closeDrawer(GravityCompat.START);
                            }
                        }).create().show();
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PhoneActivity.class));
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void initComponents() {
        spc = new SharedPrefConfig(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        buttonLogin = findViewById(R.id.button_login);
        buttonFav = findViewById(R.id.button_fav);
        buttonLogout = findViewById(R.id.button_logout);
        buttonProfile = findViewById(R.id.button_profile);
        buttonAbout = findViewById(R.id.button_about);
        buttonContact = findViewById(R.id.button_contact);
        buttonPhone = findViewById(R.id.button_phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search)
            Toast.makeText(getApplicationContext(), "Izlash", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else {
            if (isBack) {
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Chiqish uchun ORQAGA tugamasini yana bir marta bosing!", Toast.LENGTH_SHORT).show();
                isBack = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isBack = false;
                    }
                }, 2000);
            }
        }
        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        if (spc.getIsLogin()) {
            buttonProfile.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
            buttonLogout.setVisibility(View.VISIBLE);
            buttonFav.setVisibility(View.VISIBLE);
        }
        else
        {
            buttonProfile.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
            buttonLogout.setVisibility(View.GONE);
            buttonFav.setVisibility(View.GONE);
        }
        super.onResume();
    }
}
