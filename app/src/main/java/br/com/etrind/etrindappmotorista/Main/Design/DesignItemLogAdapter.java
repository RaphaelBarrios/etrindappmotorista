package br.com.etrind.etrindappmotorista.Main.Design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.R;

public class DesignItemLogAdapter extends BaseAdapter {
    final Context ctx;
    final ArrayList<LogEntity> items;

    public DesignItemLogAdapter(Context context, ArrayList<LogEntity> arrayItems){
        this.ctx = context;
        this.items = arrayItems;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).LogId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.design_item_log, null);
        }

        TextView tvLviDataHora = view.findViewById(R.id.tvLviDataHora);
        tvLviDataHora.setText(items.get(position).DataHora);

        TextView tvLviDescricao = view.findViewById(R.id.tvLviDescricao);
        tvLviDescricao.setText(items.get(position).Descricao);

        return view;
    }


}
