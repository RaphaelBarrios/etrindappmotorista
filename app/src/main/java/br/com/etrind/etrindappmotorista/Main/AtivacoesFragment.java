package br.com.etrind.etrindappmotorista.Main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Infra.UserInfo;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemAtivacaoAdapter;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings({"Convert2Lambda", "CommentedOutCode"})
public class AtivacoesFragment extends Fragment {
    private Context ctx;
    private ListView lvAtivacoes;
    private LinearLayout llAtivacaoAtual;
    private View viewAtivacaoAtual;
    private final String cnh;
    private final List<AtivacaoEntity> ativacoes;
    UserInfo userInfo;

    public AtivacoesFragment(List<AtivacaoEntity> ativacoes, String cnh) {
        this.ativacoes = ativacoes;
        this.cnh = cnh;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ativacoes, container, false);

        ctx =  getActivity();
        userInfo = new UserInfo(ctx);
        lvAtivacoes = view.findViewById(R.id.lvFragmentAtivacoes);
        llAtivacaoAtual = view.findViewById(R.id.llFragmentAtivacaoAtual);
        TextView tvFragmentAtivacoes = view.findViewById(R.id.tvFragmentAtivacoes);
        tvFragmentAtivacoes.setText(String.format(ctx.getString(R.string.AtivacoesFragment_tvFragmentAtivacoes), this.cnh));

        CarregarAtivacaoAtual();
        CarregarAtivacoesProximas();

        return view;

    }

    protected  void CarregarAtivacaoAtual(){
        AtivacaoEntity ativacaoAtual = ObterAtivacaoAtual();

        if(ativacaoAtual != null) {
            LayoutInflater inflater =(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            viewAtivacaoAtual = inflater.inflate(R.layout.design_item_ativacao, null);
            CarregarViewAtivacaoAtual(ativacaoAtual);
            llAtivacaoAtual.addView(viewAtivacaoAtual);
        }
    }

    protected  void CarregarAtivacoesProximas(){
        ArrayList<AtivacaoEntity>  itens = ObterProximasAtivacoes();
        if(itens != null && itens.size() > 0){
            DesignItemAtivacaoAdapter designItemAtivacaoAdapter = new DesignItemAtivacaoAdapter(ctx, itens);
            lvAtivacoes.setAdapter(designItemAtivacaoAdapter);
        }
    }

    protected  void CarregarViewAtivacaoAtual(AtivacaoEntity ativacaoAtual){

        TextView tvLviAtivacaoNumero = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoNumero);
        tvLviAtivacaoNumero.setText(String.format(ctx.getString(R.string.AtivacoesActivity_tvLviAtivacaoNumero), ativacaoAtual.Numero));

        TextView tvLviAtivacaoData = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoData);
        tvLviAtivacaoData.setText(ativacaoAtual.Data);

        TextView tvLviAtivacaoProcesso = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoProcesso);
        tvLviAtivacaoProcesso.setText(ativacaoAtual.Processo);

        TextView tvLviAtivacaoTransportadora = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoTransportadora);
        tvLviAtivacaoTransportadora.setText(ativacaoAtual.Transportadora);

        TextView tvLviAtivacaoOrigemTitulo = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoOrigemTitulo);
        tvLviAtivacaoOrigemTitulo.setText(ativacaoAtual.OrigemTitulo);

        TextView tvLviAtivacaoOrigemOrigemDesc = viewAtivacaoAtual.findViewById(R.id.tvLviAtivacaoOrigemOrigemDesc);
        tvLviAtivacaoOrigemOrigemDesc.setText(ativacaoAtual.ListaItinerariosStr);

        Button btnAtivacaoRegistroDanfe = viewAtivacaoAtual.findViewById(R.id.btnAtivacaoRegistroDanfe);
        btnAtivacaoRegistroDanfe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, DanfeActivity.class);
                intent.putExtra("AtivacaoNumero",ativacaoAtual.Numero);
                startActivity(intent);
            }
        });

        Button btnAtivacaoEventos = viewAtivacaoAtual.findViewById(R.id.btnAtivacaoEventos);
        //noinspection CommentedOutCode
        btnAtivacaoEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ctx, EventosActivity.class);
                //intent.putExtra("AtivacaoNumero",ativacaoAtual.Numero);
                //startActivity(intent);

                Toast.makeText(ctx, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnAtivacaoNotificacoes = viewAtivacaoAtual.findViewById(R.id.btnAtivacaoNotificacoes);
        btnAtivacaoNotificacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, NotificacoesActivity.class);
                intent.putExtra("AtivacaoNumero",ativacaoAtual.Numero);
                startActivity(intent);
            }
        });

        Button btnAtivacaoTipoViagem = viewAtivacaoAtual.findViewById(R.id.btnAtivacaoTipoViagem);
        btnAtivacaoTipoViagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(ctx, TipoViagemActivity.class);
                //intent.putExtra("AtivacaoNumero",ativacaoAtual.Numero);
                //startActivity(intent);

                Toast.makeText(ctx, "Funcionalidade em desenvolvimento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected AtivacaoEntity ObterAtivacaoAtual(){
        for(AtivacaoEntity ativacaoEntity : this.ativacoes){
            if(ativacaoEntity.ViagemEmAndamento.equals(true)){
                userInfo.tempoAttEtaSet(ativacaoEntity.TempoAttEta);
                return ativacaoEntity;
            }
        }
        return null;
    }

    protected ArrayList<AtivacaoEntity> ObterProximasAtivacoes(){
        ArrayList<AtivacaoEntity> proximasAtivacoes = new ArrayList<>();

        for(AtivacaoEntity ativacaoEntity : this.ativacoes){
            if(!ativacaoEntity.ViagemEmAndamento.equals(true)){
                proximasAtivacoes.add(ativacaoEntity);
            }
        }
        return proximasAtivacoes;
    }
}