package tuit.vacancies.uz.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import tuit.vacancies.uz.Common;
import tuit.vacancies.uz.R;
import tuit.vacancies.uz.adapter.AdapterVacancyGrid;
import tuit.vacancies.uz.model.Favourite;
import tuit.vacancies.uz.model.Phone;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.ItemClickListener;
import tuit.vacancies.uz.service.MyAppDatabase;
import tuit.vacancies.uz.service.SharedPrefConfig;

public class DetailsActivity extends AppCompatActivity {

    private NestedScrollView body;
    private MyAppDatabase myAppDatabase;
    private ValueEventListener eventFav1, eventFav2, eventVac1, eventVac2, eventVac3;
    private SharedPrefConfig spc;
    private ImageView image, fav_icon;
    private TextView title, category, address, salary, desc, viewCount;
    private LinearLayout phoneLayout;
    private CardView close, fav;
    private boolean isFirtRun = true, isFav = true;
    private String phone = "", name, vacancyName;
    private AdapterVacancyGrid adapterVacancy;
    private RecyclerView recyclerView;
    private List<Vacancy> vacancies;
    private String categoryId = "0";
    private LinearLayout layoutRelated;
    private CardView cardViewCount;
    private AlertDialog alertDialog;
    private EditText messageText;
    private ImageButton messageButton;
    private FrameLayout container;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initComponents();
        initFunctions();
    }

    private void initFunctions() {
        fav.setVisibility(View.INVISIBLE);
        container.setVisibility(View.GONE);
        eventVac1 = FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getIntent().getStringExtra("vacancy_key")).exists()) {
                    Vacancy vacancy = dataSnapshot.child(getIntent().getStringExtra("vacancy_key")).getValue(Vacancy.class);
                    assert vacancy != null;
                    vacancy.setViews_count(String.valueOf(Integer.parseInt(vacancy.getViews_count()) + 1));
                    if (isFirtRun) {
                        FirebaseDatabase.getInstance().getReference("Vacancy")
                                .child(getIntent().getStringExtra("vacancy_key"))
                                .setValue(vacancy);
                        isFirtRun = false;
                    } else {
                        if (!vacancy.getImage().equals("") && !vacancy.getImage().isEmpty())
                            Picasso.get().load(vacancy.getImage()).placeholder(R.drawable.no_image).into(image);
                        else {
                            body.setPadding(0, (int) Common.pxFromDp(getApplicationContext(), 56), 0, (int) Common.pxFromDp(getApplicationContext(), 56));
                            image.requestLayout();
                            image.getLayoutParams().height = (int) Common.pxFromDp(getApplicationContext(), 56);

                            ViewGroup.MarginLayoutParams layoutParams =
                                    (ViewGroup.MarginLayoutParams) cardViewCount.getLayoutParams();
                            layoutParams.setMargins((int) Common.pxFromDp(getApplicationContext(), 8),
                                    (int) Common.pxFromDp(getApplicationContext(), 8),
                                    (int) Common.pxFromDp(getApplicationContext(), 64),
                                    (int) Common.pxFromDp(getApplicationContext(), 8));
                            cardViewCount.requestLayout();

                            image.setVisibility(View.INVISIBLE);
                        }
                        title.setText(vacancy.getTitle());
                        category.setText(vacancy.getCategory_name());
                        address.setText(vacancy.getRegion_name() + ", " + vacancy.getAddress());
                        desc.setText(vacancy.getDescription());
                        viewCount.setText(vacancy.getViews_count());
                        phone = vacancy.getPhone();
                        salary.setText(
                                (vacancy.getGraph().equals("1") ? "Oyik" : vacancy.getGraph().equals("2") ? "Kunlik" : vacancy.getGraph().equals("3") ? "Masofadan" : "") +
                                        " " + Common.salaryFormat(vacancy.getSalary_from(), vacancy.getSalary_to())
                        );
                        categoryId = vacancy.getCategory_id();
                        name = vacancy.getAuthor_name();
                        vacancyName = vacancy.getTitle();
                        container.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        getRelatedVacancy();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Ushbu vakansiya faol emas!", Toast.LENGTH_SHORT).show();
                }
                //FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Internet bilan bog`liq muammo yuz berdi!", Toast.LENGTH_SHORT).show();
                FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac1);
            }
        });
        eventFav1 = FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(getIntent().getStringExtra("vacancy_key") + "_" + spc.getUser().getPhone()).exists()) {
                    fav_icon.setImageResource(R.drawable.ic_bookmark_fill);
                } else {
                    fav_icon.setImageResource(R.drawable.ic_bookmark_border);
                }
                fav.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav1);
            }
        });
        if (!spc.getIsLogin())
            messageText.setEnabled(false);
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowPhoneDialog();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spc.getIsLogin()) {
                    fav.setEnabled(false);
                    eventFav2 = FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (isFav) {
                                isFav = false;
                                if (dataSnapshot.child(getIntent().getStringExtra("vacancy_key") + "_" + spc.getUser().getPhone()).exists()) {
                                    FirebaseDatabase.getInstance().getReference("Favourite")
                                            .child(getIntent().
                                                    getStringExtra("vacancy_key") + "_" + spc.getUser().getPhone())
                                            .removeValue(new DatabaseReference.CompletionListener() {
                                                @Override
                                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                                    fav_icon.setImageResource(R.drawable.ic_bookmark_border);
                                                    Toast.makeText(getApplicationContext(), "Saqlanmadan o`chirildi!", Toast.LENGTH_SHORT).show();
                                                    isFav = true;
                                                    fav.setEnabled(true);
                                                }
                                            });
                                } else {
                                    Favourite favourite = new Favourite(
                                            getIntent().getStringExtra("vacancy_key") + "_" + spc.getUser().getPhone(),
                                            spc.getUser().getPhone(), getIntent().getStringExtra("vacancy_key"));
                                    FirebaseDatabase.getInstance().getReference("Favourite").child(favourite.getKey()).setValue(favourite, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            fav_icon.setImageResource(R.drawable.ic_bookmark_fill);
                                            Toast.makeText(getApplicationContext(), "Saqlanganlarga qo`shildi!", Toast.LENGTH_SHORT).show();
                                            isFav = true;
                                            fav.setEnabled(true);
                                        }
                                    });
                                }
                            }
                            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav2);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Internet bilan bog`liq muammo yuz berdi!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav2);
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Vakansiyani saqlanganlarga qo`shish uchun avval tizimga kirishingiz kerak!", Toast.LENGTH_LONG).show();
                }
            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spc.getIsLogin()) {
                    Toast.makeText(getApplicationContext(), "Ushbu bo`lim hozircha ish faoliyatida emas!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Xabar yo`llash uchun oldin tizimga kirishingiz kerak!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getRelatedVacancy() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
        adapterVacancy = new AdapterVacancyGrid(vacancies, new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("vacancy_key", vacancies.get(position).getId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapterVacancy);
        eventVac2 = FirebaseDatabase.getInstance().getReference("Vacancy").orderByChild("category_id").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vacancies.clear();
                for (DataSnapshot post : dataSnapshot.getChildren()) {
                    if (!post.getKey().equals(getIntent().getStringExtra("vacancy_key"))) {
                        Vacancy v = post.getValue(Vacancy.class);
                        v.setId(post.getKey());
                        vacancies.add(v);
                    }
                }
                if (vacancies.size() == 0)
                    layoutRelated.setVisibility(View.GONE);
                else
                    adapterVacancy.notifyDataSetChanged();
                FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Internet bilan bog`liq muammo yuz berdi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void onShowPhoneDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.layout_dialog_on_details, null);

        final TextView avatar = (TextView) view.findViewById(R.id.avatar);
        final TextView names = (TextView) view.findViewById(R.id.name);
        final TextView phones = (TextView) view.findViewById(R.id.phone);
        final LinearLayout buttonPhone = (LinearLayout) view.findViewById(R.id.button_phone);
        final LinearLayout buttonClose = (LinearLayout) view.findViewById(R.id.button_close);
        final LinearLayout buttonProfile = (LinearLayout) view.findViewById(R.id.button_profile);
        avatar.setText(name.substring(0, 1));
        names.setText(name);
        phones.setText(phone);
        buttonPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("SimpleDateFormat")
                String date = new SimpleDateFormat("HH:mm:ss dd-MMM yyyy").format(Calendar.getInstance().getTime());
                myAppDatabase = Room.databaseBuilder(getApplicationContext(), MyAppDatabase.class, "phone").allowMainThreadQueries().build();
                myAppDatabase.myDao().deleteByIdPhone(phone);
                myAppDatabase.myDao().addPhone(new Phone(getIntent().getStringExtra("vacancy_key"), "", vacancyName, name, phone, date));
                alertDialog.dismiss();
                Uri u = Uri.parse("tel:" + phone);
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                startActivity(i);
            }
        });
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("user_key", phone);
                startActivity(intent);
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void initComponents() {
        body = findViewById(R.id.body);
        spc = new SharedPrefConfig(getApplicationContext());
        image = findViewById(R.id.image);
        fav_icon = findViewById(R.id.fav_icon);
        title = findViewById(R.id.title);
        category = findViewById(R.id.category);
        address = findViewById(R.id.address);
        salary = findViewById(R.id.salary);
        desc = findViewById(R.id.desc);
        viewCount = findViewById(R.id.view_count);
        phoneLayout = findViewById(R.id.phone_layout);
        close = findViewById(R.id.close);
        fav = findViewById(R.id.fav);
        recyclerView = findViewById(R.id.related);
        vacancies = new ArrayList<>();
        layoutRelated = findViewById(R.id.layout_related);
        cardViewCount = findViewById(R.id.card_view_count);
        messageText = findViewById(R.id.message_text);
        messageButton = findViewById(R.id.message_button);
        container = findViewById(R.id.container);
        progress = findViewById(R.id.progress);
    }

    @Override
    protected void onDestroy() {
        try {
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav1);
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav2);
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac1);
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac2);
        } catch (Exception ex) {

        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        try {
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav1);
            FirebaseDatabase.getInstance().getReference("Favourite").removeEventListener(eventFav2);
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac1);
            FirebaseDatabase.getInstance().getReference("Vacancy").removeEventListener(eventVac2);
        } catch (Exception ex) {

        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(eventFav1);
            FirebaseDatabase.getInstance().getReference("Favourite").addValueEventListener(eventFav2);
            FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(eventVac1);
            FirebaseDatabase.getInstance().getReference("Vacancy").addValueEventListener(eventVac2);
        } catch (Exception ex) {

        }
        super.onResume();
    }
}
