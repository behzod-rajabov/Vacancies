package tuit.vacancies.uz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import tuit.vacancies.uz.R;

public class AdapterSpinner extends BaseAdapter {

    Context context;
    List<String> list;
    LayoutInflater inflter;

    public AdapterSpinner(Context applicationContext, List<String> list) {
        this.context = applicationContext;
        this.list = list;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.layout_spinner, null);
        TextView name = (TextView) view.findViewById(R.id.text);
        name.setText(list.get(position));

        return view;
    }
}
