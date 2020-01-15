package tuit.vacancies.uz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class AnketaActivity extends AppCompatActivity {

    private SharedPrefConfig spc;
    private Toolbar toolbar;
    private EditText lname, fname;
    private Button submit;
    private AlertDialog dialogProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anketa);
        spc = new SharedPrefConfig(getApplicationContext());

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
        fname.setText(spc.getUser().getFname());
        lname.setText(spc.getUser().getLname());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeInfo();
            }
        });
        fname.setSelection(fname.getText().length());

        AlertDialog.Builder builder = new AlertDialog.Builder(AnketaActivity.this);
        dialogProgress = builder.create();
        LayoutInflater inflater1 = getLayoutInflater();
        View progressView = inflater1.inflate(R.layout.layout_progress, null);
        dialogProgress.setView(progressView);
        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProgress.setCancelable(false);
    }

    private void onChangeInfo() {
        if (fname.getText().toString().trim().isEmpty()) {
            fname.setError("Ismni kiritishingiz shart");
            return;
        }
        if (lname.getText().toString().trim().isEmpty()) {
            lname.setError("Familiyani kiritish shart");
            return;
        }

        dialogProgress.show();

        final User user = new User("",
                fname.getText().toString().trim(),
                lname.getText().toString().trim(),
                spc.getUser().getPhone(),
                spc.getUser().getPassword(), "employer",
                spc.getUser().getStatus(), "", "");


        final DatabaseReference tableUser = FirebaseDatabase.getInstance().getReference("Users");
        tableUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tableUser.child(user.getPhone()).setValue(user);
                spc.setUser(user);
                Toast.makeText(getApplicationContext(), "Ma'lumotlar tasdiqlandi!", Toast.LENGTH_SHORT).show();
                finish();
                dialogProgress.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Internet bilan bog`lanib bo`lmadi!", Toast.LENGTH_SHORT).show();
                dialogProgress.dismiss();
            }
        });
    }

    private void initComponents() {
        toolbar = findViewById(R.id.toolbar);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        submit = findViewById(R.id.submit);
    }
}
