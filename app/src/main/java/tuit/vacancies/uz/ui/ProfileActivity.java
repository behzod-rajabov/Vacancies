package tuit.vacancies.uz.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterVacancy;
import tuit.vacancies.uz.model.Phone;
import tuit.vacancies.uz.model.User;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.ItemClickListener;
import tuit.vacancies.uz.service.MyAppDatabase;
import tuit.vacancies.uz.service.SharedPrefConfig;

public class ProfileActivity extends AppCompatActivity {

    private SharedPrefConfig spc;
    private ImageButton exit, logout, message, phone, edit, typeList, typeGrid;
    private TextView avatarText, nameText, phoneText;
    private RecyclerView recyclerView;
    private List<Vacancy> vacancies;
    private AdapterVacancy adapterVacancy;
    private ShimmerFrameLayout shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initComponents();
        initFunctions();
    }

    private void initFunctions() {
        shimmer.startShimmerAnimation();
        if (getIntent().getStringExtra("user_key") == null || getIntent().getStringExtra("user_key").equals(spc.getUser().getPhone())) {
            Toast.makeText(getApplicationContext(), "" + spc.getUser().getPhone(), Toast.LENGTH_SHORT).show();
            avatarText.setText(spc.getUser().getFname().substring(0, 1));
            nameText.setText(spc.getUser().getFname() + " " + spc.getUser().getLname());
            phoneText.setText(spc.getUser().getPhone());
            phone.setVisibility(View.GONE);
            message.setVisibility(View.GONE);
        }
        else {
            logout.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference("User")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.d("Phone", getIntent().getStringExtra("user_key"));
                            for (DataSnapshot post : dataSnapshot.getChildren()) {
                                if (post.getKey().equals(getIntent().getStringExtra("user_key"))) {
                                    User user = post.getValue(User.class);
                                    avatarText.setText(user.getFname().substring(0, 1));
                                    nameText.setText(user.getFname() + " " + user.getLname());
                                    phoneText.setText(user.getPhone());
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AnketaActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Profildan chiqish")
                        .setMessage("Siz rostdan ham profilingizdan chiqmoqchimisiz?")
                        .setPositiveButton("Bekor qilish", null)
                        .setNegativeButton("Chiqish", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spc.setUser(new User("", "", "", "", "", "", "", "", ""));
                                spc.setIsLogin(false);
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        }).create().show();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setTitle("Qo`ng`iroq qilish")
                        .setMessage("Siz rostdan ham ushbu raqamga qo`ng`iroq qilmoqchimisiz? \n" + phoneText.getText().toString())
                        .setNegativeButton("Bekor qilish", null)
                        .setPositiveButton("Tasdiqlash", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Uri u = Uri.parse("tel:" + phone);
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                startActivity(i);
                            }
                        }).create().show();
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spc.getIsLogin())
                {
                    Toast.makeText(getApplicationContext(), "Ushbu bo`lim hozircha ish faoliyatida emas!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Xabar yo`llash uchun oldin tizimga kirishingiz kerak!", Toast.LENGTH_LONG).show();
                }
            }
        });
        onLoadVacancy();
    }

    private void onLoadVacancy() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterVacancy = new AdapterVacancy(vacancies, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vacancy_key", vacancies.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterVacancy);
        String phn = "";
        if (getIntent().getStringExtra("user_key") == null)
            phn = spc.getUser().getPhone();
        else
            phn = getIntent().getStringExtra("user_key");
        FirebaseDatabase.getInstance().getReference("Vacancy").orderByChild("phone").equalTo(phn).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Vacancy v = post.getValue(Vacancy.class);
                    vacancies.add(v);
                }
                shimmer.setVisibility(View.GONE);
                shimmer.stopShimmerAnimation();
                adapterVacancy.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initComponents() {
        shimmer = findViewById(R.id.shimmer);
        spc = new SharedPrefConfig(getApplicationContext());
        exit = findViewById(R.id.button_exit);
        logout = findViewById(R.id.button_logout);
        message = findViewById(R.id.button_message);
        phone = findViewById(R.id.button_phone);
        edit = findViewById(R.id.button_edit);
        typeList = findViewById(R.id.type_list);
        typeGrid = findViewById(R.id.type_grid);
        avatarText = findViewById(R.id.avatar);
        nameText = findViewById(R.id.name);
        phoneText = findViewById(R.id.phone);
        recyclerView = findViewById(R.id.recycler_vacancy);
        vacancies = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        initFunctions();
        super.onResume();
    }
}
