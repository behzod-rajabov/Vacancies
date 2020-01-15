package tuit.vacancies.uz.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterSpinner;
import tuit.vacancies.uz.model.Category;
import tuit.vacancies.uz.model.Region;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.SharedPrefConfig;

public class AddActivity extends AppCompatActivity {

    private ValueEventListener eventReg, eventCat;
    private SharedPrefConfig spc;
    private Toolbar toolbar;
    private Spinner spinnerCats;
    private AdapterSpinner adapterCats;
    private List<String> cats;
    private List<String> catsId;
    private Spinner spinnerRegs;
    private AdapterSpinner adapterRegs;
    private List<String> regs;
    private List<String> regsId;
    private EditText title, desc, salaryFrom, salaryTo, address;
    private Spinner salaryToSpinner, salaryFromSpinner;
    private RadioButton monthly, daily, remote;
    private RadioButton worker, employer;
    private Button buttonSubmit;
    private ProgressBar progress;
    private NestedScrollView body;
    int load = 0;
    String salaryToString = "", salaryFromString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
        body.setVisibility(View.GONE);
        adapterCats = new AdapterSpinner(getApplicationContext(), cats);
        spinnerCats.setAdapter(adapterCats);
        eventCat = FirebaseDatabase.getInstance().getReference("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cats.clear();
                catsId.clear();
                cats.add("Kategoriyani tanlang");
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Category c = post.getValue(Category.class);
                    cats.add(c.getName());
                    catsId.add(c.getId());
                }
                load++;
                if (load >= 2) {
                    progress.setVisibility(View.GONE);
                    body.setVisibility(View.VISIBLE);
                }
                adapterCats.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Internet bilan aloqa yo`q!", Toast.LENGTH_SHORT).show();
            }
        });
        adapterRegs = new AdapterSpinner(getApplicationContext(), regs);
        spinnerRegs.setAdapter(adapterRegs);
        eventReg = FirebaseDatabase.getInstance().getReference("Region").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                regs.clear();
                regsId.clear();
                regs.add("Regionni tanlang");
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    Region c = post.getValue(Region.class);
                    regs.add(c.getName());
                    regsId.add(c.getId());
                }
                load++;
                if (load >= 2) {
                    progress.setVisibility(View.GONE);
                    body.setVisibility(View.VISIBLE);
                }
                adapterRegs.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Internet bilan aloqa yo`q!", Toast.LENGTH_SHORT).show();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddVacancy();
            }
        });
        salaryFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!salaryFrom.getText().toString().isEmpty()) {
                    if (salaryFrom.getText().toString().equals("0"))
                        salaryFrom.setText("");
                    if (Float.parseFloat(salaryFrom.getText().toString()) > 999.0) {
                        salaryFrom.setText(salaryFromString);
                        salaryFrom.setSelection(salaryFrom.getText().length());
                    }
                    else
                        salaryFromString = salaryFrom.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        salaryTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!salaryTo.getText().toString().isEmpty()) {
                    if (salaryTo.getText().toString().equals("0"))
                        salaryTo.setText("");
                    if (Float.parseFloat(salaryTo.getText().toString()) > 999.0) {
                        salaryTo.setText(salaryToString);
                        salaryTo.setSelection(salaryTo.getText().length());
                    }
                    else
                        salaryToString = salaryTo.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void onAddVacancy() {
        String graph = "", type = "";

        if (spinnerCats.getSelectedItemPosition() < 1) {
            Toast.makeText(getApplicationContext(), "Kategoriya tanlanmadi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerRegs.getSelectedItemPosition() < 1) {
            Toast.makeText(getApplicationContext(), "Region tanlanmadi!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (employer.isChecked())
            type = "1";
        else if (worker.isChecked())
            type = "2";
        else {
            Toast.makeText(getApplicationContext(), "Vakansiya turi tanlanmagan!", Toast.LENGTH_SHORT).show();
            worker.setError("Vakansiya turi tanlanishi shart!");
            return;
        }

        if (title.getText().length() < 5) {
            title.setError("Sarlavha 5 belgidan kam bo`lmasligi kerak!");
            Toast.makeText(getApplicationContext(), "Sarlavha 5 belgidan kam bo`lmasligi kerak!", Toast.LENGTH_SHORT).show();
            title.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(title, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (desc.getText().length() < 10) {
            desc.setError("Tafsilot 10 belgidan kam bo`lmasligi kerak!");
            Toast.makeText(getApplicationContext(), "Tafsilot 10 belgidan kam bo`lmasligi kerak!", Toast.LENGTH_SHORT).show();
            desc.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(desc, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (monthly.isChecked())
            graph = "1";
        else if (daily.isChecked())
            graph = "2";
        else if (remote.isChecked())
            graph = "3";
        else {
            Toast.makeText(getApplicationContext(), "Ish grafigi tanlanmagan!", Toast.LENGTH_SHORT).show();
            remote.setError("Grafik tanlanishi shart!");
            return;
        }

        if (salaryFrom.getText().toString().equals("") || salaryTo.getText().toString().equals("")) {
            salaryTo.setError("Oylik maoshni kiritish shart!");
            Toast.makeText(getApplicationContext(), "Oylik maoshni kiritish shart!", Toast.LENGTH_SHORT).show();
            salaryFrom.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(salaryFrom, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (address.getText().toString().equals("")) {
            address.setError("To`liq adress ham kiritilishi shart");
            Toast.makeText(getApplicationContext(), "To`liq address ham kiritilishi shart!", Toast.LENGTH_SHORT).show();
            address.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(address, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

        if (Float.parseFloat(salaryFrom.getText().toString().trim()) * (salaryFromSpinner.getSelectedItemPosition() == 0 ? 1000 : 1000000) >
                Float.parseFloat(salaryTo.getText().toString().trim()) * (salaryToSpinner.getSelectedItemPosition() == 0 ? 1000 : 1000000)) {
            Toast.makeText(getApplicationContext(), "Boshlang`ich va maksimal maoshlar o`zaro noto`g`ri kiritildi!", Toast.LENGTH_SHORT).show();
            return;
        }

        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MMM yyyy").format(Calendar.getInstance().getTime());
        String key = String.valueOf(System.currentTimeMillis());
        Vacancy vacancy = new Vacancy(
                key,
                catsId.get(spinnerCats.getSelectedItemPosition() - 1),
                cats.get(spinnerCats.getSelectedItemPosition()),
                regsId.get(spinnerRegs.getSelectedItemPosition() - 1),
                regs.get(spinnerRegs.getSelectedItemPosition()),
                spc.getUser().getId(), spc.getUser().getFname() + " " + spc.getUser().getLname(), spc.getUser().getPhone(), "", title.getText().toString().trim(),
                desc.getText().toString().trim(),
                String.valueOf(Float.parseFloat(salaryFrom.getText().toString().trim()) * (salaryFromSpinner.getSelectedItemPosition() == 0 ? 1000 : 1000000)),
                String.valueOf(Float.parseFloat(salaryTo.getText().toString().trim()) * (salaryToSpinner.getSelectedItemPosition() == 0 ? 1000 : 1000000)),
                address.getText().toString().trim(),
                graph, type, "1", "1", date, date
        );
        FirebaseDatabase.getInstance().getReference("Vacancy").child(key).setValue(vacancy);
        Toast.makeText(getApplicationContext(), "Muvaffaqiyatli bajarildi!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void initComponents() {
        spc = new SharedPrefConfig(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        spinnerCats = findViewById(R.id.category);
        spinnerRegs = findViewById(R.id.region);
        cats = new ArrayList<>();
        regs = new ArrayList<>();
        catsId = new ArrayList<>();
        regsId = new ArrayList<>();
        worker = findViewById(R.id.worker);
        employer = findViewById(R.id.employer);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        salaryFrom = findViewById(R.id.salary_from);
        salaryTo = findViewById(R.id.salary_to);
        address = findViewById(R.id.address);
        monthly = findViewById(R.id.monthly);
        daily = findViewById(R.id.daily);
        remote = findViewById(R.id.remote);
        buttonSubmit = findViewById(R.id.button_submit);
        progress = findViewById(R.id.progress);
        body = findViewById(R.id.body);
        salaryFromSpinner = findViewById(R.id.salary_from_spinner);
        salaryToSpinner = findViewById(R.id.salary_to_spinner);
    }

    @Override
    protected void onPause() {
        FirebaseDatabase.getInstance().getReference("Region").removeEventListener(eventReg);
        FirebaseDatabase.getInstance().getReference("Category").removeEventListener(eventCat);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        FirebaseDatabase.getInstance().getReference("Region").removeEventListener(eventReg);
        FirebaseDatabase.getInstance().getReference("Category").removeEventListener(eventCat);
        super.onDestroy();
    }
}
