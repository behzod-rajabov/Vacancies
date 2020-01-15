package tuit.vacancies.uz.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.User;
import tuit.vacancies.uz.service.SharedPrefConfig;

public class LoginActivity extends AppCompatActivity {

    private ValueEventListener eventIn, eventUp;
    private Toolbar toolbar;
    private SharedPrefConfig spc;
    private FirebaseDatabase database;
    private DatabaseReference tabelUser;
    private LinearLayout layoutSignIn, layoutSignUp;
    private EditText phoneIn, password;
    private EditText phoneUp, fname, lname, pass;
    private Button signIn, signUp, showSignIn, showSingUp;
    private AlertDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spc = new SharedPrefConfig(getApplicationContext());
        if (spc.getIsLogin())
            finish();
        initComponents();
        initFunction();
    }

    private void initFunction() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        dialogProgress = builder.create();
        LayoutInflater inflater1 = getLayoutInflater();
        View progressView = inflater1.inflate(R.layout.layout_progress, null);
        dialogProgress.setView(progressView);
        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProgress.setCancelable(false);

        layoutSignUp.setVisibility(View.GONE);
        layoutSignIn.setVisibility(View.VISIBLE);
        database = FirebaseDatabase.getInstance();
        tabelUser = database.getReference("User");

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                eventIn = tabelUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("+998" + phoneIn.getText().toString()).exists()) {
                            User user = dataSnapshot.child("+998" + phoneIn.getText().toString()).getValue(User.class);
                            if (user.getPassword().equals(password.getText().toString())) {
                                Toast.makeText(getApplicationContext(), "Xush kelibsiz " + user.getFname() + " " + user.getLname(), Toast.LENGTH_SHORT).show();
                                spc.setUser(user);
                                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                spc.setIsLogin(true);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Parol xato kiritildi!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Bunday foydalanuvchi topilmadi!", Toast.LENGTH_SHORT).show();
                        }
                        hideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Server bilan bog`lanishda xatolik!", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                });
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                eventUp = tabelUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("+998" + phoneUp.getText().toString()).exists()) {
                            Toast.makeText(getApplicationContext(), "Bu telefon raqami band!", Toast.LENGTH_SHORT).show();
                        } else {
                            User user = new User("+998" + phoneUp.getText().toString(),
                                    fname.getText().toString(),
                                    lname.getText().toString(),
                                    "+998" + phoneUp.getText().toString(),
                                    pass.getText().toString(), "1", "1", "1", "1");
                            tabelUser.child("+998" + phoneUp.getText().toString()).setValue(user);
                            Toast.makeText(getApplicationContext(), "Xush kelibsiz " + user.getFname() + " " + user.getLname(), Toast.LENGTH_SHORT).show();
                            spc.setUser(user);
                            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            spc.setIsLogin(true);
                            finish();
                        }
                        hideProgress();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Internet bilan bog`lanib bo`lmadi!", Toast.LENGTH_SHORT).show();
                        hideProgress();
                    }
                });
            }
        });

        showSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutSignIn.setVisibility(View.GONE);
                layoutSignUp.setVisibility(View.VISIBLE);
            }
        });
        showSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutSignIn.setVisibility(View.VISIBLE);
                layoutSignUp.setVisibility(View.GONE);
            }
        });
    }

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        layoutSignUp = findViewById(R.id.layout_signup);
        layoutSignIn = findViewById(R.id.layout_signin);
        phoneIn = findViewById(R.id.phone_in);
        password = findViewById(R.id.password);
        phoneUp = findViewById(R.id.phone_up);
        pass = findViewById(R.id.pass);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        signIn = findViewById(R.id.signin);
        signUp = findViewById(R.id.signup);
        showSignIn = findViewById(R.id.show_signin);
        showSingUp = findViewById(R.id.show_signup);
    }

    public void showProgress() {
        dialogProgress.show();
    }

    public void hideProgress() {
        dialogProgress.dismiss();
    }

    @Override
    protected void onPause() {
        hideProgress();
        if (eventIn != null)
            tabelUser.removeEventListener(eventIn);
        if (eventUp != null)
            tabelUser.removeEventListener(eventUp);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (eventIn != null)
            tabelUser.removeEventListener(eventIn);
        if (eventUp != null)
            tabelUser.removeEventListener(eventUp);
        super.onDestroy();
    }
}
