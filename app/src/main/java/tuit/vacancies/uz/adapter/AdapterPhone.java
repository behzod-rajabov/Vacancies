package tuit.vacancies.uz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.Phone;
import tuit.vacancies.uz.service.ItemClickListener;

public class AdapterPhone extends RecyclerView.Adapter<AdapterPhone.ViewHolder> {

    private ItemClickListener itemClickListener;
    private List<Phone> phones;

    public AdapterPhone(List<Phone> phones, ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        this.phones = phones;
    }

    @NonNull
    @Override
    public AdapterPhone.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_phone, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPhone.ViewHolder holder, int position) {
        holder.onBind(phones.get(position));
    }

    @Override
    public int getItemCount() {
        return phones.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView avatar, phoneText, name, date;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            phoneText = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(v, getAdapterPosition());
                }
            });
        }

        void onBind(Phone phone) {
            avatar.setText(phone.getUserName().toString().substring(0,1));
            phoneText.setText(phone.getPhone());
            name.setText(phone.getUserName());
            date.setText(phone.getDate());
        }
    }
}