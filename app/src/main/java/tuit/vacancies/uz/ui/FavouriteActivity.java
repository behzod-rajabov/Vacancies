package tuit.vacancies.uz.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterVacancy;
import tuit.vacancies.uz.model.Favourite;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.ItemClickListener;
import tuit.vacancies.uz.service.SharedPrefConfig;

public class FavouriteActivity extends AppCompatActivity {

    private SharedPrefConfig spc;
    private Toolbar toolbar;
    private RecyclerView recyclerViewVacancy;
    private ValueEventListener eventFav, eventVac;
    private AdapterVacancy adapterVacancy;
    private List<Vacancy> vacancies;
    private List<String> vacId;
    private ShimmerFrameLayout shimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        initComponents();
        initFunctions();
    }

    private void initFunctions() {
        shimmer.startShimmerAnimation();
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerViewVacancy.setHasFixedSize(true);
        recyclerViewVacancy.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapterVacancy = new AdapterVacancy(vacancies, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vacancy_key", vacancies.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerViewVacancy.setAdapter(adapterVacancy);
        eventFav = FirebaseDatabase.getInstance().getReference("Favourite").orderByChild("user").equalTo(spc.getUser().getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vacId.clear();
                for (final DataSnapshot post : dataSnapshot.getChildren()) {
                    Favourite f = post.getValue(Favourite.class);
                    vacId.add(f.getVacancy());
                }
                onLoadVacancy();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void onLoadVacancy() {
        eventVac = FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vacancies.clear();
                for (String v : vacId) {
                    Vacancy vacancy = dataSnapshot.child(v).getValue(Vacancy.class);
                    vacancy.setId(dataSnapshot.child(v).getKey());
                    vacancies.add(vacancy);
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
        spc = new SharedPrefConfig(getApplicationContext());
        toolbar = findViewById(R.id.toolbar);
        recyclerViewVacancy = findViewById(R.id.vacancies_favs);
        vacancies = new ArrayList<>();
        vacId = new ArrayList<>();
        shimmer = findViewById(R.id.shimmer);
    }

    @Override
    protected void onPause() {
        if (eventFav != null)
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav);
        if (eventVac != null)
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac);

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (eventFav != null)
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav);
        if (eventVac != null)
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac);
        super.onDestroy();
    }
}
