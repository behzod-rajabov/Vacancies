package tuit.vacancies.uz.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import tuit.vacancies.uz.Common;
import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.FavClickListener;
import tuit.vacancies.uz.service.ItemClickListener;

public class AdapterVacancy extends RecyclerView.Adapter<AdapterVacancy.AdapterViewHolder> {

    private List<Vacancy> vacancies;
    private ItemClickListener itemClickListener;

    public AdapterVacancy(List<Vacancy> vacancies, ItemClickListener itemClickListener) {
        this.vacancies = vacancies;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AdapterVacancy.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vacancy, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVacancy.AdapterViewHolder holder, int position) {
        holder.onBind(vacancies.get(position));
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView date;
        TextView salary;
        TextView address;
        ImageView bookmark;

        AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            salary = itemView.findViewById(R.id.salary);
            address = itemView.findViewById(R.id.address);
            bookmark = itemView.findViewById(R.id.bookmark);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(v, getAdapterPosition());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void onBind(final Vacancy vacancy) {
            title.setText(vacancy.getTitle());
            date.setText(vacancy.getUpdated_at());
            salary.setText(Common.salaryFormat(vacancy.getSalary_from(), vacancy.getSalary_to()));
            address.setText(vacancy.getRegion_name() + " " + vacancy.getAddress());
        }
    }
}
