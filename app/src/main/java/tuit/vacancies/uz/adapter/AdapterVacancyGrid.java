package tuit.vacancies.uz.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import tuit.vacancies.uz.Common;
import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.Vacancy;
import tuit.vacancies.uz.service.ItemClickListener;

public class AdapterVacancyGrid extends RecyclerView.Adapter<AdapterVacancyGrid.AdapterViewHolder> {

    private List<Vacancy> vacancies;
    private ItemClickListener itemClickListener;

    public AdapterVacancyGrid(List<Vacancy> vacancies, ItemClickListener itemClickListener) {
        this.vacancies = vacancies;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AdapterVacancyGrid.AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vacancy_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterVacancyGrid.AdapterViewHolder holder, int position) {
        holder.onBind(vacancies.get(position));
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    class AdapterViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView salary;
        ImageView image;

        AdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            salary = itemView.findViewById(R.id.salary);
            image = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(v, getAdapterPosition());
                }
            });
        }

        @SuppressLint("SetTextI18n")
        void onBind(Vacancy vacancy) {
            title.setText(vacancy.getTitle());
            salary.setText(Common.salaryFormat(vacancy.getSalary_from(), vacancy.getSalary_to()));
            if (!vacancy.getImage().equals(""))
                Picasso.get().load(vacancy.getImage()).placeholder(R.drawable.no_image).into(image);
        }
    }
}
