package tuit.vacancies.uz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tuit.vacancies.uz.Common;
import tuit.vacancies.uz.R;
import tuit.vacancies.uz.model.Message;
import tuit.vacancies.uz.service.ItemClickListener;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.ViewHolder> {

    private List<Message> messages;
    private ItemClickListener itemClickListener;
    private Context context;

    public AdapterMessage(List<Message> messages, ItemClickListener itemClickListener, Context context) {
        this.messages = messages;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMessage.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMessage.ViewHolder holder, int position) {
        holder.onBind(messages.get(position));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView layout;
        TextView text;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            text = itemView.findViewById(R.id.text);
        }

        void onBind(Message message) {
            text.setText(message.getMessage());
            if (message.getUser1().equals(""))
            {
                ViewGroup.MarginLayoutParams layoutParams =
                        (ViewGroup.MarginLayoutParams) layout.getLayoutParams();
                layoutParams.setMargins((int) Common.pxFromDp(context, 8),
                        (int) Common.pxFromDp(context, 8),
                        (int) Common.pxFromDp(context, 64),
                        (int) Common.pxFromDp(context, 8));
                layout.requestLayout();
            }
        }
    }
}
