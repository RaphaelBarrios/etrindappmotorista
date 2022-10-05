package br.com.etrind.etrindappmotorista.Main.Design;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.etrind.etrindappmotorista.Business.NotificacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.R;

public class DesignItemNotificacaoAdapter extends BaseAdapter {
    final Context ctx;
    final ArrayList<NotificacaoEntity> items;

    public DesignItemNotificacaoAdapter(Context context, ArrayList<NotificacaoEntity> arrayItems){
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
        return items.get(position).IdNotificacao;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.design_item_notificacao, null);
        }

        TextView tvNviDataHora = view.findViewById(R.id.tvNviDataHora);
        tvNviDataHora.setText(items.get(position).Data);

        TextView tvNviDescricao = view.findViewById(R.id.tvNviDescricao);
        tvNviDescricao.setText(items.get(position).Notificacao);

        return view;
    }


}
