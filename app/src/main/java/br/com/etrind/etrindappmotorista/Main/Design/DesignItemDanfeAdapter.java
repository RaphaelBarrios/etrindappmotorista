package br.com.etrind.etrindappmotorista.Main.Design;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings("Convert2Lambda")
public class DesignItemDanfeAdapter extends BaseAdapter {
    private final Context ctx;
    private final ArrayList<String> items;

    public DesignItemDanfeAdapter(Context context, ArrayList<String> arrayItems){
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
        return items.indexOf(items.get(position));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.design_item_danfe, null);
        }

        TextView tvLviDanfeNumero = view.findViewById(R.id.tvLviDanfeNumero);
        tvLviDanfeNumero.setText(items.get(position));

        Button btnLviDanfeExcluir = view.findViewById(R.id.btnLviDanfeExcluir);
        btnLviDanfeExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ctx, "Excluir NFe " + items.get(position), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setCancelable(true);
                builder.setTitle(ctx.getString(R.string.excluir_danfe_confirmacao_titulo));
                builder.setMessage(ctx.getString(R.string.excluir_danfe_confirmacao_mensagem) + "\n" + items.get(position));
                builder.setPositiveButton(ctx.getString(R.string.excluir_danfe_confirmacao_texto_positivo),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                items.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton(ctx.getString(R.string.excluir_danfe_confirmacao_texto_negativo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }

}
