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
import br.com.etrind.etrindappmotorista.R;

public class DesignItemAtivacaoAdapter extends BaseAdapter {
    final Context ctx;
    final ArrayList<AtivacaoEntity> items;

    public DesignItemAtivacaoAdapter(Context context, ArrayList<AtivacaoEntity> arrayItems){
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
        return items.get(position).Numero;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.design_item_ativacao, null);
        }

        TextView tvLviAtivacaoNumero = view.findViewById(R.id.tvLviAtivacaoNumero);
        tvLviAtivacaoNumero.setText(String.format(ctx.getString(R.string.DesignItemAtivacaoAdapter_tvLviAtivacaoNumero), items.get(position).Numero));

        TextView tvLviAtivacaoData = view.findViewById(R.id.tvLviAtivacaoData);
        tvLviAtivacaoData.setText(items.get(position).Data);

        TextView tvLviAtivacaoProcesso = view.findViewById(R.id.tvLviAtivacaoProcesso);
        tvLviAtivacaoProcesso.setText(items.get(position).Processo);

        TextView tvLviAtivacaoTransportadora = view.findViewById(R.id.tvLviAtivacaoTransportadora);
        tvLviAtivacaoTransportadora.setText(items.get(position).Transportadora);

        TextView tvLviAtivacaoOrigemTitulo = view.findViewById(R.id.tvLviAtivacaoOrigemTitulo);
        tvLviAtivacaoOrigemTitulo.setText(items.get(position).OrigemTitulo);

        TextView tvLviAtivacaoOrigemOrigemDesc = view.findViewById(R.id.tvLviAtivacaoOrigemOrigemDesc);
        tvLviAtivacaoOrigemOrigemDesc.setText(items.get(position).ListaItinerariosStr);

        LinearLayout llAtivacaoBotoesGeral = view.findViewById(R.id.llAtivacaoBotoesGeral);
        llAtivacaoBotoesGeral.removeAllViews();

        return view;
    }


}
