package tuit.vacancies.uz.ui.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterVacancy;
import tuit.vacancies.uz.model.Favourite;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.FavClickListener;
import tuit.vacancies.uz.service.ItemClickListener;
import tuit.vacancies.uz.service.SharedPrefConfig;
import tuit.vacancies.uz.ui.AddActivity;
import tuit.vacancies.uz.ui.DetailsActivity;
import tuit.vacancies.uz.ui.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private SharedPrefConfig spc;
    private ValueEventListener eventVac, eventFav;
    private boolean isFav = false;
    private RecyclerView recyclerViewVacancy;
    private AdapterVacancy adapterVacancy;
    private List<Vacancy> vacancies;
    private Button buttonAdd;
    private ShimmerFrameLayout shimmer;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        isFav = true;

        initComponents(view);
        initFunctions();

        return view;
    }

    private void initFunctions() {
        shimmer.startShimmerAnimation();
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spc.getIsLogin())
                    startActivity(new Intent(getContext(), AddActivity.class));
                else
                {
                    Toast.makeText(getContext(), "Vakansiya qo`shish uchun avval tizimga kirishingiz kerak!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
            }
        });
        recyclerViewVacancy.setHasFixedSize(true);
        recyclerViewVacancy.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterVacancy = new AdapterVacancy(vacancies, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra("vacancy_key", vacancies.get(position).getId());
                startActivity(intent);
            }
        }
        /*, new FavClickListener() {
            @Override
            public void onClick(View view, final int position, final ImageView image) {
                eventFav = FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        image.setEnabled(false);
                        if (isFav) {
                            isFav = false;
                            if (dataSnapshot.child(vacancies.get(position).getId() + "_" + spc.getUser().getPhone()).exists()) {
                                FirebaseDatabase.getInstance().getReference("Favourite")
                                        .child(vacancies.get(position).getId() + "_" + spc.getUser().getPhone())
                                        .removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                image.setImageResource(R.drawable.ic_bookmark_border);
                                                Toast.makeText(getContext(), "Saqlanmadan o`chirildi!", Toast.LENGTH_SHORT).show();
                                                isFav = true;
                                                image.setEnabled(true);
                                            }
                                        });
                            } else {
                                Favourite favourite = new Favourite(
                                        vacancies.get(position).getId() + "_" + spc.getUser().getPhone(),
                                        spc.getUser().getPhone(), vacancies.get(position).getId());
                                FirebaseDatabase.getInstance().getReference("Favourite").child(favourite.getKey()).setValue(favourite, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        image.setImageResource(R.drawable.ic_bookmark_fill);
                                        Toast.makeText(getContext(), "Saqlanganlarga qo`shildi!", Toast.LENGTH_SHORT).show();
                                        isFav = true;
                                        image.setEnabled(true);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Internet bilan bog`liq muammo yuz berdi!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }*/
        );
        eventVac = FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vacancies.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vacancy v = snapshot.getValue(Vacancy.class);
                    v.setId(snapshot.getKey());
                    vacancies.add(v);
                }
                shimmer.setVisibility(View.GONE);
                shimmer.stopShimmerAnimation();
                adapterVacancy.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Internet bilan xatolik!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewVacancy.setAdapter(adapterVacancy);
    }

    private void initComponents(View view) {
        spc = new SharedPrefConfig(getContext());
        recyclerViewVacancy = view.findViewById(R.id.recycler);
        vacancies = new ArrayList<>();
        buttonAdd = view.findViewById(R.id.button_add);
        shimmer = view.findViewById(R.id.shimmer);
    }

    @Override
    public void onPause() {
        try {
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac);
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav);
        } catch (Exception ex) {

        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        try {
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac);
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav);
        } catch (Exception ex) {

        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        try {
            FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(eventVac);
            FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(eventFav);
        } catch (Exception ex) {

        }
        super.onResume();
    }
}
