package tuit.vacancies.uz.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterPhone;
import tuit.vacancies.uz.model.Phone;
import tuit.vacancies.uz.service.ItemClickListener;
import tuit.vacancies.uz.service.MyAppDatabase;
import tuit.vacancies.uz.service.MyDao;

public class PhoneActivity extends AppCompatActivity {

    private MyAppDatabase myAppDatabase;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private AdapterPhone adapterPhone;
    private List<Phone> phones;
    private AlertDialog dialogPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        initComponents();
        initFunctions();
    }

    private void initFunctions() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterPhone = new AdapterPhone(phones, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                onShowPhoneDialog(position);
            }
        });
        recyclerView.setAdapter(adapterPhone);
        onLoadData();
    }

    private void onShowPhoneDialog(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        dialogPhone = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_dialog_phone_history, null);

        final TextView avatar = (TextView) view.findViewById(R.id.avatar);
        final TextView name = (TextView) view.findViewById(R.id.name);
        final TextView phone = (TextView) view.findViewById(R.id.phone);
        final LinearLayout buttonPhone = (LinearLayout) view.findViewById(R.id.button_phone);
        final LinearLayout buttonVacancy = (LinearLayout) view.findViewById(R.id.button_vacancy);
        final LinearLayout buttonProfile = (LinearLayout) view.findViewById(R.id.button_profile);
        final LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.button_close);
        avatar.setText(phones.get(position).getUserName().substring(0, 1));
        name.setText(phones.get(position).getUserName());
        phone.setText(phones.get(position).getPhone());
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhone.dismiss();
                @SuppressLint("SimpleDateFormat")
                String date = new SimpleDateFormat("HH:mm:ss dd-MMM yyyy").format(Calendar.getInstance().getTime());
                myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "phone").allowMainThreadQueries().build();
                myAppDatabase.myDao().deleteByIdPhone(phones.get(position).getPhone());
                phones.get(position).setDate(date);
                myAppDatabase.myDao().addPhone(phones.get(position));
                dialogPhone.dismiss();
                Uri u = Uri.parse("tel:" + phones.get(position).getPhone());
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
            }
        });
        buttonVacancy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhone.dismiss();
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vacancy_key", phones.get(position).getVacancyKey());
                startActivity(intent);
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhone.dismiss();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user_key", phones.get(position).getPhone());
                startActivity(intent);
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPhone.dismiss();
            }
        });
        dialogPhone.setView(view);
        dialogPhone.show();
    }

    private void onLoadData() {
        myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "phone").allowMainThreadQueries().build();
        phones.clear();
        phones.addAll(myAppDatabase.myDao().getPhone());
        Collections.reverse(phones);
        adapterPhone.notifyDataSetChanged();
    }

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_phone);
        phones = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Qo`ng`iroqlar tarixini o`chirish")
                    .setMessage("Siz rostdan ham qo`ng`iroqlar tarixini o`chirib tashlamoqchimisiz? Keyin ularni tiklashni iloji bo`lmaydi")
                    .setNegativeButton("O`chirish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            myAppDatabase.myDao().clearPhone();
                            onLoadData();
                        }
                    }).setPositiveButton("Bekor qilish", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create().show();
            return true;
        }
        return false;
    }
}
