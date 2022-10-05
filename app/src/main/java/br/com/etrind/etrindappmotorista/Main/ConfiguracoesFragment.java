package br.com.etrind.etrindappmotorista.Main;

import static br.com.etrind.etrindappmotorista.Infra.Constantes.LOG_TIPO_LOCALIZACAO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import br.com.etrind.etrindappmotorista.Business.LogBusiness;
import br.com.etrind.etrindappmotorista.Entity.AtivacaoEntity;
import br.com.etrind.etrindappmotorista.Entity.LogEntity;
import br.com.etrind.etrindappmotorista.Entity.Result.GenericResult;
import br.com.etrind.etrindappmotorista.Infra.Constantes;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemAtivacaoAdapter;
import br.com.etrind.etrindappmotorista.Main.Design.DesignItemLogAdapter;
import br.com.etrind.etrindappmotorista.R;

@SuppressWarnings({"Convert2Lambda", "CommentedOutCode"})
public class ConfiguracoesFragment extends Fragment {
    private Context ctx;
    private ListView lvLogLocalizacao;
    LogBusiness logBusiness;

    public ConfiguracoesFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);

        ctx =  getActivity();
        lvLogLocalizacao = view.findViewById(R.id.lvFragmentConfiguracoesLogLocalizacao);
        logBusiness = new LogBusiness(ctx);

        Spinner spTipoLog = view.findViewById(R.id.spFragmentConfiguracoesLogTipoLog);
        String[] items = new String[]{ Constantes.LOG_TIPO_LOCALIZACAO, Constantes.LOG_TIPO_TEMPO_ESTIMADO_VIAGEM };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx, android.R.layout.simple_spinner_dropdown_item, items);
        spTipoLog.setAdapter(adapter);

        spTipoLog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedText = parentView.getItemAtPosition(position).toString();
                CarregarLog(selectedText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return view;

    }

    protected  void CarregarLog(String TipoLog){

        GenericResult result = logBusiness.Listar(TipoLog);
        ArrayList<LogEntity> logEntities = new ArrayList<>();

        if(result.ResultOK){
            logEntities = (ArrayList<LogEntity>)result.ResultData;
        }else{
            Toast.makeText(ctx, result.Message, Toast.LENGTH_LONG).show();
        }

        if(logEntities != null && logEntities.size() > 0){
            DesignItemLogAdapter designItemAtivacaoAdapter = new DesignItemLogAdapter(ctx, logEntities);
            lvLogLocalizacao.setAdapter(designItemAtivacaoAdapter);
        }
    }


}